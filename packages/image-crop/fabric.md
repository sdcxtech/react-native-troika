# 迁移原生组件到 RN 新架构

本文介绍如何迁移原生组件到 RN 新架构，以 `ImageCropView` 为例。

官方文档已有示范，介绍如何定义属性、回调以及调用方法。 本文作了一些补充，以及一些注意事项。

如果想要了解更高级的用法，可以参考社区开源组件。

## 官方文档

- [Native Components](https://reactnative.dev/docs/fabric-native-components-introduction)

  介绍 Codegen、属性、回调

- [Invoking native functions on your native component](https://reactnative.dev/docs/next/the-new-architecture/fabric-component-native-commands)

  介绍如何调用原生组件的方法

## 定义规范

```ts
import type { CodegenTypes, HostComponent, ViewProps } from 'react-native';
import { codegenNativeComponent, codegenNativeCommands } from 'react-native';

export interface ObjectRect {
  top: CodegenTypes.Float;
  left: CodegenTypes.Float;
  width: CodegenTypes.Float;
  height: CodegenTypes.Float;
}

export type OnCropEventPayload = {
  uri: string;
};

export interface NativeProps extends ViewProps {
  fileUri?: string;
  cropStyle?: CodegenTypes.WithDefault<'circular' | 'default', 'default'>;
  objectRect?: ObjectRect;
  onCrop?: CodegenTypes.DirectEventHandler<OnCropEventPayload>;
}

export interface NativeCommands {
  crop: (viewRef: React.ElementRef<HostComponent<NativeProps>>) => void;
}

export const Commands: NativeCommands = codegenNativeCommands<NativeCommands>({
  supportedCommands: ['crop'],
});

export default codegenNativeComponent<NativeProps>('ImageCropView') as HostComponent<NativeProps>;
```

### 定义属性

```ts
export interface ObjectRect {
  top: CodegenTypes.Float;
  left: CodegenTypes.Float;
  width: CodegenTypes.Float;
  height: CodegenTypes.Float;
}

export interface NativeProps extends ViewProps {
  fileUri?: string;
  cropStyle?: CodegenTypes.WithDefault<'circular' | 'default', 'default'>;
  objectRect?: ObjectRect;
}
```

可以看到 ImageCropView 定义了 3 个属性。

注意事项：

- `number` 类型要定义为 `CodegenTypes.Float` 或 `CodegenTypes.Double` 或 `CodegenTypes.Int32`

- `ObjectRect` 在 Android 对应生成的类型是 `ReadableMap`，在 iOS 对应生成的类型是结构体，而不是 `NSDictionary`。

- 属性名不能是 `state`，因为已经预留了。会生成一个名为 `ImageCropViewState` 的 c++ 类型。

### 定义回调

```ts
export type OnCropEventPayload = {
  uri: string;
};

export interface NativeProps extends ViewProps {
  onCrop?: CodegenTypes.DirectEventHandler<OnCropEventPayload>;
}
```

命名约定

- payload 的命名约定为 `事件名` + `EventPayload`。

### 定义方法

```ts
export interface NativeCommands {
  crop: (viewRef: React.ElementRef<HostComponent<NativeProps>>) => void;
}

export const Commands: NativeCommands = codegenNativeCommands<NativeCommands>({
  supportedCommands: ['crop'],
});
```

如果需要传递参数

```ts
export interface NativeCommands {
  crop: (
    viewRef: React.ElementRef<HostComponent<NativeProps>>,
    arg1: string,
    arg2: CodegenTypes.Float,
  ) => void;
}
```

## 封装

有些时候，并不希望直接暴露原生组件，而是希望封装一层，提供更友好的 API。

```tsx
import React, { useImperativeHandle, useRef } from 'react';
import ImageCropNativeComponent, { Commands } from './ImageCropNativeComponent';
import type { ObjectRect, OnCropEventPayload, NativeProps } from './ImageCropNativeComponent';
import type { NativeSyntheticEvent } from 'react-native';

export type OnCropEvent = NativeSyntheticEvent<OnCropEventPayload>;
export type { ObjectRect };

export interface ImageCropViewInstance {
  crop: () => void;
}

export type ImageCropViewProps = NativeProps;

const ImageCropView = React.forwardRef<ImageCropViewInstance, ImageCropViewProps>((props, ref) => {
  const viewRef = useRef<React.ComponentRef<typeof ImageCropNativeComponent>>(null);
  useImperativeHandle(ref, () => ({
    crop: () => {
      if (viewRef.current) {
        Commands.crop(viewRef.current);
      }
    },
  }));
  return <ImageCropNativeComponent {...props} ref={viewRef} />;
});

export default ImageCropView;
```

我们导出了合成事件 `OnCropEvent` 以及其它公共 API。

我们还对组件方法进行了封装，提供了更友好的 API。

## 使用

```tsx
function ImageCropPage({ fileUri }: Props) {
  const cropViewRef = useRef<ImageCropViewInstance>(null);

  const doCrop = useCallback(() => {
    cropViewRef.current?.crop();
  }, [cropViewRef]);

  const onCrop = (event: OnCropEvent) => {
    console.log('uri = ', event.nativeEvent.uri);
  };

  return <ImageCropView ref={cropViewRef} fileUri={fileUri} onCrop={onCrop} />;
}
```

## 配置

```json
// package.json
{
  "codegenConfig": {
    "name": "imagecrop", // c++ 库的名字
    "type": "components", // codegen 的类型，可以是 components, modules, all
    "jsSrcsDir": "src", // 源码目录, 自动寻找后缀为 NativeComponent 的文件
    "android": {
      "javaPackageName": "com.reactnative.imagecrop"
    },
    "ios": {
      "componentProvider": {
        "ImageCropView": "RNImageCropView" // RN 组件名称和 iOS 类名对应关系
      }
    }
  }
}
```

```js
// react-native.config.js
module.exports = {
  dependency: {
    platforms: {
      android: {
        libraryName: 'imagecrop',
        componentDescriptors: ['ImageCropViewComponentDescriptor'],
      },
    },
  },
};
```

```groovy
// android/build.gradle 拷贝可用，替换 namespace 和配置依赖即可
buildscript {
  repositories {
    mavenCentral()
    google()
  }

  dependencies {
    classpath "com.android.tools.build:gradle:8.7.2"
  }
}

apply plugin: 'com.android.library'
apply plugin: 'com.facebook.react'

def safeExtGet(prop, fallback) {
  rootProject.ext.has(prop) ? rootProject.ext.get(prop) : fallback
}

android {

  def agpVersion = com.android.Version.ANDROID_GRADLE_PLUGIN_VERSION.tokenize('.')
  // Check AGP version for backward compatibility w/react-native versions still on gradle plugin 6
  def major = agpVersion[0].toInteger()
  def minor = agpVersion[1].toInteger()
  if ((major == 7 && minor >= 3) || major >= 8) {
    namespace "com.reactnative.imagecrop" // 替换为你的 namespace
    buildFeatures {
      buildConfig true
    }
  }

  compileSdkVersion safeExtGet('compileSdkVersion', 35)
  buildToolsVersion safeExtGet('buildToolsVersion', '35.0.0')

  defaultConfig {
    minSdkVersion safeExtGet('minSdkVersion', 24)
    targetSdkVersion safeExtGet('targetSdkVersion', 35)
  }

  sourceSets {
    main {
      java.srcDirs += [
        "generated/java",
        "generated/jni"
      ]
    }
  }
}

repositories {
  google()
  mavenCentral()
}

dependencies {
  implementation 'com.github.yalantis:ucrop:2.2.6' // 替换为你的依赖库
  api 'com.facebook.react:react-native:+'
}
```

```ruby
# ios/RNImageCrop.podspec
require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNImageCrop"
  s.version      = package["version"]
  s.summary      = package["description"]

  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]
  s.platforms    = { :ios => min_ios_version_supported }  # 注意这行
  s.source       = { :git => "https://github.com/github-account/image-crop.git", :tag => "#{s.version}" }

  s.source_files = "ios/ImageCrop/**/*.{h,m,mm}" # 替换为你的源码目录
  s.dependency "TOCropViewController" # 替换为你的依赖库
  install_modules_dependencies(s)  # 注意这行
end
```

## 迁移 Android 代码

Android 的迁移代码较为简单，只需要修改原有 `ImageCropViewManager.java` 文件

### 运行 codegen 生成代码

在主工程 android 目录下运行

```bash
./gradlew generateCodegenArtifactsFromSchema
```

会生成 java 和 jni 目录。

![fabric-2025-12-23-00-50-08](https://todoit.oss-cn-shanghai.aliyuncs.com/assets/fabric-2025-12-23-00-50-08.png)

如果无法生成，通常是类型定义错误，查看日志进行排查。

### 接口和代理

修改 `ImageCropViewManager.java` 文件，将会用到生成的两个 java 类。

```java
public class ImageCropViewManager extends ViewGroupManager<ImageCropView>
	implements ImageCropViewManagerInterface<ImageCropView> {
	private final ImageCropViewManagerDelegate<ImageCropView, ImageCropViewManager> mDelegagte = new ImageCropViewManagerDelegate<>(this);

	private static final String REACT_CLASS = "ImageCropView";

	@Override
	protected ViewManagerDelegate<ImageCropView> getDelegate() {
		return mDelegagte;
	}

	@NonNull
	@Override
	public String getName() {
		return REACT_CLASS;
	}
}
```

### 属性

由原先 通过 `@ReactProp` 注解定义的属性，改为通过接口和代理实现。

```java
@ReactProp(name = "fileUri")
public void setFileUri(ImageCropView view, @Nullable String uri) {
  view.setFileUri(uri);
}
```

改为：

```java
@Override
public void setFileUri(ImageCropView view, @Nullable String uri) {
  view.setFileUri(uri);
}
```

## 回调

和原先没有太大区别，通过定义事件类型如 `OnCropEvent` 来实现。

在 `getExportedCustomDirectEventTypeConstants` 方法中，注册事件。

```java
@Nullable
@Override
public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
  return MapBuilder.of(
    OnCropEvent.Name, MapBuilder.of("registrationName", OnCropEvent.JSEventName)
  );
}
```

在恰当的时机发布事件。

```java
private void onCropped(String uri) {
  FLog.i(TAG, "onCropped:" +uri);
  int surfaceId = UIManagerHelper.getSurfaceId(getContext());
  int viewId = getId();
  EventDispatcher eventDispatcher = UIManagerHelper.getEventDispatcherForReactTag((ReactContext) getContext(), viewId);
  if (eventDispatcher != null) {
    OnCropEvent event = new OnCropEvent(surfaceId, viewId, uri);
    eventDispatcher.dispatchEvent(event);
  }
}
```

## 方法

比原先简单，直接实现即可。

```java
@Override
public void crop(ImageCropView view) {
  view.crop();
}
```

## 迁移 iOS 代码

迁移 iOS 代码较为复杂，首先，没有 `RNImageCropViewManager` 类了，相关逻辑合并到 `RNImageCropView` 本身。

首先，重命名 `RNImageCropView.m` 为 `RNImageCropView.mm`。

然后运行 `bundle exec pod install`，这会自动生成相关 c++ 代码。

### 修改头文件

继承 `RCTViewComponentView`

```objc
#import <UIKit/UIKit.h>
#import <React/RCTViewComponentView.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNImageCropView : RCTViewComponentView

@end

NS_ASSUME_NONNULL_END
```

### 导入依赖

接下来修改 `RNImageCropView.mm` 文件，导入依赖。

留意 `using namespace facebook::react;` 别漏了。

```cpp
#import <react/renderer/components/imagecrop/ComponentDescriptors.h>
#import <react/renderer/components/imagecrop/EventEmitters.h>
#import <react/renderer/components/imagecrop/Props.h>
#import <react/renderer/components/imagecrop/RCTComponentViewHelpers.h>

#import <React/RCTConversions.h>
#import <React/RCTLog.h>

using namespace facebook::react;
```

### 常规模版代码

```objc
@implementation RNImageCropView

// Needed because of this: https://github.com/facebook/react-native/pull/37274
+ (void)load {
  [super load];
}

+ (ComponentDescriptorProvider)componentDescriptorProvider {
  return concreteComponentDescriptorProvider<ImageCropViewComponentDescriptor>();
}

+ (BOOL)shouldBeRecycled {
  return NO;
}
@end
```

### 属性

```objc
@interface RNImageCropView() <RCTImageCropViewViewProtocol>

@property(nonatomic, copy, nonnull) NSString *fileUri;

// 留意这个属性类型，对应 TS 定义的 cropStyle
@property(nonatomic, assign) ImageCropViewCropStyle cropStyle;

// 留意这个属性类型，对应 TS 定义的 objectRect
@property(nonatomic, assign) ImageCropViewObjectRectStruct objectRect;

@end
```

它们生成的 c++ 类型如下：

```cpp
// react/renderer/components/imagecrop/Props.h
namespace facebook::react {

  enum class ImageCropViewCropStyle { Circular, Default };

  struct ImageCropViewObjectRectStruct {
    Float top{0.0};
    Float left{0.0};
    Float width{0.0};
    Float height{0.0};
  };
}
```

如何更新属性，参考如下代码：

```cpp
- (instancetype)initWithFrame:(CGRect)frame {
  if ((self = [super initWithFrame:frame])) {
    static const auto defaultProps = std::make_shared<const ImageCropViewProps>();
    _props = defaultProps;
    _cropStyle = ImageCropViewCropStyle::Default;
  }
  return self;
}

- (void)updateProps:(const facebook::react::Props::Shared &)props oldProps:(const facebook::react::Props::Shared &)oldProps {
  const auto &oldViewProps = static_cast<const ImageCropViewProps &>(*_props);
  const auto &newViewProps = static_cast<const ImageCropViewProps &>(*props);

  // `fileUri`
  if (newViewProps.fileUri != oldViewProps.fileUri) {
    self.fileUri = RCTNSStringFromStringNilIfEmpty(newViewProps.fileUri);
  }

  // `cropStyle`
  if (newViewProps.cropStyle != oldViewProps.cropStyle) {
    self.cropStyle = newViewProps.cropStyle;
  }

  // `objectRect`
  if (newViewProps.objectRect.width != oldViewProps.objectRect.width || newViewProps.objectRect.height != oldViewProps.objectRect.height || newViewProps.objectRect.top != oldViewProps.objectRect.top || newViewProps.objectRect.left != oldViewProps.objectRect.left) {
    self.objectRect = newViewProps.objectRect;
  }

  [self addCropView:self.croppingStyle image:self.image];

  [super updateProps:props oldProps:oldProps];
}
```

### 回调

```cpp
- (const ImageCropViewEventEmitter &)eventEmitter {
  return static_cast<const ImageCropViewEventEmitter &>(*_eventEmitter);
}

- (void)saveImage:(UIImage*)image {
  BOOL result = // 保存图片到本地
  if(result ==YES) {
    NSString *uri = [[NSURL alloc] initFileURLWithPath:filePath].absoluteString;
    [self eventEmitter].onCrop(ImageCropViewEventEmitter::OnCrop{
      .uri = RCTStringFromNSString(uri)
    });
  }
}
```

### 方法

```objc
@interface RNImageCropView() <RCTImageCropViewViewProtocol>

@end

@implementation RNImageCropView

- (void)handleCommand:(const NSString *)commandName args:(const NSArray *)args {
  RCTImageCropViewHandleCommand(self, commandName, args);
}

- (void)crop {
   // 实现裁剪逻辑
}
@end
```

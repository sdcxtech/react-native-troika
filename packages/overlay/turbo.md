# RN 新架构迁移指南 -- 原生模块

本文介绍如何迁移原生组件到 RN 新架构。

官方文档已有示范，介绍如何定义方法、参数以及事件。 本文作了一些补充，以及一些注意事项。

## 官方文档

- [Native Modules](https://reactnative.dev/docs/turbo-native-modules-introduction)

  介绍 Codegen、方法与参数

- [Emitting Events in Native Modules](https://reactnative.dev/docs/next/the-new-architecture/native-modules-custom-events)

  介绍如何发布事件

- [Native Modules Lifecycle](https://reactnative.dev/docs/next/the-new-architecture/native-modules-lifecycle)

  介绍如何释放资源

## 定义规范

模块的规范文件必须以 `Native` 作为前缀

> 组件的规范文件以 `NativeComponent` 作为后缀

```ts
// NativeOverlay.ts
import type { TurboModule, CodegenTypes } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface OverlayOptions {
  overlayId: CodegenTypes.Int32;
  passThroughTouches?: boolean;
}

export interface Spec extends TurboModule {
  show(moduleName: string, options: OverlayOptions): void;
  hide(moduleName: string, overlayId: CodegenTypes.Int32): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('OverlayHost');
```

### 参数

- 在 Android 中，所有 `number` 类型都被映射为 `double`，尽管定义为 `CodegenTypes.Int32`， 这和原生组件不一样。

- 对象类型，如 `options`，在 Andriod 对应生成的类型是 `ReadableMap`，在 iOS 对应生成的类型是结构体， 而不是 `NSDictionary`。

- 如果希望 iOS 的对象类型也是 `NSDictionary`，那么需要使用 `CodegenTypes.UnsafeObject`。

- 参数可以是函数。如 `callback: (error: Error | null, index: number) => void`
  但是注意 `Error` 需要自定义类型，RN 自带的 `Error` 类型无法被 codegen 识别。

  ```ts
  type Error = {
    code: string;
    message: string;
  };
  ```

### 返回值

如果返回值是 `void` 或 Promise，那么这是个异步方法。

```ts
export interface Spec extends TurboModule {
  show(moduleName: string, options: OverlayOptions): void;
}
```

对应生成的 Android 代码为：

```java
@ReactMethod
@DoNotStrip
public abstract void show(String moduleName, ReadableMap options);
```

如果有返回值，类型不是 Promise，那么这是个同步方法。

```ts
export interface Spec extends TurboModule {
  show(moduleName: string, options: OverlayOptions): CodegenTypes.Int32;;
}
```

生成代码如下

```java
@ReactMethod(isBlockingSynchronousMethod = true)
@DoNotStrip
public abstract double show(String moduleName, ReadableMap options);
```

## 封装

一般不直接暴露原生模块，而是封装一层，提供更友好的 API。

## 配置

```json
// package.json
{
  "codegenConfig": {
    "name": "overlay", // 库的名字，不是模块的名字，一个库可以有多个模块
    "type": "modules",
    "jsSrcsDir": "src",
    "android": {
      "javaPackageName": "com.reactnative.overlay"
    },
    "ios": {
      "modulesProvider": {
        "OverlayHost": "RNOverlayModule" // RN 模块名称和 iOS 类名对应关系
      }
    }
  }
}
```

gradle 的配置和原生组件一致。

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
    namespace "com.reactnative.overlay" // 替换为你的 namespace
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
  api 'com.facebook.react:react-native:+'
}
```

podspec 的配置和原生组件一致。

```ruby
# RNOverlay.podspec
require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "RNOverlay"
  s.version      = package["version"]
  s.summary      = package["description"]

  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]
  s.platforms    = { :ios => min_ios_version_supported }
  s.source       = { :git => "https://github.com/github-account/overlay.git", :tag => "#{s.version}" }

  s.source_files = "ios/Overlay/**/*.{h,m,mm}"
  install_modules_dependencies(s)
end

```

## 迁移 Android 代码

### 运行 codegen 生成代码

在主工程 android 目录下运行

```bash
./gradlew generateCodegenArtifactsFromSchema
```

在模块的 android/build/generated/source/codegen 目录下，会生成一个 `java` 目录和一个 `jni` 目录。

![turbo-2025-12-28-16-15-11](https://todoit.oss-cn-shanghai.aliyuncs.com/assets/turbo-2025-12-28-16-15-11.png)

### 实现模块

模块的实现非常简单，只需要继承生成的 `NativeOverlaySpec` 类，并实现其中的抽象方法即可。

```java
public class OverlayModule extends NativeOverlaySpec {
  @NonNull
  @Override
  public String getName() {
    return "OverlayHost";
  }
}
```

### 事件

在 spec 中按如下方式定义事件：

```ts
export type OnShownEvent = {
  overlayId: CodegenTypes.Int32;
};

export interface Spec extends TurboModule {
  readonly onShown: CodegenTypes.EventEmitter<OnShownEvent>;
}
```

值得注意的是 `onShown` 返回一个 `EventSubscription`，需要调用 `remove` 方法来移除事件监听。

对应生成的 Android 代码为：

```java
// NativeOverlaySpec.java
protected final void emitOnShown(ReadableMap value) {
  mEventEmitterCallback.invoke("onShown", value);
}
```

在需要发布事件时，调用 `emitOnShown` 方法即可。

### 生命周期

在 React Native 中，原生模块采用单例模式。原生模块基础架构会在首次访问时延迟创建模块，并在应用需要时保持其存在。这种性能优化避免了在应用启动时立即创建原生模块所带来的开销，从而确保更快的启动速度。

在纯 React Native 应用中，原生模块只会创建一次，并且永远不会被销毁。然而，在更复杂的应用中，可能会出现原生模块被销毁并重新创建的情况。

对于有状态的本地模块，可能需要正确地使本地模块失效，以确保状态重置且资源释放。

可以通过重写 `initialize` 或 `invalidate` 方法来管理状态和资源。

```java
// OverlayModule.java
@Override
public void initialize() {
  //
}

@Override
public void invalidate() {
 // 释放资源
}
```

### 获取模块实例

通过 `ReactContext` 获取模块实例

```java
ReactContext reactContext = // 获取 ReactContext 实例
OverlayModule overlayModule = reactContext.getNativeModule(OverlayModule.class);
```

## 迁移 iOS 代码

首先，重命名 `RNOverlayModule.m` 为 `RNOverlayModule.mm`。

然后运行 `bundle exec pod install`，这会自动生成相关 c++ 代码。

### 修改头文件

```objc

#import <Foundation/Foundation.h>
#import <overlay/overlay.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNOverlayModule : NSObject <NativeOverlaySpec>

@end

NS_ASSUME_NONNULL_END
```

### 实现模块

实现模块较为简单，只要实现 `NativeOverlaySpec` 协议即可。

此外，需要实现 `getTurboModule` 方法，返回一个 `NativeOverlaySpecJSI` 实例。

```objc
// RNOverlayModule.mm
RCT_EXPORT_MODULE(OverlayHost)

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:(const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeOverlaySpecJSI>(params);
}
```

### 事件

在 spec 中按如下方式定义事件：

```ts
export type OnShownEvent = {
  overlayId: CodegenTypes.Int32;
};

export interface Spec extends TurboModule {
  readonly onShown: CodegenTypes.EventEmitter<OnShownEvent>;
}
```

值得注意的是 `onShown` 返回一个 `EventSubscription`，需要调用 `remove` 方法来移除事件监听。

codegen 会帮我们在 `overlay.h` 中生成一个名为 `NativeOverlaySpecBase` 的类，这个类实提供了 `emitOnShown` 方法。

```objc
// overlay.h
@interface NativeOverlaySpecBase : NSObject

- (void)emitOnShown:(NSDictionary *)value;

@end
```

让 `RNOverlayModule` 继承 `NativeOverlaySpecBase`，并在恰当时机调用 `emitOnShown` 方法即可。

```objc
// RNOverlayModule.h
#import <Foundation/Foundation.h>
#import <overlay/overlay.h>

@interface RNOverlayModule : NativeOverlaySpecBase <NativeOverlaySpec>

@end
```

### 生命周期

和 Android 只需要重写 `initialize` 或 `invalidate` 方法不一样。

iOS 有两个相关协议：`RCTInitializing` 和 `RCTInvalidating`。

在头文件中声明实现这两个协议。

```objc
// RNOverlayModule.h
#import <Foundation/Foundation.h>
#import <overlay/overlay.h>
#import <React/RCTInitializing.h>
#import <React/RCTInvalidating.h>

@interface RNOverlayModule : NativeOverlaySpecBase <NativeOverlaySpec, RCTInitializing, RCTInvalidating>

@end
```

在实现文件中实现这两个协议。

```objc
// RNOverlayModule.mm
@implementation RNOverlayModule

- (void)initialize {
    // 初始化
}

- (void)invalidate {
    // 释放资源
}

@end
```

### 获取模块实例

通过 `RCTHost` 获取模块实例

```objc
RCTHost *rctHost = // 获取 RCTHost 实例
RNOverlayModule *overlayModule = [[rctHost moduleRegistry] moduleForClass:[RNOverlayModule class]];
```

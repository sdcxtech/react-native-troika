# ImageCropView

`ImageCropView` 是一个 React Native 原生 UI 组件，用于头像裁剪以及图片裁剪，同时支持图像主体识别后设置需要裁剪的主体区域。

## 版本兼容

| 版本 | RN 版本 | RN 架构 |
| ---- | ------- | ------- |
| 0.x  | < 0.82  | 旧架构  |
| 1.x  | >= 0.82 | 新架构  |

## Installation

```bash
yarn add @sdcx/image-crop
# &
cd ios && bundle exec pod install
```

## Usage

```tsx
<ImageCropView
  ref={imageCropViewRef}
  style={'your style'}
  fileUri={'your file uri'}
  cropStyle={'circular' | 'default'}
  onCrop={(uri: string) => {}}
  objectRect={objectRect}
/>
```

#### cropStyle

默认 default 为裁剪矩形，若需要裁剪圆形头像，则设为 circular；

#### objectRect

objectRect 可设置默认的图片裁剪区域，且当 cropStyle 为 default 时有效。

objectRect 的四个属性分别是（单位都是像素 px）：

1. left: 裁剪区域离图片左边的距离；
2. top: 裁剪区域离图片上边的距离；
3. width: 裁剪区域的宽度；
4. height: 裁剪区域的高度；

#### 裁剪

```
imageCropViewRef.crop()
```

然后裁剪成功后会在 onCropped 属性中回调裁剪后图片的 uri；

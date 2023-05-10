# ImageCropView

`ImageCropView` 是一个 React Native 原生 UI 组件，用于头像裁剪以及图片裁剪，同时支持图像主体识别后设置需要裁剪的主体区域。

## Installation

```bash
yarn add @sdcx/image-crop
# &
pod install
```

## Usage

```
 <ImageCropView
    style={'your style'}
    fileUri={'your file uri'}
    cropStyle={'circular' | 'default'}
    onCropped={(uri: string) => {}}
    objectRect={objectRect}
  />
```
#### cropStyle
默认default为裁剪矩形，若需要裁剪圆形头像，则设为circular；

#### objectRect
objectRect可设置默认的图片裁剪区域，且当cropStyle为default时有效。

objectRect的四个属性分别是（单位都是像素px）：
1. left: 裁剪区域离图片左边的距离；
2. top: 裁剪区域离图片上边的距离；
3. width: 裁剪区域的宽度；
4. height: 裁剪区域的高度；


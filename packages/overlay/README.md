# Overlay

`Overlay` 是一个 React Native 原生 UI 基础设施，它漂浮在你的 React Native 应用之上，可用于实现 Modal, Alert, Toast, Popover, Notification, Hoverball 等顶层 UI。

> 这个库属于实验性质，请谨慎使用

## 版本兼容

| 版本 | RN 版本 | RN 架构 |
| ---- | ------- | ------- |
| 0.x  | < 0.82  | 旧架构  |
| 1.x  | >= 0.82 | 新架构  |

## Installation

```bash
yarn add @sdcx/overlay
# &
pod install
```

在你的项目更目录下添加或修改 react-native.config.js 文件，内容如下：

```js
// react-native.config.js
module.exports = {
  dependencies: {
    '@sdcx/overlay': {
      platforms: {
        android: {
          packageInstance: 'new OverlayPackage(getReactNativeHost())',
        },
      },
    },
  },
};
```

## Usage

请查看 Hoverball 示例代码

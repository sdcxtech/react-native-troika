# React Native 原生 UI 组件

本仓库包含一系列原生组件：

## 版本兼容

| 版本 | RN 版本 | 新旧架构 |
| ---- | ------- | -------- |
| 0.x  | <0.73   | 旧架构   |
| 0.x  | 0.73    | 旧架构   |

| RN 版本       | iOS               | Android                         | 三方库版本                                                                                                |
| ------------- | ----------------- | ------------------------------- | --------------------------------------------------------------------------------------------------------- |
| 0.72(2023.06) | 最低支持 iOS 12.4 |                                 |                                                                                                           |
| 0.73(2023.11) | 最低支持 iOS 13.4 | 支持 Android 14<br>必须 Java 17 | react-native-gesture-handler@2.14.0,<br>react-native-reanimated@3.15.0,<br>react-native-drop-shadow@1.0.0 |
| 0.74(2024.04) |                   | minSdkVersion 23                |                                                                                                           |
| 0.75(2024.08) |                   |                                 |                                                                                                           |
| 0.76(2024.10) | 最低支持 iOS 15.1 | minSdkVersion 24                |                                                                                                           |
| 0.77(2025.01) |                   | 支持 Android 15                 |                                                                                                           |
| 0.78(2025.02) |                   |                                 |                                                                                                           |
| 0.79(2025.04) |                   |                                 |                                                                                                           |

## 库

### [NestedScrollView](./packages/nested-scroll/README.md)

用于实现嵌套滚动，使用简单。可以和 PagerView，TabView 等组合使用。

<img src="./packages/nested-scroll/docs/assets/struct.png">

### [PullToRefresh](./packages/pull-to-refresh/README.md)

提供了在 React 层自定义下拉刷新的能力。

<img src="./packages/pull-to-refresh/docs/assets/separated.gif" width="320">

### [BottomSheet](./packages/bottom-sheet/README.md)

将 Android 的 [BottomSheetBehavior](https://developer.android.com/reference/com/google/android/material/bottomsheet/BottomSheetBehavior) 迁移到了 React Native 中，在 API 设计上也尽量和 Android 保持一致，同时支持 iOS。

<img src="./packages/bottom-sheet/docs/assets/pagerview.gif" width="320">

### [ActivityIndicator](./packages/activity-indicator/README.md)

在 Android 上实现了和 iOS 类似的菊花组件。

<img src="./packages/activity-indicator/docs/assets/activity.png" width="320">

### [ImageCropView](./packages/image-crop/README.md)

用来实现头像裁剪，和社区其它方安不同，仅仅只是个 View，非常方便页面的自定义布局。

也可以用来实现图片裁剪，支持设置裁剪区域。

### [KeyboardInsetsView](./packages/keyboard-insets/README.md)

KeyboardInsetsView 是一个 React Native 原生 UI 组件，用于处理软键盘遮挡输入框的问题。自动模式下使用非常简单，不需要额外代码来处理键盘。

如果想要实现类似聊天界面那样的效果，也不在话下。

<img src="./packages/keyboard-insets/docs/assets/chat.gif" width="320">

### [Overlay](./packages/overlay/README.md)

`Overlay` 是一个 React Native 原生 UI 基础设施，它漂浮在你的 React Native 应用之上，可用于实现 Modal, Alert, Toast, Popover, Notification, Hoverball 等顶层 UI。

### [WheelPicker](./packages/wheel-picker/README.md)

一个非常简单的 WheelPicker

一个非常帅的 WheelPicker

<img src="./packages/wheel-picker/docs/assets/wheelpicker.png" width="320">

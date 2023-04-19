# NestedScrollWebView

`NestedScrollWebView` 为 react-native-webview 提供纵向嵌套滚动支持。

因为社区的 [react-native-webview](https://github.com/react-native-webview/react-native-webview) 并没有完全遵循 Android [NestedScrolling API](https://developer.android.com/reference/androidx/core/view/NestedScrollingChild)，导致在嵌套滚动的场景下，WebView 无法正常滚动。

本库修补了这个缺陷，安装即可生效。

## Installation

```sh
yarn add react-native-webview
yarn add @sdcx/nested-scroll-webview
```

## Usage

只是一个补丁，安装即可生效。

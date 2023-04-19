# NestedScrollView

`NestedScrollView` 是一个 React Native 原生 UI 组件。

主要用来实现如下结构的视图：

![README-2023-04-18-17-43-21](https://todoit.oss-cn-shanghai.aliyuncs.com/assets/README-2023-04-18-17-43-21.svg)

最外层是一个可纵向滚动的视图， 也就是我们的 `NestedScrollView`， 它的子组件往往由一个头部和一个可横向滚动的视图组成。

这个可横向滚动的视图在 React Native 中通常是设置了 `horizontal` 属性的 `ScrollView`，也可以是 `PagerView`。

最里层是若干也可以纵向滚动的视图，譬如 `ScrollView`、`FlashList`、`WebView` 等等。

`NestedScrollView` 的作用是协调最里层和最外层可滚动视图之间的（纵向）滚动，使得滚动体验更加流畅。

## Installation

```sh
yarn add @sdcx/nested-scroll
# &
pod install
```

## Usage

`NestedScrollView` 在使用上比较简单

```tsx
import { NestedScrollView, NestedScrollViewHeader } from '@sdcx/nested-scroll'

const App = () => {
  return (
    <NestedScrollView>
      <NestedScrollViewHeader stickyHeaderBeginIndex={1}>
        <Image />
        <TabBar />
      </NestedScrollViewHeader>
      <PagerView>
        <FlatList nestedScrollEnabled />
        <ScrollView nestedScrollEnabled />
        <WebView />
      </PagerView>
    </NestedScrollView>
  )
}
```

> Android 是基于 NestedScrolling API 实现的，因此，记得为最里层的可滚动视图设置 `nestedScrollEnabled` 属性。

## API

### NestedScrollView

`NestedScrollView` 只有两个属于自己的属性

- `bounces`：仅限于 iOS 平台，用于设置 `NestedScrollView` 是否有弹性，默认为 `false`。一旦设置为 `true`，最内层可滚动视图将失去弹性。

- `contentContainerStyle`：仅限于 Android 平台，用于设置 `NestedScrollView` 的 contentView 的样式。

### NestedScrollViewHeader

`NestedScrollViewHeader` 可以通过 `stickyHeaderBeginIndex` 或者 `stickyHeight` 属性来设置 sticky 效果。

此外，`NestedScrollViewHeader` 可以通过 `onScroll` 回调函数来实现头部视图的视差效果。

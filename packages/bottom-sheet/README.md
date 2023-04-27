# BottomSheet

`BottomSheet` 是一个 React Native 原生 UI 组件。

它位于屏幕底部，可拖拽，支持嵌套滚动，可以和可滚动视图（`FlatList`, `FlashList`, `WebView` 等等）一起使用。

|                                                                                                           |                                                                                                           |
| --------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------- |
| <img src="https://todoit.oss-cn-shanghai.aliyuncs.com/assets/troika-2023-04-27-14-47-39.gif" width="320"> | <img src="https://todoit.oss-cn-shanghai.aliyuncs.com/assets/troika-2023-04-27-14-48-40.gif" width="320"> |

## Installation

```bash
yarn install @sdcx/bottom-sheet
# &
pod install
```

## Usage

`BottomSheet` 在使用上是非常简单的，几乎没有什么心智负担。

```tsx
import BottomSheet from '@sdcx/bottom-sheet'

const App = () => {
  return (
    <View style={styles.container}>
      <ScrollView>...</ScrollView>
      <BottomSheet peeekHeight={200}>
        {
          // 在这里放置你的内容，可以是任何组件，如：
        }
        <View />
        <PagerView>
          <FlatList nestedScrollEnabled />
          <ScrollView nestedScrollEnabled />
          <WebView />
        </PagerView>
      </BottomSheet>
    </View>
  )
}
```

> :white_check_mark: 当和可滚动视图一起使用时，需要设置 `nestedScrollEnabled` 属性。

## 基本概念和 API

`BottomSheet` 由内外两层视图组成，外层是绝对定位，默认填满父组件，除非设置了 `top` 样式属性，内层也是绝对定位，默认填满外层视图。外层的位置固定不变，内层则可拖动。

![README-2023-04-18-16-17-39](https://todoit.oss-cn-shanghai.aliyuncs.com/assets/README-2023-04-18-16-17-39.png)

`BottomSheet` 拥有 3 个属性和两个回调。

### 属性

- `peekHeight`, 是指 BottomSheet 收起时，在屏幕上露出的高度，默认是 200。

- `state`, 是指 BottomSheet 的状态，有三种状态：

  - `'collapsed'`，收起状态，此时 BottomSheet 的高度为 `peekHeight`。

  - `'expanded'`，展开状态，此时 BottomSheet 的高度为父组件的高度或内容的高度，参考 `fitToContents` 属性。

  - `'hidden'`，隐藏状态，此时 BottomSheet 的高度为 0。

- `fitToContents`，是指 BottomSheet 在展开时，是否适应内容的高度，默认是 `false`。

### 回调

- `onStateChanged`, 是指 BottomSheet 状态变化时的回调，它和 `state` 属性是一对，用来实现受控模式。

  ```tsx
  export type BottomSheetState = 'collapsed' | 'expanded' | 'hidden'

  export interface StateChangedEventData {
    state: BottomSheetState
  }

  interface NativeBottomSheetProps extends ViewProps {
    onStateChanged?: (event: NativeSyntheticEvent<StateChangedEventData>) => void
  }
  ```

- `onSlide`, 是指 BottomSheet 滑动时的回调，可以用它来实现一些动画效果。

  ```tsx
  export interface OffsetChangedEventData {
    progress: number // 是指 `BottomSheet` 当前的位置在 `collapsedOffset` 和 `expandedOffset` 之间的比例，它的值在 0 和 1 之间。
    offset: number // 是指 `BottomSheet` 当前的位置，它的值在 `collapsedOffset` 和 `expandedOffset` 之间。
    expandedOffset: number // 是指 `BottomSheet` 完全展开时，内层顶部距离外层顶部的距离，通常是 0。但如果设置了 `fitToContents` 属性，则可能大于 0。
    collapsedOffset: number // 是指 `BottomSheet` 完全收起时，内层顶部距离外层顶部的距离。可以看到，它的值等于外层的高度减去 `peekHeight`。
  }

  interface NativeBottomSheetProps extends ViewProps {
    onSlide?: (event: NativeSyntheticEvent<OffsetChangedEventData>) => void
  }
  ```

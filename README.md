# React Native 嵌套滚动三件套

本库包含如下原生组件：

[NestedScrollView](./packages/nested-scroll/README.md)

[PullToRefresh](./packages/pull-to-refresh/README.md)

[BottomSheet](./packages/bottom-sheet/README.md)

这三个组件在 Android 都是基于 [NestedScrolling API](https://developer.android.com/reference/androidx/core/view/NestedScrollingChild) 实现的，在使用它们时，记得为 `ScrollView` 打开 `nestedScrollEnabled` 属性。

|                                                                                               |                                                                                               |                                                                                               |
| --------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------- |
| <img src="https://todoit.oss-cn-shanghai.aliyuncs.com/assets/README-2023-04-27-15-00-51.gif"> | <img src="https://todoit.oss-cn-shanghai.aliyuncs.com/assets/troika-2023-04-27-14-48-40.gif"> | <img src="https://todoit.oss-cn-shanghai.aliyuncs.com/assets/README-2023-02-18-21-36-20.gif"> |

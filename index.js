import App from './App'
import Navigation from 'hybrid-navigation'
import { Platform } from 'react-native'

import './src/PullToRefresh'

import NestedScrollFlatList from './src/NestedScrollFlatList'
import NestedScrollParallaxHeader from './src/NestedScrollParallaxHeader'
import NestedScrollTabView from './src/NestedScrollTabView'
import NestedScrollPagerViewStickyHeader from './src/NestedScrollPagerViewStickyHeader'

import PullRefreshFlatList from './src/components/PullRefreshFlatList'
import PullRefreshPagerView from './src/PullRefreshPagerView'
import PullRefreshFlatListNestedScroll from './src/PullRefreshFlatListNestedScroll'
import PullRefreshPagerViewNestedScroll from './src/PullRefreshPagerViewNestedScroll'
import PullRefreshNestedScrollPagerView from './src/PullRefreshNestedScrollPagerView'

import BottomSheetWithoutScrollView from './src/BottomSheetWithoutScrollView'
import BottomSheetFlashList from './src/BottomSheetFlashList'
import BottomSheetPagerView from './src/BottomSheetPagerView'
import BottomSheetBackdropShadow from './src/BottomSheetBackdropShadow'

// 配置全局样式
Navigation.setDefaultOptions({
  topBarStyle: 'dark-content',
  statusBarColorAndroid: Platform.Version > 21 ? undefined : '#4A4A4A',
  scrimAlphaAndroid: 100,
  fitsOpaqueNavigationBarAndroid: true,
  swipeBackEnabledAndroid: true,
})

// 重要必须
Navigation.startRegisterComponent()

// 注意，你的每一个页面都需要注册
Navigation.registerComponent('App', () => App)
Navigation.registerComponent('NestedScrollFlatList', () => NestedScrollFlatList)
Navigation.registerComponent('NestedScrollParallaxHeader', () => NestedScrollParallaxHeader)
Navigation.registerComponent('NestedScrollTabView', () => NestedScrollTabView)
Navigation.registerComponent(
  'NestedScrollPagerViewStickyHeader',
  () => NestedScrollPagerViewStickyHeader,
)

Navigation.registerComponent('PullRefreshFlatList', () => PullRefreshFlatList)
Navigation.registerComponent(
  'PullRefreshFlatListNestedScroll',
  () => PullRefreshFlatListNestedScroll,
)
Navigation.registerComponent('PullRefreshPagerView', () => PullRefreshPagerView)
Navigation.registerComponent(
  'PullRefreshPagerViewNestedScroll',
  () => PullRefreshPagerViewNestedScroll,
)
Navigation.registerComponent(
  'PullRefreshNestedScrollPagerView',
  () => PullRefreshNestedScrollPagerView,
)

Navigation.registerComponent('BottomSheetWithoutScrollView', () => BottomSheetWithoutScrollView)
Navigation.registerComponent('BottomSheetFlashList', () => BottomSheetFlashList)
Navigation.registerComponent('BottomSheetPagerView', () => BottomSheetPagerView)
Navigation.registerComponent('BottomSheetBackdropShadow', () => BottomSheetBackdropShadow)

// 重要必须
Navigation.endRegisterComponent()

Navigation.setRoot({
  stack: {
    children: [{ screen: { moduleName: 'App' } }],
  },
})

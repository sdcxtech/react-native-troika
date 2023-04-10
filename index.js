import App from './App'
import Navigation from 'hybrid-navigation'
import { Platform } from 'react-native'
import NestedScrollFlatList from './src/NestedScrollFlatList'
import NestedScrollParallaxHeader from './src/NestedScrollFlatListParallaxHeader'
import NestedScrollPagerView from './src/NestedScrollPagerView'
import NestedScrollPagerViewStickyHeader from './src/NestedScrollPagerViewStickyHeader'

import PullRefreshFlatList from './src/components/PullRefreshFlatList'
import PullRefreshPagerView from './src/PullRefreshPagerView'
import PullRefreshFlatListNestedScroll from './src/PullRefreshFlatListNestedScroll'
import PullRefreshPagerViewNestedScroll from './src/PullRefreshPagerViewNestedScroll'
import PullRefreshNestedScrollPagerView from './src/PullRefreshNestedScrollPagerView'

// 配置全局样式
Navigation.setDefaultOptions({
  topBarStyle: 'dark-content',
  statusBarColorAndroid: Platform.Version > 21 ? undefined : '#4A4A4A',
  scrimAlphaAndroid: 100,
  fitsOpaqueNavigationBarAndroid: true,
})

// 重要必须
Navigation.startRegisterComponent()

// 注意，你的每一个页面都需要注册
Navigation.registerComponent('App', () => App)
Navigation.registerComponent('NestedScrollFlatList', () => NestedScrollFlatList)
Navigation.registerComponent('NestedScrollParallaxHeader', () => NestedScrollParallaxHeader)
Navigation.registerComponent('NestedScrollPagerView', () => NestedScrollPagerView)
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

// 重要必须
Navigation.endRegisterComponent()

Navigation.setRoot({
  stack: {
    children: [{ screen: { moduleName: 'App' } }],
  },
})

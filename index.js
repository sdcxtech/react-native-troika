import App from './App'
import Navigation from 'hybrid-navigation'
import { Platform } from 'react-native'
import 'react-native-gesture-handler'

// 设置全局下拉刷新样式
import './src/pull-to-refresh/PullToRefresh'

import HoverBall from './src/overlay/OverlayScreen'
import { registerNestedScrollComponent } from './src/nested-scroll'
import { registerPullToRefreshComponent } from './src/pull-to-refresh'
import { registerBottomSheetComponent } from './src/bottom-sheet'
import { registerKeyboardComponent } from './src/keyboard-insets'

// 配置全局样式
Navigation.setDefaultOptions({
  topBarStyle: 'dark-content',
  statusBarColorAndroid: Platform.Version > 21 ? undefined : '#4A4A4A',
  scrimAlphaAndroid: 50,
  fitsOpaqueNavigationBarAndroid: true,
  swipeBackEnabledAndroid: true,
})

// 重要必须
Navigation.startRegisterComponent()

// 注意，你的每一个页面都需要注册
Navigation.registerComponent('App', () => App)

registerNestedScrollComponent()
registerPullToRefreshComponent()
registerBottomSheetComponent()
registerKeyboardComponent()

Navigation.registerComponent('HoverBall', () => HoverBall)

// 重要必须
Navigation.endRegisterComponent()

Navigation.setRoot({
  stack: {
    children: [{ screen: { moduleName: 'App' } }],
  },
})

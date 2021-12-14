import App from './App'
import { ReactRegistry, Garden, Navigator } from 'hybrid-navigation'
import { Platform } from 'react-native'
import NestedScroll from './src/NestedScroll'
import AvatarCrop from './src/AvatarCrop'

// 配置全局样式
Garden.setStyle({
  topBarStyle: 'dark-content',
  statusBarColorAndroid: Platform.Version > 21 ? undefined : '#4A4A4A',
})

// 重要必须
ReactRegistry.startRegisterComponent()

// 注意，你的每一个页面都需要注册
ReactRegistry.registerComponent('App', () => App)
ReactRegistry.registerComponent('NestedScroll', () => NestedScroll)
ReactRegistry.registerComponent('AvatarCrop', () => AvatarCrop)

// 重要必须
ReactRegistry.endRegisterComponent()

Navigator.setRoot({
  stack: {
    children: [{ screen: { moduleName: 'App' } }],
  },
})

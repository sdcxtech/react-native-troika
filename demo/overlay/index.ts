import Navigation from 'hybrid-navigation'

import Home from './Home'
import OverlayScreen from './OverlayScreen'
import ToastScreen from './ToastScreen'

export function registerOverlayComponent() {
  Navigation.registerComponent('Overlay', () => Home)
  Navigation.registerComponent('Hoverball', () => OverlayScreen)
  Navigation.registerComponent('Toast', () => ToastScreen)
}

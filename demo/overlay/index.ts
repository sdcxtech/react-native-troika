import Navigation from 'hybrid-navigation'

import Home from './Home'
import HoverballScreen from './HoverballScreen'
import ToastScreen from './ToastScreen'

export function registerOverlayComponent() {
  Navigation.registerComponent('Overlay', () => Home)
  Navigation.registerComponent('Hoverball', () => HoverballScreen)
  Navigation.registerComponent('Toast', () => ToastScreen)
}

import Navigation from 'hybrid-navigation'

import Home from './Home'
import KeyboardAdvoiding from './KeyboardAdvoiding'
import KeyboardChat from './KeyboardChat'

export function registerKeyboardComponent() {
  Navigation.registerComponent('Keyboard', () => Home)
  Navigation.registerComponent('KeyboardAdvoiding', () => KeyboardAdvoiding)
  Navigation.registerComponent('KeyboardChat', () => KeyboardChat)
}

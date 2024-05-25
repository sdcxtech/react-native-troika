import Navigation from 'hybrid-navigation'

import WheelPicker from './WheelPicker'

export function registerWheelPicker() {
  Navigation.registerComponent('WheelPicker', () => WheelPicker)
}

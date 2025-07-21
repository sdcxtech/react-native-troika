import Navigation from 'hybrid-navigation';

import WheelPicker from './WheelPickerDemo';

export function registerWheelPicker() {
    Navigation.registerComponent('WheelPicker', () => WheelPicker);
}

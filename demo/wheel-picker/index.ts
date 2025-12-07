import Navigation from 'hybrid-navigation';

import WheelPicker from './WheelPickerDemo';

export function registerWheelPickerComponent() {
	Navigation.registerComponent('WheelPicker', () => WheelPicker);
}

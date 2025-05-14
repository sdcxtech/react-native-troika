import Navigation from 'hybrid-navigation';

import Home from './Home';
import HoverballScreen from './HoverballScreen';
import ToastScreen from './ToastScreen';
import AlertScreen from './AlertScreen';

export function registerOverlayComponent() {
  Navigation.registerComponent('Overlay', () => Home);
  Navigation.registerComponent('Hoverball', () => HoverballScreen);
  Navigation.registerComponent('Toast', () => ToastScreen);
  Navigation.registerComponent('Alert', () => AlertScreen);
}

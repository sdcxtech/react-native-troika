import Navigation from 'hybrid-navigation';

import Home from './Home';
import KeyboardAdvoiding from './KeyboardAdvoiding';
import KeyboardChat from './KeyboardChat';
import KeyboardChatReanimated from './KeyboardChatReanimated';
import ModalTextInput from './ModalTextInput';

export function registerKeyboardComponent() {
  Navigation.registerComponent('Keyboard', () => Home);
  Navigation.registerComponent('KeyboardAdvoiding', () => KeyboardAdvoiding);
  Navigation.registerComponent('KeyboardChat', () => KeyboardChat);
  Navigation.registerComponent('KeyboardChatReanimated', () => KeyboardChatReanimated);
  Navigation.registerComponent('ModalTextInput', () => ModalTextInput);
}

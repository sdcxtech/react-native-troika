import { NativeModules, requireNativeComponent } from 'react-native';
export const NativeKeyboardInsetsView = requireNativeComponent('KeyboardInsetsView');
const KeyboardInsetsModule = NativeModules.KeyboardInsetsModule;
export function getEdgeInsetsForView(viewTag, callback) {
    KeyboardInsetsModule.getEdgeInsetsForView(viewTag, callback);
}

import { Animated } from 'react-native';
interface KeyboardState {
    height: number;
    shown: boolean;
    transitioning: boolean;
    position: Animated.Value;
}
export declare function useKeyboard(): {
    keyboard: KeyboardState;
    onKeyboard: (state: KeyboardState) => void;
};
export {};

/// <reference types="react" />
import { Animated, ViewProps } from 'react-native';
export interface KeyboardState {
    height: number;
    shown: boolean;
    transitioning: boolean;
    position: Animated.Value;
}
interface KeyboardInsetsViewProps extends Animated.AnimatedProps<ViewProps> {
    extraHeight?: number;
    explicitly?: boolean;
    onKeyboard?: (status: KeyboardState) => void;
}
export declare function KeyboardInsetsView(props: KeyboardInsetsViewProps): JSX.Element;
export {};

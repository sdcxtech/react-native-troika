import { PropsWithChildren } from 'react';
import { Insets, NativeSyntheticEvent } from 'react-native';
export interface KeyboardStatusChangedEventData {
    height: number;
    shown: boolean;
    transitioning: boolean;
}
export interface KeyboardPositionChangedEventData {
    position: number;
}
export type KeyboardStatusChangedEvent = NativeSyntheticEvent<KeyboardStatusChangedEventData>;
export type KeyboardPositionChangedEvent = NativeSyntheticEvent<KeyboardPositionChangedEventData>;
interface NativeKeyboardInsetsViewProps {
    mode?: 'auto' | 'manual';
    extraHeight?: number;
    explicitly?: boolean;
    onStatusChanged?: (event: KeyboardStatusChangedEvent) => void;
    onPositionChanged?: (event: KeyboardPositionChangedEvent) => void;
}
export declare const NativeKeyboardInsetsView: import("react-native").HostComponent<PropsWithChildren<NativeKeyboardInsetsViewProps>>;
export declare function getEdgeInsetsForView(viewTag: number, callback: (insets: Insets) => void): void;
export {};

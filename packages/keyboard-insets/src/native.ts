import { Insets, NativeModule, NativeModules, NativeSyntheticEvent, requireNativeComponent } from 'react-native'

export interface KeyboardStatusChangedEventData {
  height: number
  shown: boolean
  transitioning: boolean
}

export interface KeyboardPositionChangedEventData {
  position: number
}

export type KeyboardStatusChangedEvent = NativeSyntheticEvent<KeyboardStatusChangedEventData>

export type KeyboardPositionChangedEvent = NativeSyntheticEvent<KeyboardPositionChangedEventData>

interface NativeKeyboardInsetsViewProps {
  mode?: 'auto' | 'manual'
  extraHeight?: number
  onStatusChanged?: (event: KeyboardStatusChangedEvent) => void
  onPositionChanged?: (event: KeyboardPositionChangedEvent) => void
}

export const NativeKeyboardInsetsView = requireNativeComponent<NativeKeyboardInsetsViewProps>('KeyboardInsetsView')

interface KeyboardInsetsModuleInterface extends NativeModule {
  getEdgeInsetsForView(viewTag: number, callback: (insets: Insets) => void): void
}

const KeyboardInsetsModule: KeyboardInsetsModuleInterface = NativeModules.KeyboardInsetsModule

export function getEdgeInsetsForView(viewTag: number, callback: (insets: Insets) => void) {
  KeyboardInsetsModule.getEdgeInsetsForView(viewTag, callback)
}

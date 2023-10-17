import { useCallback } from 'react'
import { useSharedValue } from 'react-native-reanimated'
import {
  KeyboardPositionChangedEventData,
  KeyboardStatusChangedEventData,
} from '@sdcx/keyboard-insets'

export function useKeyboard() {
  const position = useSharedValue(0)
  const height = useSharedValue(0)
  const shown = useSharedValue(false)
  const transitioning = useSharedValue(false)

  const onStatusChanged = useCallback(
    (e: KeyboardStatusChangedEventData) => {
      'worklet'
      shown.value = e.shown
      transitioning.value = e.transitioning
      height.value = e.height
    },
    [shown, transitioning, height],
  )

  const onPositionChanged = useCallback(
    (e: KeyboardPositionChangedEventData) => {
      'worklet'
      position.value = e.position
    },
    [position],
  )

  const keyboard = {
    position,
    height,
    shown,
    transitioning,
  }

  return { keyboard, onStatusChanged, onPositionChanged }
}

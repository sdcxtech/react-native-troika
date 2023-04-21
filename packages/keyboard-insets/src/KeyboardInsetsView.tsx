import React, { useCallback, useMemo, useRef } from 'react'
import { Animated, NativeSyntheticEvent, ViewProps } from 'react-native'
import { KeyboardStatus, NativeKeyboardInsetsView } from './native'

export interface KeyboardState {
  height: number
  shown: boolean
  transitioning: boolean
  position: Animated.Value
}

const NativeKeyboardInsetsViewAnimated = Animated.createAnimatedComponent(NativeKeyboardInsetsView)

interface KeyboardInsetsViewProps extends Animated.AnimatedProps<ViewProps> {
  extraHeight?: number
  onKeyboard?: (status: KeyboardState) => void
}

export function KeyboardInsetsView(props: KeyboardInsetsViewProps) {
  const { children, onKeyboard, ...rest } = props

  const position = useRef(new Animated.Value(0)).current

  const onPositionChanged = useMemo(
    () =>
      Animated.event(
        [
          {
            nativeEvent: {
              position,
            },
          },
        ],
        {
          useNativeDriver: true,
        },
      ),
    [position],
  )

  const onStatusChanaged = useCallback(
    (event: NativeSyntheticEvent<KeyboardStatus>) => {
      onKeyboard?.({ ...event.nativeEvent, position })
    },
    [position, onKeyboard],
  )

  if (onKeyboard) {
    return (
      <NativeKeyboardInsetsViewAnimated
        mode="manual"
        onStatusChanged={onStatusChanaged}
        onPositionChanged={onPositionChanged}
        {...rest}>
        {children}
      </NativeKeyboardInsetsViewAnimated>
    )
  }

  return <NativeKeyboardInsetsView {...rest}>{children}</NativeKeyboardInsetsView>
}

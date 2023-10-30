import React from 'react'

import {
  KeyboardPositionChangedEvent,
  KeyboardPositionChangedEventData,
  KeyboardStatusChangedEvent,
  KeyboardStatusChangedEventData,
  NativeKeyboardInsetsView,
} from '@sdcx/keyboard-insets'

import Reanimated, { useEvent, useHandler } from 'react-native-reanimated'

export interface KeyboardState {
  height: number
  shown: boolean
  transitioning: boolean
}

const NativeKeyboardInsetsViewReanimated =
  Reanimated.createAnimatedComponent(NativeKeyboardInsetsView)

interface KeyboardInsetsViewProps extends Reanimated.AnimateProps<any> {
  onPositionChanged: (e: KeyboardPositionChangedEventData) => void
  onStatusChanged: (e: KeyboardStatusChangedEventData) => void
}

export function RekeyboardInsetsView(props: KeyboardInsetsViewProps) {
  const { children, onPositionChanged, onStatusChanged, ...rest } = props

  const handlePositionChanged = useKeyboardPositionChangedHandler(
    {
      onPositionChanged: e => {
        'worklet'
        onPositionChanged?.(e)
      },
    },
    [onPositionChanged],
  )

  const handleStatusChanaged = useKeyboardStatusChangedHandler(
    {
      onStatusChanged: e => {
        'worklet'
        onStatusChanged?.(e)
      },
    },
    [onStatusChanged],
  )

  return (
    <NativeKeyboardInsetsViewReanimated
      mode="manual"
      onStatusChanged={handleStatusChanaged}
      onPositionChanged={handlePositionChanged}
      {...rest}>
      {children}
    </NativeKeyboardInsetsViewReanimated>
  )
}

interface CustomKeyboardPositionChangedEventData extends KeyboardPositionChangedEventData {
  eventName: string
}

function useKeyboardPositionChangedHandler<TContext extends Record<string, unknown>>(
  handlers: {
    onPositionChanged: (e: KeyboardPositionChangedEventData, ctx: TContext) => void
  },
  dependencies?: ReadonlyArray<unknown>,
): (e: KeyboardPositionChangedEvent) => void {
  const { context, doDependenciesDiffer } = useHandler(handlers, dependencies)
  return useEvent<KeyboardPositionChangedEventData>(
    event => {
      'worklet'
      const { onPositionChanged } = handlers
      if (
        onPositionChanged &&
        (event as CustomKeyboardPositionChangedEventData).eventName.endsWith('onPositionChanged')
      ) {
        onPositionChanged(event, context)
      }
    },
    ['onPositionChanged'],
    doDependenciesDiffer,
  )
}

interface CustomKeyboardStatusChangedEventData extends KeyboardStatusChangedEventData {
  eventName: string
}

function useKeyboardStatusChangedHandler<TContext extends Record<string, unknown>>(
  handlers: {
    onStatusChanged: (e: KeyboardStatusChangedEventData, ctx: TContext) => void
  },
  dependencies?: ReadonlyArray<unknown>,
): (e: KeyboardStatusChangedEvent) => void {
  const { context, doDependenciesDiffer } = useHandler(handlers, dependencies)
  return useEvent<KeyboardStatusChangedEventData>(
    event => {
      'worklet'
      const { onStatusChanged } = handlers
      if (
        onStatusChanged &&
        (event as CustomKeyboardStatusChangedEventData).eventName.endsWith('onStatusChanged')
      ) {
        onStatusChanged(event, context)
      }
    },
    ['onStatusChanged'],
    doDependenciesDiffer,
  )
}

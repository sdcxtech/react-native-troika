import React, { useCallback } from 'react'
import { NativeSyntheticEvent, RefreshControlProps, requireNativeComponent } from 'react-native'

export const RefreshStateIdle = 0
export const RefreshStateComing = 1
export const RefreshStateRefreshing = 2
export type RefreshStateIdle = typeof RefreshStateIdle
export type RefreshStateComing = typeof RefreshStateComing
export type RefreshStateRefreshing = typeof RefreshStateRefreshing

export type RefreshState = RefreshStateIdle | RefreshStateComing | RefreshStateRefreshing

interface StateChangedEventData {
  state: RefreshState
}

interface OffsetChangedEventData {
  offset: number
}

interface NativeRefreshHeaderProps {
  onRefresh?: () => void
  onStateChanged?: (event: NativeSyntheticEvent<StateChangedEventData>) => void
  onOffsetChanged?: (event: NativeSyntheticEvent<OffsetChangedEventData>) => void
  refreshing: boolean
}

export interface RefreshHeaderProps extends RefreshControlProps {
  onRefresh?: () => void
  onStateChanged?: (state: RefreshState) => void
  onOffsetChanged?: (offset: number) => void
  refreshing: boolean
}

const NativeRefreshHeader = requireNativeComponent<NativeRefreshHeaderProps>('RefreshHeader')

function RefreshHeader(props: RefreshHeaderProps) {
  const { onStateChanged, onOffsetChanged, ...rest } = props

  const handleStateChanged = useCallback(
    (event: NativeSyntheticEvent<StateChangedEventData>) => {
      if (onStateChanged) {
        onStateChanged(event.nativeEvent.state)
      }
    },
    [onStateChanged],
  )

  const handleOffsetChanged = useCallback(
    (event: NativeSyntheticEvent<OffsetChangedEventData>) => {
      if (onOffsetChanged) {
        onOffsetChanged(event.nativeEvent.offset)
      }
    },
    [onOffsetChanged],
  )

  return (
    <NativeRefreshHeader
      onStateChanged={handleStateChanged}
      onOffsetChanged={handleOffsetChanged}
      {...rest}
    />
  )
}

export default RefreshHeader

import React, { useCallback } from 'react'
import { NativeSyntheticEvent, requireNativeComponent, ViewProps } from 'react-native'

export const RefreshStateIdle = 0
export const RefreshStateComing = 1
export const RefreshStateRefreshing = 2
export type RefreshStateIdle = typeof RefreshStateIdle
export type RefreshStateComing = typeof RefreshStateComing
export type RefreshStateRefreshing = typeof RefreshStateRefreshing

export type RefreshState = RefreshStateIdle | RefreshStateComing | RefreshStateRefreshing

interface StateChangeEventData {
  state: RefreshState
}

interface OffsetChangedEventData {
  offset: number
}

interface NativeRefreshFooterProps {
  onRefresh?: () => void
  onStateChanged?: (event: NativeSyntheticEvent<StateChangeEventData>) => void
  onOffsetChanged?: (event: NativeSyntheticEvent<OffsetChangedEventData>) => void
  refreshing?: boolean
  noMoreData?: boolean
  manual?: boolean
}

export interface RefreshFooterProps extends ViewProps {
  onRefresh?: () => void
  onStateChanged?: (state: RefreshState) => void
  onOffsetChanged?: (offset: number) => void
  refreshing: boolean
  noMoreData?: boolean
  manual?: boolean
}

const NativeRefreshFooter = requireNativeComponent<NativeRefreshFooterProps>('RefreshFooter')

function RefreshFooter(props: RefreshFooterProps) {
  const { onStateChanged, onOffsetChanged, ...rest } = props

  const handleStateChanged = useCallback(
    (event: NativeSyntheticEvent<StateChangeEventData>) => {
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
    <NativeRefreshFooter
      onStateChanged={handleStateChanged}
      onOffsetChanged={handleOffsetChanged}
      {...rest}
    />
  )
}

export default RefreshFooter

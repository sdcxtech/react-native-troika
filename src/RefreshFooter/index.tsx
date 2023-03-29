import React, { useCallback } from 'react'
import { NativeSyntheticEvent, requireNativeComponent, ViewProps } from 'react-native'

export const RefreshStateIdle = 0
export const RefreshStateComing = 1
export const RefreshStateRefreshing = 2
export type RefreshStateIdle = typeof RefreshStateIdle
export type RefreshStatePulling = typeof RefreshStateComing
export type RefreshStateRefreshing = typeof RefreshStateRefreshing

export type RefreshState = RefreshStateIdle | RefreshStatePulling | RefreshStateRefreshing

interface StateChangeEventData {
  state: RefreshState
}

interface NativeRefreshFooterProps {
  onRefresh?: () => void
  onStateChanged?: (event: NativeSyntheticEvent<StateChangeEventData>) => void
  refreshing?: boolean
  enabled?: boolean
  manual?: boolean
}

export interface RefreshFooterProps extends ViewProps {
  onRefresh?: () => void
  onStateChanged?: (state: RefreshState) => void
  refreshing?: boolean
  enabled?: boolean
  manual?: boolean
}

const NativeRefreshFooter = requireNativeComponent<NativeRefreshFooterProps>('RefreshFooter')

function RefreshFooter(props: RefreshFooterProps) {
  const { onStateChanged, ...rest } = props

  const handleStateChanged = useCallback(
    (event: NativeSyntheticEvent<StateChangeEventData>) => {
      if (onStateChanged) {
        onStateChanged(event.nativeEvent.state)
      }
    },
    [onStateChanged],
  )

  return <NativeRefreshFooter onStateChanged={handleStateChanged} {...rest} />
}

export default RefreshFooter

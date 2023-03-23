import React, { useCallback } from 'react'
import { NativeSyntheticEvent, requireNativeComponent } from 'react-native'

export const RefreshStateIdle = 1
export const RefreshStatePulling = 2
export const RefreshStateRefreshing = 3
export type RefreshStateIdle = typeof RefreshStateIdle
export type RefreshStatePulling = typeof RefreshStatePulling
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

interface RefreshFooterProps {
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

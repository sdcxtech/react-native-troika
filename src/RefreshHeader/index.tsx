import React, { useCallback } from 'react'
import { NativeSyntheticEvent, RefreshControlProps, requireNativeComponent } from 'react-native'

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

interface NativeRefreshHeaderProps {
  onRefresh?: () => void
  onStateChanged?: (event: NativeSyntheticEvent<StateChangeEventData>) => void
  refreshing: boolean
}

export interface RefreshHeaderProps extends RefreshControlProps {
  onRefresh?: () => void
  onStateChanged?: (state: RefreshState) => void
  refreshing: boolean
}

const NativeRefreshHeader = requireNativeComponent<NativeRefreshHeaderProps>('RefreshHeader')

function RefreshHeader(props: RefreshHeaderProps) {
  const { onStateChanged, ...rest } = props

  const handleStateChanged = useCallback(
    (event: NativeSyntheticEvent<StateChangeEventData>) => {
      if (onStateChanged) {
        onStateChanged(event.nativeEvent.state)
      }
    },
    [onStateChanged],
  )

  return <NativeRefreshHeader onStateChanged={handleStateChanged} {...rest} />
}

export default RefreshHeader

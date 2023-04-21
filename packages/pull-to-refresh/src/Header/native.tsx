import React, { useEffect, useRef, useState } from 'react'
import { findNodeHandle, requireNativeComponent, UIManager, ViewProps } from 'react-native'
import { PullToRefreshOffsetChangedEvent, PullToRefreshStateChangedEvent } from '../types'

interface NativePullToRefreshHeaderProps extends ViewProps {
  onRefresh?: () => void
  onStateChanged?: (event: PullToRefreshStateChangedEvent) => void
  onOffsetChanged?: (event: PullToRefreshOffsetChangedEvent) => void
  refreshing: boolean
}

const NativePullToRefreshHeader = requireNativeComponent<NativePullToRefreshHeaderProps>('RefreshHeader')

export type NativePullToRefreshHeaderInstance = InstanceType<typeof NativePullToRefreshHeader>

const PullToRefreshHeaderCommands = {
  setNativeRefreshing(componentOrHandle: NativePullToRefreshHeaderInstance, refreshing: boolean) {
    UIManager.dispatchViewManagerCommand(findNodeHandle(componentOrHandle), 'setNativeRefreshing', [refreshing])
  },
}

const PullToRefreshHeader = React.forwardRef<NativePullToRefreshHeaderInstance, NativePullToRefreshHeaderProps>(
  (props, ref) => {
    const [, forceUpdate] = useState(false)
    const innerRef = useRef<NativePullToRefreshHeaderInstance | null>(null)
    const nativeRefreshing = useRef(props.refreshing)

    useEffect(() => {
      nativeRefreshing.current = props.refreshing
    }, [props.refreshing])

    useEffect(() => {
      if (props.refreshing !== nativeRefreshing.current && innerRef?.current) {
        nativeRefreshing.current = props.refreshing
        PullToRefreshHeaderCommands.setNativeRefreshing(innerRef.current, props.refreshing)
      }
    })

    const { onRefresh, ..._props } = props
    const _onRefresh = () => {
      nativeRefreshing.current = true
      onRefresh?.()
      forceUpdate(v => !v)
    }

    return (
      <NativePullToRefreshHeader
        {..._props}
        ref={instance => {
          innerRef.current = instance
          if (typeof ref === 'function') {
            ref(instance)
          } else if (ref !== null) {
            ref.current = instance
          }
        }}
        onRefresh={_onRefresh}
      />
    )
  },
)

export { PullToRefreshHeader }

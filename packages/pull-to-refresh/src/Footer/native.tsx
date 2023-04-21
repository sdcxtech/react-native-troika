import React, { useEffect, useRef, useState } from 'react'
import { findNodeHandle, requireNativeComponent, UIManager, ViewProps } from 'react-native'
import { PullToRefreshOffsetChangedEvent, PullToRefreshStateChangedEvent } from '../types'

interface NativePullToRefreshFooterProps extends ViewProps {
  onRefresh?: () => void
  onStateChanged?: (event: PullToRefreshStateChangedEvent) => void
  onOffsetChanged?: (event: PullToRefreshOffsetChangedEvent) => void
  refreshing: boolean
  noMoreData?: boolean
  manual?: boolean
}

const NativePullToRefreshFooter = requireNativeComponent<NativePullToRefreshFooterProps>('RefreshFooter')

export type NativePullToRefreshFooterInstance = InstanceType<typeof NativePullToRefreshFooter>

const PullToRefreshFooterCommands = {
  setNativeRefreshing(componentOrHandle: NativePullToRefreshFooterInstance, refreshing: boolean) {
    UIManager.dispatchViewManagerCommand(findNodeHandle(componentOrHandle), 'setNativeRefreshing', [refreshing])
  },
}

const PullToRefreshFooter = React.forwardRef<NativePullToRefreshFooterInstance, NativePullToRefreshFooterProps>(
  (props, ref) => {
    const [, forceUpdate] = useState(false)
    const innerRef = useRef<NativePullToRefreshFooterInstance | null>(null)
    const nativeRefreshing = useRef(props.refreshing)

    useEffect(() => {
      nativeRefreshing.current = props.refreshing
    }, [props.refreshing])

    useEffect(() => {
      if (props.refreshing !== nativeRefreshing.current && innerRef?.current) {
        nativeRefreshing.current = props.refreshing
        PullToRefreshFooterCommands.setNativeRefreshing(innerRef.current, props.refreshing)
      }
    })

    const { onRefresh, ..._props } = props
    const _onRefresh = () => {
      nativeRefreshing.current = true
      onRefresh?.()
      forceUpdate(v => !v)
    }

    return (
      <NativePullToRefreshFooter
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

export { PullToRefreshFooter }

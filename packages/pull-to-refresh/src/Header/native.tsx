import React from 'react'
import { requireNativeComponent, ViewProps } from 'react-native'
import { PullToRefreshOffsetChangedEvent, PullToRefreshStateChangedEvent } from '../types'

interface NativePullToRefreshHeaderProps extends ViewProps {
  onRefresh?: () => void
  onStateChanged?: (event: PullToRefreshStateChangedEvent) => void
  onOffsetChanged?: (event: PullToRefreshOffsetChangedEvent) => void
  refreshing: boolean
}

const NativePullToRefreshHeader = requireNativeComponent<NativePullToRefreshHeaderProps>('RefreshHeader')

type NativePullToRefreshHeaderInstance = InstanceType<typeof NativePullToRefreshHeader>

const PullToRefreshHeader = React.forwardRef<NativePullToRefreshHeaderInstance, NativePullToRefreshHeaderProps>(
  (props, ref) => {
    return <NativePullToRefreshHeader {...props} ref={ref} />
  },
)

export { PullToRefreshHeader }

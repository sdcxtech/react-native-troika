import React from 'react'
import { Platform, requireNativeComponent, ViewProps } from 'react-native'
import { PullToRefreshOffsetChangedEvent, PullToRefreshStateChangedEvent } from '../types'

interface NativePullToRefreshHeaderProps extends ViewProps {
  onRefresh?: () => void
  onStateChanged?: (event: PullToRefreshStateChangedEvent) => void
  onOffsetChanged?: (event: PullToRefreshOffsetChangedEvent) => void
  refreshing: boolean
}

const NativePullToRefreshHeader = requireNativeComponent<NativePullToRefreshHeaderProps>(
  Platform.OS === 'ios' ? 'RefreshHeader' : 'SPullRefreshHeader',
)

type NativePullToRefreshHeaderInstance = InstanceType<typeof NativePullToRefreshHeader>

const PullToRefreshHeader = React.forwardRef<NativePullToRefreshHeaderInstance, NativePullToRefreshHeaderProps>(
  (props, ref) => {
    return <NativePullToRefreshHeader {...props} ref={ref} />
  },
)

export { PullToRefreshHeader }

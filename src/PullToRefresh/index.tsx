import React from 'react'
import { requireNativeComponent, ViewProps } from 'react-native'

interface PullToRefreshProps extends ViewProps {}

const NativePullToRefresh = requireNativeComponent<PullToRefreshProps>('PullToRefresh')

function PullToRefresh(props: PullToRefreshProps) {
  return <NativePullToRefresh {...props} />
}

export default PullToRefresh

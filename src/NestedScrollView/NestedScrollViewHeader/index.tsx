import React from 'react'
import { requireNativeComponent, ViewProps } from 'react-native'

export interface NestedScrollViewHeaderProps extends ViewProps {
  fixedHeight?: number
  stickyHeaderBeginIndex?: number
}

const NestedScrollViewHeaderNative =
  requireNativeComponent<NestedScrollViewHeaderProps>('NestedScrollViewHeader')

function NestedScrollViewHeader({ ...props }: NestedScrollViewHeaderProps) {
  return <NestedScrollViewHeaderNative {...props} />
}

export default NestedScrollViewHeader

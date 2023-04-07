import React, { SyntheticEvent } from 'react'
import { requireNativeComponent, ViewProps } from 'react-native'
export type NestedScrollViewHeaderScrollEvent = SyntheticEvent<{ scrollY: number }>

export interface NestedScrollViewHeaderProps extends ViewProps {
  fixedHeight?: number
  stickyHeaderBeginIndex?: number
  onScroll?: (event: NestedScrollViewHeaderScrollEvent) => void
}

const NestedScrollViewHeaderNative =
  requireNativeComponent<NestedScrollViewHeaderProps>('NestedScrollViewHeader')

function NestedScrollViewHeader({ ...props }: NestedScrollViewHeaderProps) {
  return <NestedScrollViewHeaderNative {...props} />
}

export default NestedScrollViewHeader

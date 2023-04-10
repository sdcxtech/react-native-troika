import React from 'react'
import {
  HostComponent,
  NativeScrollPoint,
  NativeSyntheticEvent,
  requireNativeComponent,
  ViewProps,
} from 'react-native'

interface NestedScrollEventData {
  contentOffset: NativeScrollPoint
}

export type NestedScrollEvent = NativeSyntheticEvent<NestedScrollEventData>

export interface NestedScrollViewHeaderProps extends ViewProps {
  fixedHeight?: number
  stickyHeaderBeginIndex?: number
  onScroll?: (event: NestedScrollEvent) => void
}

const NestedScrollViewHeaderNative =
  requireNativeComponent<NestedScrollViewHeaderProps>('NestedScrollViewHeader')

const NestedScrollViewHeader = React.forwardRef<
  HostComponent<NestedScrollViewHeaderProps>,
  NestedScrollViewHeaderProps
>((props, ref) => {
  return <NestedScrollViewHeaderNative ref={ref} {...props} />
})

export default NestedScrollViewHeader

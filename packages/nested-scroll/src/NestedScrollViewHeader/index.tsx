import React from 'react'
import { NativeScrollPoint, NativeSyntheticEvent, requireNativeComponent, StyleSheet, ViewProps } from 'react-native'

interface NestedScrollEventData {
  contentOffset: NativeScrollPoint
}

export type NestedScrollEvent = NativeSyntheticEvent<NestedScrollEventData>

export interface NestedScrollViewHeaderProps extends ViewProps {
  stickyHeight?: number
  stickyHeaderBeginIndex?: number
  onScroll?: (event: NestedScrollEvent) => void
}

const NativeNestedScrollViewHeader = requireNativeComponent<NestedScrollViewHeaderProps>('NestedScrollViewHeader')

type NativeNestedScrollViewHeaderInstance = InstanceType<typeof NativeNestedScrollViewHeader>

const NestedScrollViewHeader = React.forwardRef<NativeNestedScrollViewHeaderInstance, NestedScrollViewHeaderProps>(
  (props, ref) => {
    const { style, ...rest } = props
    return <NativeNestedScrollViewHeader style={[styles.zIndex, style]} {...rest} ref={ref} />
  },
)

export { NestedScrollViewHeader }

const styles = StyleSheet.create({
  zIndex: {
    zIndex: 10,
  },
})

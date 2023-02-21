import React, { PropsWithChildren, useMemo, useRef, useState } from 'react'
import {
  LayoutChangeEvent,
  PixelRatio,
  requireNativeComponent,
  View,
  ViewProps,
} from 'react-native'

const AppBarLayoutAndroid = requireNativeComponent<AppBarLayoutProps>('AppBarLayout')

export interface AppBarLayoutProps extends ViewProps {
  fixedRange?: number
  stickyHeaderBeginIndex?: number
}

function AppBarLayout({
  style,
  children,
  fixedRange,
  stickyHeaderBeginIndex,
  onLayout,
  ...props
}: PropsWithChildren<AppBarLayoutProps>) {
  const [layoutHeight, setLayoutHeight] = useState(0)
  const childMap = useRef<Map<number, number>>(new Map())

  const _children = React.Children.map(children, (child, index) => {
    if (React.isValidElement(child)) {
      const handleChildLayoutEvent = (layoutEvent: LayoutChangeEvent) => {
        childMap.current.set(index, layoutEvent.nativeEvent.layout.y)
      }
      return <View onLayout={handleChildLayoutEvent}>{child}</View>
    }
    return child
  })

  const handleLayoutChange = (layoutEvent: LayoutChangeEvent) => {
    const { height } = layoutEvent.nativeEvent.layout
    setLayoutHeight(height)
    onLayout?.(layoutEvent)
  }

  const _fixedRange = useMemo(() => {
    if (typeof fixedRange === 'number') {
      return fixedRange
    }
    if (typeof stickyHeaderBeginIndex === 'number') {
      const stickyHeaderBeginY = childMap.current.get(stickyHeaderBeginIndex)
      if (typeof stickyHeaderBeginY === 'number') {
        return PixelRatio.getPixelSizeForLayoutSize(layoutHeight - stickyHeaderBeginY)
      }
    }
    return 0
  }, [fixedRange, layoutHeight, stickyHeaderBeginIndex])

  return (
    <AppBarLayoutAndroid
      style={style}
      fixedRange={_fixedRange}
      onLayout={handleLayoutChange}
      {...props}>
      {_children}
    </AppBarLayoutAndroid>
  )
}

export default AppBarLayout

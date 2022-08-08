import React, { PropsWithChildren, useRef, useState } from 'react'
import { LayoutChangeEvent, PixelRatio, requireNativeComponent, View, ViewProps } from 'react-native'

const AppBarLayoutAndroid = requireNativeComponent<AppBarLayoutProps>('AppBarLayout')

interface AppBarLayoutProps extends ViewProps {
  fixedRange?: number
  stickyHeaderBeginIndex?: number
}

function AppBarLayout({ style, children, fixedRange, stickyHeaderBeginIndex }: PropsWithChildren<AppBarLayoutProps>) {
  const [layoutHeight, setLayoutHeight] = useState(0)
  const childMap = useRef<Map<number, number>>(new Map())

  const handleChildLayoutChange = (index: number, layoutEvent: LayoutChangeEvent) => {
    const { y } = layoutEvent.nativeEvent.layout
    childMap.current.set(index, y)
  }

  const handleLayoutChange = (layoutEvent: LayoutChangeEvent) => {
    const { height } = layoutEvent.nativeEvent.layout
    setLayoutHeight(height)
  }

  const getFixedRange = () => {
    if (fixedRange) return fixedRange
    if (stickyHeaderBeginIndex) {
      const stickyHeaderBeginY = childMap.current.get(stickyHeaderBeginIndex)
      if (stickyHeaderBeginY !== undefined) {
        return PixelRatio.getPixelSizeForLayoutSize(layoutHeight - stickyHeaderBeginY)
      }
    }
    return 0
  }

  const _children = React.Children.map(children, (child, index) => {
    if (React.isValidElement(child)) {
      return <View onLayout={event => handleChildLayoutChange(index, event)}>{child}</View>
    }
    return child
  })

  return (
    <AppBarLayoutAndroid style={style} fixedRange={getFixedRange()} onLayout={handleLayoutChange}>
      {_children}
    </AppBarLayoutAndroid>
  )
}

export default AppBarLayout

import React, { PropsWithChildren } from 'react'
import { PixelRatio, requireNativeComponent, ViewProps } from 'react-native'

const AppBarLayoutAndroid = requireNativeComponent<AppBarLayoutProps>('AppBarLayout')

export interface AppBarLayoutProps extends ViewProps {
  fixedHeight?: number
  stickyHeaderBeginIndex?: number
}

function AppBarLayout({
  style,
  children,
  fixedHeight,
  stickyHeaderBeginIndex,
  ...props
}: PropsWithChildren<AppBarLayoutProps>) {
  return (
    <AppBarLayoutAndroid
      style={style}
      fixedHeight={fixedHeight ? PixelRatio.getPixelSizeForLayoutSize(fixedHeight) : 0}
      stickyHeaderBeginIndex={stickyHeaderBeginIndex}
      {...props}>
      {children}
    </AppBarLayoutAndroid>
  )
}

export default AppBarLayout

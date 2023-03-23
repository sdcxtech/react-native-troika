import React, { PropsWithChildren } from 'react'
import { Platform, requireNativeComponent, ViewProps } from 'react-native'

const AppBarLayoutAndroid =
  Platform.OS === 'android'
    ? requireNativeComponent<AppBarLayoutProps>('AppBarLayout')
    : requireNativeComponent('NestedScrollViewHeader')

export interface AppBarLayoutProps extends ViewProps {
  fixedHeight?: number
  stickyHeaderBeginIndex?: number
}

function AppBarLayout({
  style,
  children,
  fixedHeight = 0,
  stickyHeaderBeginIndex,
  ...props
}: PropsWithChildren<AppBarLayoutProps>) {
  return (
    <AppBarLayoutAndroid
      style={style}
      fixedHeight={fixedHeight}
      stickyHeaderBeginIndex={stickyHeaderBeginIndex}
      {...props}>
      {children}
    </AppBarLayoutAndroid>
  )
}

export default AppBarLayout

import React, { PropsWithChildren } from 'react'
import { requireNativeComponent, StyleProp, ViewStyle } from 'react-native'

const AppBarLayoutAndroid = requireNativeComponent<any>('AppBarLayout')

interface AppBarLayoutProps {
  style?: StyleProp<ViewStyle>
  anchorViewId?: number
  fixedRange?: number
}

export const INVALID_VIEW_ID = -1

function AppBarLayout({ style, children, anchorViewId, fixedRange }: PropsWithChildren<AppBarLayoutProps>) {
  return (
    <AppBarLayoutAndroid
      style={style}
      anchorViewId={typeof fixedRange === 'number' && fixedRange > 0 ? INVALID_VIEW_ID : anchorViewId}
      fixedRange={fixedRange}>
      {children}
    </AppBarLayoutAndroid>
  )
}

export default AppBarLayout

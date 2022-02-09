import React, { PropsWithChildren } from 'react'
import { requireNativeComponent, StyleProp, ViewStyle } from 'react-native'

const AppBarLayoutAndroid = requireNativeComponent<any>('AppBarLayout')

interface AppBarLayoutProps {
  style?: StyleProp<ViewStyle>
}

function AppBarLayout({ style, children }: PropsWithChildren<AppBarLayoutProps>) {
  return <AppBarLayoutAndroid style={style}>{children}</AppBarLayoutAndroid>
}

export default AppBarLayout

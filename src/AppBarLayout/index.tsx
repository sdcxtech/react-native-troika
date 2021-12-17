import React, { PropsWithChildren } from 'react'
import { ViewStyle } from 'react-native'
import { StyleProp } from 'react-native'
import { requireNativeComponent } from 'react-native'

const AppBarLayoutAndroid = requireNativeComponent<any>('AppBarLayout')

interface CoordinatorLayoutProps {
  style?: StyleProp<ViewStyle>
}

function CoordinatorLayout({ style, children }: PropsWithChildren<CoordinatorLayoutProps>) {
  return <AppBarLayoutAndroid style={style}>{children}</AppBarLayoutAndroid>
}

export default CoordinatorLayout

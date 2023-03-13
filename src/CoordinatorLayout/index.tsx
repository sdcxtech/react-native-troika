import React, { PropsWithChildren } from 'react'
import { requireNativeComponent, StyleProp, ViewStyle } from 'react-native'

const CoordinatorLayoutAndroid = requireNativeComponent<any>('CoordinatorLayout')

interface CoordinatorLayoutProps {
  style?: StyleProp<ViewStyle>
}

function CoordinatorLayout({ style, children }: PropsWithChildren<CoordinatorLayoutProps>) {
  return <CoordinatorLayoutAndroid style={style}>{children}</CoordinatorLayoutAndroid>
}

export default CoordinatorLayout

import React, { PropsWithChildren } from 'react'
import { Platform, requireNativeComponent, StyleProp, ViewStyle } from 'react-native'

const CoordinatorLayoutAndroid =
  Platform.OS === 'android'
    ? requireNativeComponent<any>('CoordinatorLayout')
    : requireNativeComponent<any>('NestedScrollView')

interface CoordinatorLayoutProps {
  style?: StyleProp<ViewStyle>
}

function CoordinatorLayout({ style, children }: PropsWithChildren<CoordinatorLayoutProps>) {
  return <CoordinatorLayoutAndroid style={style}>{children}</CoordinatorLayoutAndroid>
}

export default CoordinatorLayout

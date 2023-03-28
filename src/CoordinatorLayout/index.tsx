import React, { PropsWithChildren } from 'react'
import { Platform, requireNativeComponent, StyleProp, ViewStyle } from 'react-native'

const CoordinatorLayoutAndroid =
  Platform.OS === 'android'
    ? requireNativeComponent<any>('CoordinatorLayout')
    : requireNativeComponent<any>('NestedScrollView')

interface CoordinatorLayoutProps {
  style?: StyleProp<ViewStyle>
  bounces?: boolean
}

function CoordinatorLayout({ children, ...props }: PropsWithChildren<CoordinatorLayoutProps>) {
  return <CoordinatorLayoutAndroid {...props}>{children}</CoordinatorLayoutAndroid>
}

export default CoordinatorLayout

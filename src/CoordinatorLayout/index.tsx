import React, { PropsWithChildren } from 'react'
import { ViewStyle } from 'react-native'
import { StyleProp } from 'react-native'
import { requireNativeComponent } from 'react-native'

const CoordinatorLayoutAndroid = requireNativeComponent<any>('CoordinatorLayoutAndroid')

interface CoordinatorLayoutProps {
  style?: StyleProp<ViewStyle>
}

function CoordinatorLayout({ style, children }: PropsWithChildren<CoordinatorLayoutProps>) {
  return <CoordinatorLayoutAndroid style={style}>{children}</CoordinatorLayoutAndroid>
}

export default CoordinatorLayout

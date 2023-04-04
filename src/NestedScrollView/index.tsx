import React, { PropsWithChildren } from 'react'
import {
  Platform,
  requireNativeComponent,
  StyleProp,
  StyleSheet,
  View,
  ViewStyle,
} from 'react-native'
export type NestedScrollViewProps = PropsWithChildren<{
  style?: StyleProp<ViewStyle>
  //Android Only
  contentContainerStyle?: StyleProp<ViewStyle>
  //iOS Only
  bounces?:boolean
}>

type NestedScrollViewNativeProps = Omit<NestedScrollViewProps, 'contentContainerStyle'>

const NestedScrollViewNative =
  Platform.OS === 'android'
    ? requireNativeComponent<NestedScrollViewNativeProps>('NestedScrollView')
    : requireNativeComponent<NestedScrollViewNativeProps>('NestedScrollView')

function NestedScrollView({  children ,...props}: NestedScrollViewProps) {
  if (Platform.OS === 'android') {
    return <NestedScrollViewAndroid {...props}>{children}</NestedScrollViewAndroid>
  }
  return <NestedScrollViewNative {...props}>{children}</NestedScrollViewNative>
}

export function NestedScrollViewAndroid({
  style,
  children,
  contentContainerStyle,
}: NestedScrollViewProps) {
  return (
    <NestedScrollViewNative style={style}>
      <View style={[styles.flex1, contentContainerStyle]} removeClippedSubviews={false}>
        {children}
      </View>
    </NestedScrollViewNative>
  )
}

const styles = StyleSheet.create({
  flex1: {
    flex: 1,
  },
})

export default NestedScrollView

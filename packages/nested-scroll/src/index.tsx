import React, { PropsWithChildren } from 'react'
import {
  NativeScrollEvent,
  NativeSyntheticEvent,
  Platform,
  requireNativeComponent,
  StyleProp,
  StyleSheet,
  View,
  ViewStyle,
} from 'react-native'

export type NestedScrollViewProps = PropsWithChildren<{
  style?: StyleProp<ViewStyle>
  onScroll?: (event: NativeSyntheticEvent<Omit<NativeScrollEvent, 'velocity'>>) => void
  //Android Only
  contentContainerStyle?: StyleProp<ViewStyle>
  //iOS Only
  bounces?: boolean
}>

type NestedScrollViewNativeProps = Omit<NestedScrollViewProps, 'contentContainerStyle'>

const NestedScrollViewNative = requireNativeComponent<NestedScrollViewNativeProps>('NestedScrollView')

function NestedScrollView({ children, ...props }: NestedScrollViewProps) {
  if (Platform.OS === 'android') {
    return <NestedScrollViewAndroid {...props}>{children}</NestedScrollViewAndroid>
  }
  return <NestedScrollViewNative {...props}>{children}</NestedScrollViewNative>
}

export function NestedScrollViewAndroid({ children, contentContainerStyle, ...props }: NestedScrollViewProps) {
  return (
    <NestedScrollViewNative {...props}>
      <View style={[styles.flex1, contentContainerStyle]} collapsable={false}>
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

export { NestedScrollView }

export { NestedScrollViewHeader } from './NestedScrollViewHeader'

export type { NestedScrollEvent, NestedScrollViewHeaderProps } from './NestedScrollViewHeader'

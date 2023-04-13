import React from 'react'
import { Platform, requireNativeComponent, StyleSheet, View, ViewProps } from 'react-native'

interface BottomSheetProps extends ViewProps {}

const NativeBottomSheet = requireNativeComponent<BottomSheetProps>('BottomSheet')

type NativeBottomSheetInstance = InstanceType<typeof NativeBottomSheet>

const BottomSheet = React.forwardRef<NativeBottomSheetInstance, BottomSheetProps>((props, ref) => {
  const { style, children, ...rest } = props
  if (Platform.OS === 'android') {
    return (
      <NativeBottomSheet style={[StyleSheet.absoluteFill, style]} {...rest} ref={ref}>
        <View style={StyleSheet.absoluteFill} collapsable={false}>
          {children}
        </View>
      </NativeBottomSheet>
    )
  } else {
    return (
      <NativeBottomSheet style={[StyleSheet.absoluteFill, style]} {...rest} ref={ref}>
        {children}
      </NativeBottomSheet>
    )
  }
})

export default BottomSheet

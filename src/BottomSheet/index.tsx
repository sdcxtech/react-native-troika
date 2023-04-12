import React from 'react'
import { requireNativeComponent, StyleSheet, ViewProps } from 'react-native'

interface BottomSheetProps extends ViewProps {}

const NativeBottomSheet = requireNativeComponent<BottomSheetProps>('BottomSheet')

type NativeBottomSheetInstance = InstanceType<typeof NativeBottomSheet>

const BottomSheet = React.forwardRef<NativeBottomSheetInstance, BottomSheetProps>((props, ref) => {
  const { style, ...rest } = props
  return <NativeBottomSheet style={[StyleSheet.absoluteFill, style]} {...rest} ref={ref} />
})

export default BottomSheet

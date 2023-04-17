import React from 'react'
import { NativeSyntheticEvent, Platform, requireNativeComponent, StyleSheet, View, ViewProps } from 'react-native'
import splitLayoutProps from './splitLayoutProps'

export interface OffsetChangedEventData {
  offset: number
  expandedOffset: number
  collapsedOffset: number
}

export type BottomSheetState = 'collapsed' | 'expanded' | 'hidden'

export interface StateChangedEventData {
  state: BottomSheetState
}

interface NativeBottomSheetProps extends ViewProps {
  onSlide?: (event: NativeSyntheticEvent<OffsetChangedEventData>) => void
  onStateChanged?: (event: NativeSyntheticEvent<StateChangedEventData>) => void
  peekHeight?: number
  state?: BottomSheetState
}

type BottomSheetProps = NativeBottomSheetProps & {
  fitToContents?: boolean
}

const NativeBottomSheet = requireNativeComponent<NativeBottomSheetProps>('BottomSheet')

type NativeBottomSheetInstance = InstanceType<typeof NativeBottomSheet>

const BottomSheet = React.forwardRef<NativeBottomSheetInstance, BottomSheetProps>((props, ref) => {
  const { style, children, peekHeight = 200, state = 'collapsed', fitToContents, ...rest } = props
  const { outer, inner } = splitLayoutProps(StyleSheet.flatten(style))

  if (Platform.OS === 'android') {
    return (
      <NativeBottomSheet
        style={[StyleSheet.absoluteFill, outer]}
        peekHeight={peekHeight}
        state={state}
        {...rest}
        ref={ref}>
        <View style={[fitToContents ? styles.fitToContents : StyleSheet.absoluteFill, inner]} collapsable={false}>
          {children}
        </View>
      </NativeBottomSheet>
    )
  } else {
    return (
      <View style={[StyleSheet.absoluteFill, outer]} pointerEvents="box-none">
        <NativeBottomSheet
          style={[fitToContents ? styles.fitToContents : StyleSheet.absoluteFill, inner]}
          peekHeight={peekHeight}
          state={state}
          {...rest}
          ref={ref}>
          {children}
        </NativeBottomSheet>
      </View>
    )
  }
})

const styles = StyleSheet.create({
  fitToContents: {
    position: 'absolute',
    left: 0,
    right: 0,
    bottom: 0,
  },
})

export default BottomSheet

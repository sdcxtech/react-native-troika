import BottomSheet, {
  BottomSheetState,
  OffsetChangedEventData,
  StateChangedEventData,
} from '@sdcx/bottom-sheet'
import React, { PropsWithChildren, useCallback, useEffect, useRef, useState } from 'react'
import {
  BackHandler,
  Keyboard,
  NativeSyntheticEvent,
  Pressable,
  StyleProp,
  StyleSheet,
  View,
  ViewStyle,
} from 'react-native'

interface BottomModalProps {
  style?: StyleProp<ViewStyle>
  modalContentStyle?: StyleProp<ViewStyle>
  fitToContents?: boolean
  visible: boolean
  onClose?: () => void
}

export function BottomModal(props: PropsWithChildren<BottomModalProps>) {
  const {
    visible = true,
    fitToContents = false,
    onClose,
    style,
    modalContentStyle,
    children,
  } = props
  const [bottomSheetState, setBottomSheetState] = useState<BottomSheetState>('collapsed')
  const progressRef = useRef(0)

  useEffect(() => {
    if (!visible) {
      Keyboard.dismiss()
    }
    // 保证动画
    if (visible) {
      setTimeout(() => {
        setBottomSheetState('expanded')
      }, 0)
    } else {
      setBottomSheetState('collapsed')
    }
  }, [visible])

  useEffect(() => {
    const subscription = BackHandler.addEventListener('hardwareBackPress', () => {
      setBottomSheetState('collapsed')
      return true
    })

    return () => subscription.remove()
  }, [])

  const onOutsidePress = () => {
    Keyboard.dismiss()
    setBottomSheetState('collapsed')
  }

  const onStateChanged = (event: NativeSyntheticEvent<StateChangedEventData>) => {
    const { state } = event.nativeEvent
    setBottomSheetState(state)
    if (state === 'collapsed' && progressRef.current === 1) {
      onClose?.()
    }
  }

  const onSlide = useCallback((event: NativeSyntheticEvent<OffsetChangedEventData>) => {
    const { progress } = event.nativeEvent
    progressRef.current = progress
  }, [])

  return (
    <View style={styles.container}>
      {!fitToContents && <Pressable style={StyleSheet.absoluteFill} onPress={onOutsidePress} />}
      <BottomSheet
        fitToContents={fitToContents}
        peekHeight={0}
        draggable={false}
        state={bottomSheetState}
        onStateChanged={onStateChanged}
        onSlide={onSlide}
        style={style}
        contentContainerStyle={[
          fitToContents ? styles.fitToContents : undefined,
          modalContentStyle,
        ]}>
        {fitToContents && <Pressable style={StyleSheet.absoluteFill} onPress={onOutsidePress} />}
        {children}
      </BottomSheet>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  fitToContents: {
    position: 'relative',
    flex: 1,
    justifyContent: 'flex-end',
  },
})

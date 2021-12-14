import React, { PropsWithChildren, useEffect, useState, useCallback, useRef } from 'react'
import {
  Animated,
  Easing,
  LayoutChangeEvent,
  StyleProp,
  ViewStyle,
  View,
  StyleSheet,
  Modal,
  ModalProps,
  Dimensions,
} from 'react-native'
import OverlayFadingBackground from './OverlayFadingBackground'
import TouchableOverlay from './TouchableOverlay'

interface BottomModalProps {
  visible?: boolean
  contentStyle?: StyleProp<ViewStyle>
  onDismiss?: ModalProps['onDismiss']
  onRequestClose?: ModalProps['onRequestClose']
}

export default function BottomModal({
  children,
  visible,
  contentStyle,
  onDismiss,
  onRequestClose,
}: PropsWithChildren<BottomModalProps>) {
  const [modalVisible, setModalVisible] = useState(visible)

  const animatedValue = useRef<Animated.AnimatedValue>(new Animated.Value(0))
  const animateTo = useCallback((toValue: number, animationEndCallback?: Animated.EndCallback) => {
    Animated.timing(animatedValue.current, {
      toValue,
      duration: 300,
      easing: Easing.bezier(0.2, 0, 0.35, 1),
      useNativeDriver: true,
    }).start(animationEndCallback)
  }, [])

  const hide = useCallback(() => {
    animateTo(0, () => {
      setModalVisible(false)
    })
  }, [animateTo])

  useEffect(() => {
    if (visible) {
      setModalVisible(visible)
    } else {
      hide()
    }
  }, [visible, hide])

  const contentRef = useRef<View>()
  const contentHeight = useRef(Dimensions.get('screen').height)

  const onLayout = useCallback(
    (event: LayoutChangeEvent) => {
      const { height } = event.nativeEvent.layout
      contentHeight.current = height
      animateTo(1)
    },
    [animateTo],
  )

  return (
    <Modal
      visible={modalVisible}
      transparent
      statusBarTranslucent
      animationType="none"
      onDismiss={onDismiss}
      onRequestClose={onRequestClose}>
      <TouchableOverlay onPress={onRequestClose} />
      <OverlayFadingBackground backgroundColor="#00000088" visible={visible} />
      <View style={styles.modal} pointerEvents="box-none">
        <Animated.View
          ref={contentRef}
          onLayout={onLayout}
          style={[
            styles.content,
            contentStyle,
            {
              transform: [
                {
                  translateY: animatedValue.current.interpolate({
                    inputRange: [0, 1],
                    outputRange: [contentHeight.current, 0],
                  }),
                },
              ],
            },
          ]}>
          {children}
        </Animated.View>
      </View>
    </Modal>
  )
}

const styles = StyleSheet.create({
  modal: {
    flex: 1,
    justifyContent: 'flex-end',
    alignItems: 'center',
  },
  content: {
    backgroundColor: '#FFFFFF',
    borderTopRightRadius: 16,
    borderTopLeftRadius: 16,
    width: '100%',
  },
})

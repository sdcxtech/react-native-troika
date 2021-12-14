import React, { useRef, useMemo, useCallback, useEffect } from 'react'
import { ViewStyle, StyleSheet, Animated } from 'react-native'

interface OverlayFadingBackgroundProps {
  backgroundColor: ViewStyle['backgroundColor']
  visible?: boolean
}

export default function OverlayFadingBackground({ backgroundColor, visible }: OverlayFadingBackgroundProps) {
  const fadeAnimation = useRef(new Animated.Value(0)).current

  const animateFading = useCallback(
    toValue => {
      Animated.timing(fadeAnimation, {
        toValue,
        duration: 400,
        useNativeDriver: true,
      }).start()
    },
    [fadeAnimation],
  )

  useEffect(() => {
    animateFading(visible ? 1 : 0)
  }, [visible, animateFading])

  const style = useMemo(() => {
    return {
      opacity: fadeAnimation,
      backgroundColor: backgroundColor,
    }
  }, [backgroundColor, fadeAnimation])

  return <Animated.View style={[StyleSheet.absoluteFillObject, style]} pointerEvents="box-none" />
}

import React from 'react'
import { TouchableOpacity, ViewProps, StyleProp, Text, StyleSheet, TextStyle } from 'react-native'

interface TextButtonProps {
  title: string
  onPress?: () => void
  hitSlop?: ViewProps['hitSlop']
  style?: StyleProp<TextStyle>
  textStyle?: StyleProp<TextStyle>
  disabled?: boolean
}

export default function TextButton({ onPress, hitSlop, style, title, textStyle, disabled }: TextButtonProps) {
  return (
    <TouchableOpacity
      onPress={onPress}
      style={[styles.button, style]}
      activeOpacity={0.5}
      disabled={disabled}
      hitSlop={{ left: 8, right: 8, top: 8, bottom: 8, ...hitSlop }}>
      <Text style={[styles.text, textStyle]}>{title}</Text>
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  button: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    color: '#333333',
    fontSize: 15,
  },
})

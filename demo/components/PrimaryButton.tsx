import React, { useState } from 'react'
import { Pressable, StyleSheet, Text, TextStyle, ViewStyle } from 'react-native'

interface PrimayButtonProps {
  style?: ViewStyle
  textStyle?: TextStyle
  onPress?: () => void
  text: string
}

export default function PrimaryButton(props: PrimayButtonProps) {
  const { text, onPress, style, textStyle } = props
  const [pressed, setPressed] = useState(false)
  const onPressIn = () => {
    setPressed(true)
  }

  const onPressOut = () => {
    setPressed(false)
  }
  return (
    <Pressable
      style={[styles.button, pressed ? styles.pressIn : styles.pressOut, style]}
      onPress={onPress}
      onPressIn={onPressIn}
      onPressOut={onPressOut}>
      <Text style={[styles.title, textStyle]}>{text}</Text>
    </Pressable>
  )
}

const styles = StyleSheet.create({
  button: {
    height: 44,
    backgroundColor: '#2A3080',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 10,
  },
  pressIn: {
    backgroundColor: '#141A66',
  },
  pressOut: {
    backgroundColor: '#2A3080',
  },
  title: {
    lineHeight: 32,
    color: '#FFFFFF',
    fontSize: 14,
  },
})

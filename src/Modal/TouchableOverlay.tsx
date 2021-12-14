import React from 'react'
import { StyleSheet, View, TouchableWithoutFeedback } from 'react-native'

interface TouchableOverlayProps {
  onPress?: () => void
}

export default function TouchableOverlay({ onPress }: TouchableOverlayProps) {
  return (
    <TouchableWithoutFeedback onPress={onPress}>
      <View style={StyleSheet.absoluteFillObject} collapsable={false} />
    </TouchableWithoutFeedback>
  )
}

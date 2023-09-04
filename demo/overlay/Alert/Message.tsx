import React from 'react'
import { StyleSheet, Text, View } from 'react-native'

interface MessageProps {
  message: string
}

export default function Message({ message }: MessageProps) {
  return (
    <View style={styles.box}>
      <Text style={styles.message}>{message}</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  box: {
    paddingHorizontal: 12,
  },
  message: {
    color: '#1D2023',
    fontSize: 15,
    lineHeight: 25,
    marginVertical: 6,
  },
})

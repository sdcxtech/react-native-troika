import React from 'react'
import { ScrollView, Text, View } from 'react-native'

export function DemoScrollView() {
  return (
    <ScrollView style={{ flex: 1 }} scrollEnabled nestedScrollEnabled>
      {['yellow', 'aqua', 'pink'].map((color, index) => (
        <View key={color} style={{ height: 200 * (index + 1), backgroundColor: color }}>
          <Text style={{ color: 'white' }}>{color}</Text>
        </View>
      ))}
    </ScrollView>
  )
}

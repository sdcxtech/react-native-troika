import React from 'react'
import { FlatList, StyleSheet, Text, View } from 'react-native'

const DATA = Array(40)
  .fill(Math.random())
  .map((item, index) => ({
    id: item + index,
    title: `index: ${index} `,
  }))
export function DemoFlatList() {
  const renderItem = ({ item }: { item: { title: string } }) => <Item title={item.title} />
  return (
    <FlatList
      style={{ flex: 1 }}
      data={DATA}
      renderItem={renderItem}
      keyExtractor={item => item.id}
      nestedScrollEnabled
    />
  )
}
const Item = ({ title }: { title: string }) => (
  <View style={styles.item}>
    <Text style={styles.title}>{title}</Text>
  </View>
)

const styles = StyleSheet.create({
  item: {
    backgroundColor: '#f9c2ff',
    padding: 20,
    marginVertical: 8,
    marginHorizontal: 16,
  },
  title: {
    fontSize: 32,
  },
})

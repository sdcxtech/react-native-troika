import React, { useState } from 'react'
import { FlatList, StyleSheet, Text, View } from 'react-native'

const FLATLIST_DATA = Array(40)
  .fill(Math.random() + '')
  .map((item, index) => ({
    id: item + index,
    title: `index: ${index} `,
  }))

const generateFlatlistItem = (index: number, extra: string) => ({
  id: Math.random() + '' + index,
  title: `${extra} index ${index}`,
})

export function useDemoFlatlistData() {
  const [flatlistData, setFlatlistData] = useState(FLATLIST_DATA)
  return {
    flatlistData,
    addFlatlistRefreshItem: () => setFlatlistData(data => [generateFlatlistItem(data.length, 'refresh'), ...data]),
    addFlatlistLoadMoreItem: () =>
      setFlatlistData([...flatlistData, generateFlatlistItem(flatlistData.length, 'load more')]),
  }
}

export function FlatListPage({ data = FLATLIST_DATA }: { data?: { id: string; title: string }[] }) {
  const renderItem = ({ item }: { item: { title: string } }) => <Item title={item.title} />
  return (
    <FlatList
      style={{ flex: 1 }}
      contentContainerStyle={{ flexGrow: 1 }}
      data={data}
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

import React, { useState } from 'react'
import { FlatList, StyleSheet, Text, TouchableWithoutFeedback, View } from 'react-native'

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
    addFlatlistRefreshItem: () =>
      setFlatlistData(data => [generateFlatlistItem(data.length, 'refresh'), ...data]),
    addFlatlistLoadMoreItem: () =>
      setFlatlistData([...flatlistData, generateFlatlistItem(flatlistData.length, 'load more')]),
  }
}

export function FlatListPage({ data = FLATLIST_DATA }: { data?: { id: string; title: string }[] }) {
  const renderItem = ({ item }: { item: { title: string } }) => <Item title={item.title} />

  const [refreshing, setRefreshing] = useState(false)

  const onRefresh = () => {
    setRefreshing(true)
    console.log('onRefresh')
    setTimeout(() => {
      setRefreshing(false)
    }, 2000)
  }

  const [loadingMore, setLoadingMore] = useState(false)

  const onLoadMore = () => {
    setLoadingMore(true)
    console.log('onLoadMore')
    setTimeout(() => {
      setLoadingMore(false)
    }, 2000)
  }

  return (
    <FlatList
      onLayout={e => console.log('flatlist', e.nativeEvent.layout.height)}
      contentContainerStyle={{ flexGrow: 1 }}
      data={data}
      renderItem={renderItem}
      keyExtractor={item => item.id}
      nestedScrollEnabled
    />
  )
}
const Item = ({ title }: { title: string }) => {
  const [clickCount, setClickCount] = useState(0)
  return (
    <TouchableWithoutFeedback onPress={() => setClickCount(v => v + 1)}>
      <View style={styles.item}>
        <Text style={styles.title}>
          {title} {clickCount}
        </Text>
      </View>
    </TouchableWithoutFeedback>
  )
}

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

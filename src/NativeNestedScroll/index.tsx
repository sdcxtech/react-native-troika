import { withNavigationItem } from 'hybrid-navigation'
import React from 'react'
import { FlatList, Image, ListRenderItem, StyleSheet, Text, TouchableOpacity, View } from 'react-native'
import CoordinatorLayout from '../CoordinatorLayout'
import AppBarLayout from '../AppBarLayout'

function NativeNestedScroll() {
  const renderListItem: ListRenderItem<string> = ({ item }) => {
    return <ListItem title={item} />
  }

  const data = Array(100)
    .fill('')
    .map((_, index) => index + '')

  return (
    <View style={styles.container}>
      <CoordinatorLayout style={styles.coordinator}>
        <AppBarLayout style={styles.appbar}>
          <View style={styles.content}>
            <Text style={styles.text}>我是大魔王</Text>
          </View>
        </AppBarLayout>
        <FlatList nestedScrollEnabled data={data} keyExtractor={item => item} renderItem={renderListItem} />
      </CoordinatorLayout>
    </View>
  )
}

interface ListItemProps {
  title: string
  onPress?: () => void
}

function ListItem({ title, onPress }: ListItemProps) {
  return (
    <TouchableOpacity style={styles.item} onPress={onPress}>
      <Text style={styles.itemText}>{title}</Text>
      <Image source={require('../assets/indicator.png')} />
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FF0000',
  },
  coordinator: {
    flex: 1,
    backgroundColor: '#00FF00',
  },
  appbar: {
    height: 100,
  },
  content: {
    height: 100,
    backgroundColor: '#0000FF',
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    fontSize: 18,
    color: '#FFFFFF',
  },
  item: {
    height: 60,
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
    borderBottomWidth: 1,
    borderBottomColor: '#EEEEEE',
    paddingLeft: 16,
    paddingRight: 16,
  },
  itemText: {
    color: '#222222',
    fontSize: 17,
  },
})

export default withNavigationItem({
  titleItem: {
    title: '原生嵌套滑动',
  },
})(NativeNestedScroll)

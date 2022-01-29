import { withNavigationItem } from 'hybrid-navigation'
import React from 'react'
import { FlatList, Image, ListRenderItem, ScrollView, StyleSheet, Text, TouchableOpacity, View } from 'react-native'
import CoordinatorLayout from '../CoordinatorLayout'
import AppBarLayout from '../AppBarLayout'
import PagerView from 'react-native-pager-view'
import WebView from 'react-native-webview'

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
        <AppBarLayout>
          <View style={styles.content}>
            <Text style={styles.text}>我是大魔王</Text>
          </View>
        </AppBarLayout>
        <PagerView style={styles.pager}>
          <FlatList
            nestedScrollEnabled
            data={data}
            style={styles.list}
            keyExtractor={item => item}
            renderItem={renderListItem}
          />
          <ScrollView nestedScrollEnabled style={{ flex: 1 }}>
            <View key={'1'} style={{ height: 200, backgroundColor: 'red' }} />
            <View key={'2'} style={{ height: 300, backgroundColor: 'greed' }} />
            <View key={'3'} style={{ height: 400, backgroundColor: 'gray' }} />
          </ScrollView>
          <WebView
            contentContainerStyle={{ flexGrow: 1 }}
            style={{ flex: 1 }}
            originWhitelist={['*']}
            source={{ uri: 'https://www.npmjs.com/package/react' }}
            cacheEnabled={false}
          />
        </PagerView>
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
  pager: {
    height: '100%',
  },
  list: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  web: {
    flex: 1,
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

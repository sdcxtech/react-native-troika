import { withNavigationItem } from 'hybrid-navigation'
import React from 'react'
import { Image, StyleSheet, Text, View } from 'react-native'
import NestedScrollView from '../NestedScrollView'
import NestedScrollViewHeader from '../NestedScrollView/NestedScrollViewHeader'
import { FlatListPage } from '../components/FlatListPage'

export function NestedScrollFlatList() {
  return (
    <NestedScrollView
      style={styles.coordinator}
      bounces
      onScroll={event => console.log(event.nativeEvent)}>
      <NestedScrollViewHeader fixedHeight={60}>
        <Image
          source={require('../components/assets/cover.webp')}
          style={styles.image}
          resizeMode="cover"
        />
        <View style={[styles.text]}>
          <Text>anchor</Text>
        </View>
      </NestedScrollViewHeader>
      <FlatListPage />
    </NestedScrollView>
  )
}

const styles = StyleSheet.create({
  coordinator: {
    height: '100%',
    backgroundColor: '#fff',
  },
  content: {
    backgroundColor: '#0000FF',
    justifyContent: 'center',
    alignItems: 'center',
  },
  image: {
    height: 160,
    width: '100%',
  },
  text: {
    height: 60,
    justifyContent: 'center',
    fontSize: 18,
    color: '#FFFFFF',
  },
})

export default withNavigationItem({
  titleItem: {
    title: 'NestedScroll + FlatList',
  },
})(NestedScrollFlatList)

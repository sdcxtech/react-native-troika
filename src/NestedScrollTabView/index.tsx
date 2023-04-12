import { withNavigationItem } from 'hybrid-navigation'
import React from 'react'
import { Image, StyleSheet } from 'react-native'
import NestedScrollView from '../NestedScrollView'
import NestedScrollViewHeader from '../NestedScrollView/NestedScrollViewHeader'
import TabView from '../components/tabview'

export function NestedScrollTabView() {
  return (
    <NestedScrollView style={styles.container}>
      <NestedScrollViewHeader>
        <Image source={require('assets/cover.webp')} style={styles.image} resizeMode="cover" />
      </NestedScrollViewHeader>
      <TabView />
    </NestedScrollView>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // backgroundColor: '#fff',
  },
  image: {
    height: 160,
    width: '100%',
  },
})

export default withNavigationItem({
  titleItem: {
    title: 'NestedScroll + TabView',
  },
})(NestedScrollTabView)

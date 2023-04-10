import { withNavigationItem } from 'hybrid-navigation'
import React from 'react'
import { Animated, Image, StyleSheet } from 'react-native'
import NestedScrollView from '../NestedScrollView'
import NestedScrollViewHeader from '../NestedScrollView/NestedScrollViewHeader'
import PagerView from 'react-native-pager-view'
import { FlatListPage } from '../components/FlatListPage'
import { ScrollViewPage } from '../components/ScrollViewPage'
import { WebViewPage } from '../components/WebViewPage'
import TabBar from '../components/TabBar'
import usePagerView from '../components/usePagerView'
import Contacts from '../components/contacts/Contacts'

const AnimatedPagerView = Animated.createAnimatedComponent<typeof PagerView>(PagerView)

const pages = ['FlatList', 'FlashList', 'ScrollView', 'WebView']

export function NestedScrollPagerViewStickyHeader() {
  const {
    pagerRef,
    setPage,
    page,
    position,
    offset,
    isIdle,
    onPageScroll,
    onPageSelected,
    onPageScrollStateChanged,
  } = usePagerView()

  return (
    <NestedScrollView style={styles.coordinator}>
      <NestedScrollViewHeader stickyHeaderBeginIndex={1}>
        <Image
          source={require('../components/assets/cover.webp')}
          style={styles.image}
          resizeMode="cover"
        />
        <TabBar
          tabs={pages}
          onTabPress={setPage}
          position={position}
          offset={offset}
          page={page}
          isIdle={isIdle}
        />
      </NestedScrollViewHeader>
      <AnimatedPagerView
        ref={pagerRef}
        style={styles.pager}
        overdrag={true}
        overScrollMode="always"
        onPageScroll={onPageScroll}
        onPageSelected={onPageSelected}
        onPageScrollStateChanged={onPageScrollStateChanged}>
        <FlatListPage />
        <Contacts />
        <ScrollViewPage />
        <WebViewPage url="https://wangdoc.com" />
      </AnimatedPagerView>
    </NestedScrollView>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FF0000',
  },
  coordinator: {
    flex: 1,
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
    paddingVertical: 20,
    fontSize: 18,
    color: '#FFFFFF',
  },
  pager: {
    height: '100%',
  },
})

export default withNavigationItem({
  titleItem: {
    title: 'NestedScroll + PagerView + StickyHeader',
  },
})(NestedScrollPagerViewStickyHeader)

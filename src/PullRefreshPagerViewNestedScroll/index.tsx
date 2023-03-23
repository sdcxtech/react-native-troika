import { withNavigationItem } from 'hybrid-navigation'
import React from 'react'
import { Animated, Image, StyleSheet, View } from 'react-native'
import CoordinatorLayout from '../CoordinatorLayout'
import AppBarLayout from '../AppBarLayout'
import PagerView from 'react-native-pager-view'
import TabBar from '../components/TabBar'
import usePagerView from '../components/usePagerView'
import PullRefreshFlatList from '../components/PullRefreshFlatList'
import PullRefreshScrollView from '../components/PullRefreshScrollView'
import PullRefreshWebView from '../components/PullRefreshWebView'
import { FlatListPage } from '../components/FlatListPage'

const AnimatedPagerView = Animated.createAnimatedComponent<typeof PagerView>(PagerView)

const pages = ['FlatList', 'ScrollView', 'WebView']

export function PullRefreshPagerViewNestedScroll() {
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
    <CoordinatorLayout style={styles.coordinator}>
      <AppBarLayout stickyHeaderBeginIndex={1}>
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
      </AppBarLayout>
      <AnimatedPagerView
        ref={pagerRef}
        style={styles.pager}
        overdrag={true}
        overScrollMode="always"
        onPageScroll={onPageScroll}
        onPageSelected={onPageSelected}
        onPageScrollStateChanged={onPageScrollStateChanged}>
        <View removeClippedSubviews={false}>
          <FlatListPage />
        </View>
        <PullRefreshFlatList />
        <PullRefreshScrollView />
        <PullRefreshWebView />
      </AnimatedPagerView>
    </CoordinatorLayout>
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
    title: 'NestedScroll + PagerView + PullRefresh',
  },
})(PullRefreshPagerViewNestedScroll)

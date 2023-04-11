import { withNavigationItem } from 'hybrid-navigation'
import React from 'react'
import { Animated, StyleSheet, View } from 'react-native'
import PagerView from 'react-native-pager-view'
import TabBar from '../components/TabBar'
import usePagerView from '../components/usePagerView'
import PullRefreshScrollView from '../components/PullRefreshScrollView'
import PullRefreshWebView from '../components/PullRefreshWebView'
import { Twitter, TwitterFlatList } from '../components/twitter'

const AnimatedPagerView = Animated.createAnimatedComponent<typeof PagerView>(PagerView)

const pages = ['FlatList', 'FlashList', 'ScrollView', 'WebView']

export function PullRefreshPagerView() {
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
    <View style={styles.container}>
      <TabBar
        tabs={pages}
        onTabPress={setPage}
        position={position}
        offset={offset}
        page={page}
        isIdle={isIdle}
      />
      <AnimatedPagerView
        ref={pagerRef}
        style={styles.pager}
        overdrag={true}
        overScrollMode="always"
        onPageScroll={onPageScroll}
        onPageSelected={onPageSelected}
        onPageScrollStateChanged={onPageScrollStateChanged}>
        <TwitterFlatList />
        <Twitter />
        <PullRefreshScrollView />
        <PullRefreshWebView />
      </AnimatedPagerView>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  pager: {
    flex: 1,
  },
})

export default withNavigationItem({
  titleItem: {
    title: 'PagerView + PullRefresh',
  },
})(PullRefreshPagerView)

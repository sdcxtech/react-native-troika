import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef, useState } from 'react'
import { Animated, Image, StyleSheet } from 'react-native'
import NestedScrollView from '../NestedScrollView'
import AppBarLayout from '../AppBarLayout'
import PagerView from 'react-native-pager-view'
import { ScrollViewPage } from '../components/ScrollViewPage'
import { WebViewPage } from '../components/WebViewPage'
import TabBar from '../components/TabBar'
import usePagerView from '../components/usePagerView'
import { FlatListPage, useDemoFlatlistData } from '../components/FlatListPage'
import PullToRefresh from '../PullToRefresh'

const AnimatedPagerView = Animated.createAnimatedComponent<typeof PagerView>(PagerView)

const pages = ['FlatList', 'ScrollView', 'WebView']

export function PullRefreshNestedScrollPagerView() {
  const [refreshing, setRefreshing] = useState(false)
  const [loadingMore, setLoadingMore] = useState(false)
  const { addFlatlistRefreshItem, addFlatlistLoadMoreItem } = useDemoFlatlistData()
  const pendingAction = useRef<ReturnType<typeof setTimeout> | null>(null)

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

  const clearPendingAction = () => {
    if (pendingAction.current) {
      clearTimeout(pendingAction.current)
    }
  }

  const beginRefresh = async () => {
    setRefreshing(true)
    pendingAction.current = setTimeout(() => {
      addFlatlistRefreshItem()
      endRefresh()
    }, 1500)
  }

  const endRefresh = () => {
    clearPendingAction()
    setRefreshing(false)
  }

  const loadMore = () => {
    setLoadingMore(true)
    pendingAction.current = setTimeout(() => {
      addFlatlistLoadMoreItem()
      endLoadMore()
    }, 1500)
  }

  const endLoadMore = () => {
    clearPendingAction()
    setLoadingMore(false)
  }

  return (
    <PullToRefresh
      style={{ flex: 1 }}
      refreshing={refreshing}
      loadingMore={loadingMore}
      onRefresh={beginRefresh}
      onLoadMore={loadMore}>
      <NestedScrollView style={styles.coordinator}>
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
          <FlatListPage />
          <ScrollViewPage />
          <WebViewPage url="https://wangdoc.com" />
        </AnimatedPagerView>
      </NestedScrollView>
    </PullToRefresh>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FF0000',
  },
  pager: {
    height: '100%',
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
})

export default withNavigationItem({
  titleItem: {
    title: 'PullRefresh + NestedScroll + PagerView',
  },
})(PullRefreshNestedScrollPagerView)

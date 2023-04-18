import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef, useState } from 'react'
import { Animated, Image, StyleSheet } from 'react-native'
import { NestedScrollView, NestedScrollViewHeader } from '@sdcx/nested-scroll'
import PagerView from 'react-native-pager-view'
import { ScrollViewPage } from '../components/ScrollViewPage'
import { WebViewPage } from '../components/WebViewPage'
import TabBar from '../components/TabBar'
import usePagerView from '../components/usePagerView'
import { useDemoFlatlistData } from '../components/FlatListPage'
import { PullToRefresh } from '@sdcx/pull-to-refresh'
import Contacts from '../components/contacts/Contacts'
import ContactsSectionList from '../components/contacts/ContactsSectionList'

const AnimatedPagerView = Animated.createAnimatedComponent<typeof PagerView>(PagerView)

const pages = ['SectionList', 'FlashList', 'ScrollView', 'WebView']

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
        <NestedScrollViewHeader stickyHeaderBeginIndex={1}>
          <Image source={require('assets/cover.webp')} style={styles.image} resizeMode="cover" />
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
          <ContactsSectionList />
          <Contacts />
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

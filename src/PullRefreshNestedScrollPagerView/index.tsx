import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef, useState } from 'react'
import { Animated, Image, StyleSheet, Text, View } from 'react-native'
import CoordinatorLayout from '../CoordinatorLayout'
import AppBarLayout from '../AppBarLayout'
import PagerView from 'react-native-pager-view'
import { ScrollViewPage } from '../components/ScrollViewPage'
import { WebViewPage } from '../components/WebViewPage'
import TabBar from '../components/TabBar'
import usePagerView from '../components/usePagerView'
import { FlatListPage, useDemoFlatlistData } from '../components/FlatListPage'
import PullRefreshLayout, { PullEvent } from '../PullRefreshLayout'

const AnimatedPagerView = Animated.createAnimatedComponent<typeof PagerView>(PagerView)

const pages = ['FlatList', 'ScrollView', 'WebView']

export function PullRefreshNestedScrollPagerView() {
  const [refreshing, setRefreshing] = useState(false)
  const [statusText, setStatusText] = useState('下拉刷新')
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
    setStatusText('正在刷新...')
    pendingAction.current = setTimeout(() => {
      addFlatlistRefreshItem()
      endRefresh()
    }, 1500)
  }

  const endRefresh = () => {
    clearPendingAction()
    setRefreshing(false)
    setStatusText('下拉刷新')
  }

  const loadMore = () => {
    setLoadingMore(true)
    setStatusText('load more')
    pendingAction.current = setTimeout(() => {
      addFlatlistLoadMoreItem()
      endLoadMore()
    }, 1500)
  }

  const endLoadMore = () => {
    clearPendingAction()
    setLoadingMore(false)
  }

  const handlePullEvent = (event: PullEvent) => {
    const { currentRefreshViewOffset, totalRefreshViewOffset } = event.nativeEvent
    if (refreshing) {
      setStatusText('正在刷新...')
    } else if (currentRefreshViewOffset >= totalRefreshViewOffset) {
      setStatusText('释放刷新')
    } else {
      setStatusText('下拉刷新')
    }
  }

  const renderRefreshView = () => {
    return (
      <View style={{ overflow: 'hidden' }}>
        <Text
          style={{
            color: 'white',
            paddingTop: 40,
            paddingBottom: 40,
            backgroundColor: refreshing ? 'red' : 'aqua',
          }}>
          {statusText}
        </Text>
      </View>
    )
  }
  return (
    <PullRefreshLayout
      style={{ flex: 1 }}
      refreshViewOverPullLocation="bottom"
      enableRefreshAction={true}
      refreshing={refreshing}
      loadingMore={loadingMore}
      onRefresh={beginRefresh}
      onRefreshStop={endRefresh}
      onRefreshPull={handlePullEvent}
      enableLoadMoreAction={true}
      RefreshView={renderRefreshView()}
      onLoadMore={loadMore}
      onLoadMoreStop={endLoadMore}>
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
          <FlatListPage />
          <ScrollViewPage />
          <WebViewPage url="https://wangdoc.com" />
        </AnimatedPagerView>
      </CoordinatorLayout>
    </PullRefreshLayout>
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

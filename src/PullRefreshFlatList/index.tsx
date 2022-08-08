import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef, useState } from 'react'
import { View, Text } from 'react-native'
import { FlatListPage, useDemoFlatlistData } from '../components/FlatListPage'
import PullRefreshLayout, { PullEvent } from '../PullRefreshLayout'

function PullRefreshFlatList() {
  const [refreshing, setRefreshing] = useState(false)
  const [statusText, setStatusText] = useState('下拉刷新')
  const [loadingMore, setLoadingMore] = useState(false)
  const { flatlistData, addFlatlistRefreshItem, addFlatlistLoadMoreItem } = useDemoFlatlistData()
  const pendingAction = useRef<ReturnType<typeof setTimeout> | null>(null)

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
      style={{ height: '100%', overflow: 'hidden' }}
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
      <FlatListPage data={flatlistData} />
    </PullRefreshLayout>
  )
}

export default withNavigationItem({
  titleItem: {
    title: 'PullRefresh + FlatList',
  },
})(PullRefreshFlatList)

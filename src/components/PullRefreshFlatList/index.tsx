import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef, useState } from 'react'
import PullToRefresh from '../../PullToRefresh'
import { FlatListPage, useDemoFlatlistData } from '../FlatListPage'

function PullRefreshFlatList() {
  const [refreshing, setRefreshing] = useState(false)

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
      style={{ height: '100%', overflow: 'hidden' }}
      refreshing={refreshing}
      loadingMore={loadingMore}
      onRefresh={beginRefresh}
      onLoadMore={loadMore}>
      <FlatListPage data={flatlistData} />
    </PullToRefresh>
  )
}

export default withNavigationItem({
  titleItem: {
    title: 'PullRefresh + FlatList',
  },
})(PullRefreshFlatList)

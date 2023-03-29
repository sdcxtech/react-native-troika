import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef, useState } from 'react'
import { ScrollViewPage } from '../ScrollViewPage'
import PullToRefresh from '../../PullToRefresh'

function PullRefreshScrollView() {
  const [refreshing, setRefreshing] = useState(false)

  const pendingAction = useRef<ReturnType<typeof setTimeout> | null>(null)

  const clearPendingAction = () => {
    if (pendingAction.current) {
      clearTimeout(pendingAction.current)
    }
  }

  const beginRefresh = async () => {
    setRefreshing(true)

    pendingAction.current = setTimeout(() => {
      endRefresh()
    }, 1500)
  }

  const endRefresh = () => {
    clearPendingAction()
    setRefreshing(false)
  }

  return (
    <PullToRefresh
      style={{ height: '100%', overflow: 'hidden' }}
      refreshing={refreshing}
      onRefresh={beginRefresh}>
      <ScrollViewPage />
    </PullToRefresh>
  )
}

export default withNavigationItem({
  titleItem: {
    title: 'PullRefresh + ScrollView',
  },
})(PullRefreshScrollView)

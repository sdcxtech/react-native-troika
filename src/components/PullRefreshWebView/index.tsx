import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef, useState } from 'react'
import { WebViewPage } from '../WebViewPage'
import PullToRefresh from '../../PullToRefresh'

function PullRefreshWebView() {
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
      <WebViewPage url="https://wangdoc.com" />
    </PullToRefresh>
  )
}

export default withNavigationItem({
  titleItem: {
    title: 'PullRefresh + WebView',
  },
})(PullRefreshWebView)

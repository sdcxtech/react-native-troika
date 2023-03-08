import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef, useState } from 'react'
import { WebViewPage } from '../WebViewPage'
import PullRefreshLayout from '../../PullRefreshLayout'

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
    <PullRefreshLayout
      style={{ height: '100%', overflow: 'hidden' }}
      refreshViewOverPullLocation="bottom"
      refreshing={refreshing}
      onRefresh={beginRefresh}
      onRefreshStop={() => {
        setRefreshing(false)
      }}>
      <WebViewPage url="https://wangdoc.com" />
    </PullRefreshLayout>
  )
}

export default withNavigationItem({
  titleItem: {
    title: 'PullRefresh + WebView',
  },
})(PullRefreshWebView)

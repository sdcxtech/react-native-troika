import React, { useRef, useState } from 'react'
import { View, Text } from 'react-native'
import { NativeNestedScroll } from '../NativeNestedScroll'
import PullRefreshLayout, { PullEvent } from '../PullRefreshLayout'
import { DemoFlatList, useDemoFlatlistData } from './Components/DemoFlatList'
import { DemoScrollView } from './Components/DemoScrollView'
import { DemoWebView } from './Components/DemoWebView'
import { MenuBtn, PressType } from './Components/MenuBtn'

export type ContentType = 'webview' | 'scrollview' | 'flatlist' | 'nested' | 'nestedChild'

export const RefreshView = () => {
  const [refreshing, setRefreshing] = useState(false)
  const [url, setUrl] = useState('https://baike.baidu.com/')
  const [isMenuBtnActive, setIsMenuBtnActive] = useState(false)
  const [contentType, setContentType] = useState<ContentType>('nested')
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

    switch (contentType) {
      case 'flatlist':
        pendingAction.current = setTimeout(() => {
          addFlatlistRefreshItem()
          endRefresh()
        }, 1500)
        break
      case 'webview':
        setUrl(url => (url.includes('baike') ? 'https://baidu.com' : 'https://baike.baidu.com'))
        break
      default:
        pendingAction.current = setTimeout(() => {
          endRefresh()
        }, 1500)
    }
  }

  const endRefresh = () => {
    clearPendingAction()
    setRefreshing(false)
    setStatusText('下拉刷新')
  }

  const loadMore = () => {
    setLoadingMore(true)
    setStatusText('load more')
    switch (contentType) {
      case 'flatlist':
        pendingAction.current = setTimeout(() => {
          addFlatlistLoadMoreItem()
          endLoadMore()
        }, 1500)
        break

      default:
        pendingAction.current = setTimeout(() => {
          endLoadMore()
        }, 1500)
    }
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

  const handleMenuBtnTap = (type: PressType) => {
    switch (type) {
      case 'show':
        setIsMenuBtnActive(true)
        return
      case 'hide':
        setIsMenuBtnActive(false)
        return
      case 'refresh':
        beginRefresh()
        return
      case 'loadMore':
        loadMore()
        return
      default:
        endRefresh()
        setContentType(type)
        beginRefresh()
        return
    }
  }

  const renderContent = (contentType: ContentType) => {
    switch (contentType) {
      case 'flatlist':
        return <DemoFlatList data={flatlistData} />
      case 'scrollview':
        return <DemoScrollView />
      case 'webview':
        const onLoadFinish = () => {
          if (refreshing) {
            endRefresh()
          } else if (loadingMore) {
            setLoadingMore(false)
          }
        }
        return <DemoWebView url={url} onLoadFinish={onLoadFinish} />
      case 'nested':
        return <NativeNestedScroll />
      default:
        return <Text>Unsupported Content</Text>
    }
  }

  return (
    <View style={{ position: 'relative', flex: 1, overflow: 'hidden' }}>
      <PullRefreshLayout
        style={{ flex: 1 }}
        refreshViewOverPullLocation="center"
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
        {renderContent(contentType)}
      </PullRefreshLayout>
      <MenuBtn isActive={isMenuBtnActive} onPress={handleMenuBtnTap} />
    </View>
  )
}

export const PullRefreshPage = () => <RefreshView />

export default PullRefreshPage

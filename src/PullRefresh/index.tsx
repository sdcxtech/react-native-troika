import React, { useCallback, useEffect, useState } from 'react'
import { View, Text } from 'react-native'
import { NativeNestedScroll } from '../NativeNestedScroll'
import PullRefreshLayout, { PullEvent } from '../PullRefreshLayout'
import { DemoFlatList } from './Components/DemoFlatList'
import { DemoScrollView } from './Components/DemoScrollView'
import { DemoWebView } from './Components/DemoWebView'
import { MenuBtn, PressType } from './Components/MenuBtn'

export type ContentType = 'webview' | 'scrollview' | 'flatlist' | 'nested' | 'nestedChild'

export const RefreshView = () => {
  const [refreshing, setRefreshing] = useState(false)
  const [url, setUrl] = useState('https://www.shundaojia.com/')
  const [isMenuBtnActive, setIsMenuBtnActive] = useState(false)
  const [contentType, setContentType] = useState<ContentType>('flatlist')
  const [statusText, setStatusText] = useState('下拉刷新')

  const endRefresh = useCallback(() => {
    setRefreshing(false)
    setStatusText('下拉刷新')
  }, [])

  const beginRefresh = async () => {
    setRefreshing(true)
    setStatusText('正在刷新...')
    if (contentType === 'webview') {
      setUrl(url =>
        url.includes('weixin') ? 'https://www.shundaojia.com/' : 'https://developers.weixin.qq.com/miniprogram/design/',
      )
    }
  }

  useEffect(() => {
    if (refreshing && contentType !== 'webview') {
      setTimeout(() => {
        endRefresh()
      }, 2000)
    }
  }, [contentType, endRefresh, refreshing])

  const handlePullEvent = (event: PullEvent) => {
    const { currentRefreshViewOffset, totalRefreshViewOffset } = event.nativeEvent
    if (currentRefreshViewOffset >= totalRefreshViewOffset) {
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
            backgroundColor: statusText === '正在刷新...' ? 'red' : 'aqua',
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
        return <DemoFlatList />
      case 'scrollview':
        return <DemoScrollView />
      case 'webview':
        return <DemoWebView url={url} onLoadFinish={endRefresh} />
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
        refreshing={refreshing}
        onRefresh={beginRefresh}
        onStop={endRefresh}
        onPull={handlePullEvent}
        RefreshView={renderRefreshView()}>
        {renderContent(contentType)}
      </PullRefreshLayout>
      <MenuBtn isActive={isMenuBtnActive} onPress={handleMenuBtnTap} />
    </View>
  )
}

export const PullRefreshPage = () => <RefreshView />

export default PullRefreshPage

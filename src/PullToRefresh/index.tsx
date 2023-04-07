import React, { useCallback, useState } from 'react'
import { requireNativeComponent, StyleSheet, Text, ViewProps } from 'react-native'
import RefreshFooter, { RefreshFooterProps } from '../RefreshFooter'
import RefreshHeader, {
  RefreshHeaderProps,
  RefreshState,
  RefreshStateIdle,
  RefreshStateRefreshing,
} from '../RefreshHeader'

interface NativePullToRefreshProps extends ViewProps {
  enableRefreshAction?: boolean
  enableLoadMoreAction?: boolean
}

interface PullToRefreshProps extends ViewProps {
  onRefresh?: () => void
  refreshing?: boolean
  onLoadMore?: () => void
  loadingMore?: boolean
  noMoreData?: boolean
  header?: React.ReactElement<RefreshHeaderProps>
  footer?: React.ReactElement<RefreshFooterProps>
}

const NativePullToRefresh = requireNativeComponent<NativePullToRefreshProps>('PullToRefresh')

function PullToRefresh(props: PullToRefreshProps) {
  const {
    children,
    onRefresh,
    refreshing,
    onLoadMore,
    loadingMore,
    noMoreData,
    header,
    footer,
    style,
    ...rest
  } = props

  const renderHeader = () => {
    if (header) {
      return header
    }

    if (onRefresh) {
      return <DefaultRefreshHeader onRefresh={onRefresh} refreshing={!!refreshing} />
    }

    return null
  }

  const renderFooter = () => {
    if (footer) {
      return footer
    }

    if (onLoadMore) {
      return (
        <DefaultRefreshFooter
          onRefresh={onLoadMore}
          refreshing={!!loadingMore}
          noMoreData={noMoreData}
        />
      )
    }

    return null
  }

  return (
    <NativePullToRefresh
      style={[styles.fill, style]}
      {...rest}
      enableLoadMoreAction={!!onLoadMore || !!footer}
      enableRefreshAction={!!onRefresh || !!header}>
      {renderHeader()}
      {children}
      {renderFooter()}
    </NativePullToRefresh>
  )
}

interface DefaultRefreshHeaderProps {
  onRefresh?: () => void
  refreshing: boolean
}

function DefaultRefreshHeader(props: DefaultRefreshHeaderProps) {
  const { onRefresh, refreshing } = props

  const [text, setText] = useState('下拉刷新')

  const onStateChanged = useCallback((state: RefreshState) => {
    if (state === RefreshStateIdle) {
      setText('下拉刷新')
    } else if (state === RefreshStateRefreshing) {
      setText('正在刷新...')
    } else {
      setText('松开刷新')
    }
  }, [])

  const onOffsetChanged = useCallback((offset: number) => {
    console.log('refresh header offset', offset)
  }, [])

  return (
    <RefreshHeader
      style={styles.container}
      onOffsetChanged={onOffsetChanged}
      onStateChanged={onStateChanged}
      onRefresh={onRefresh}
      refreshing={refreshing}>
      <Text style={styles.text}>{text}</Text>
    </RefreshHeader>
  )
}

interface DefaultRefreshFooterProps {
  onRefresh?: () => void
  refreshing: boolean
  noMoreData?: boolean
}

function DefaultRefreshFooter(props: DefaultRefreshFooterProps) {
  const { onRefresh, refreshing, noMoreData } = props

  const [text, setText] = useState('上拉加载更多')

  const onStateChanged = useCallback((state: RefreshState) => {
    if (state === RefreshStateIdle) {
      setText('上拉加载更多')
    } else if (state === RefreshStateRefreshing) {
      setText('正在加载更多...')
    } else {
      setText('松开加载更多')
    }
  }, [])

  const onOffsetChanged = useCallback((offset: number) => {
    console.log('refresh footer offset', offset)
  }, [])

  return (
    <RefreshFooter
      style={styles.container}
      manual
      onOffsetChanged={onOffsetChanged}
      onStateChanged={onStateChanged}
      onRefresh={onRefresh}
      refreshing={refreshing}
      noMoreData={noMoreData}>
      <Text style={styles.text}>{noMoreData ? '没有更多数据了' : text}</Text>
    </RefreshFooter>
  )
}

const styles = StyleSheet.create({
  fill: {
    flex: 1,
  },
  container: {
    alignItems: 'center',
    backgroundColor: 'red',
  },
  text: {
    paddingVertical: 16,
    fontSize: 17,
    color: 'white',
  },
})

export default PullToRefresh

import React from 'react'
import { StyleSheet } from 'react-native'
import { DefaultPullToRefreshFooter } from '../Footer'
import { DefaultPullToRefreshHeader } from '../Header'
import { PullToRefreshProps } from '../types'
import { NativePullToRefresh } from './native'

export function PullToRefresh(props: PullToRefreshProps) {
  const { children, onRefresh, refreshing, onLoadMore, loadingMore, noMoreData, header, footer, style, ...rest } = props

  const renderHeader = () => {
    if (header) {
      return header
    }

    if (onRefresh) {
      return <DefaultPullToRefreshHeader onRefresh={onRefresh} refreshing={!!refreshing} />
    }

    return null
  }
  const renderFooter = () => {
    if (footer) {
      return footer
    }

    if (onLoadMore) {
      return <DefaultPullToRefreshFooter onRefresh={onLoadMore} refreshing={!!loadingMore} noMoreData={noMoreData} />
    }

    return null
  }

  return (
    <NativePullToRefresh style={[styles.fill, style]} {...rest}>
      {renderHeader()}
      {children}
      {renderFooter()}
    </NativePullToRefresh>
  )
}

const styles = StyleSheet.create({
  fill: {
    flex: 1,
  },
})

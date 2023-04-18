import React from 'react'
import { PullToRefreshProps, PullToRefresh as OriginPullToRefresh } from '@sdcx/pull-to-refresh'
import { LottiePullToRefreshHeader } from './LottiePullToRefreshHeader'
import { LottiePullToRefreshFooter } from './LottiePullToRefreshFooter'

export default function PullToRefesh(props: PullToRefreshProps) {
  const { onRefresh, refreshing, onLoadMore, loadingMore, noMoreData, children } = props
  return (
    <OriginPullToRefresh
      header={
        onRefresh ? (
          <LottiePullToRefreshHeader onRefresh={onRefresh} refreshing={!!refreshing} />
        ) : undefined
      }
      footer={
        onLoadMore ? (
          <LottiePullToRefreshFooter
            onRefresh={onLoadMore}
            refreshing={!!loadingMore}
            noMoreData={noMoreData}
          />
        ) : undefined
      }>
      {children}
    </OriginPullToRefresh>
  )
}

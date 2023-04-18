import React from 'react'
import { NativeSyntheticEvent, ViewProps } from 'react-native'

export const RefreshStateIdle = 0
export const RefreshStateComing = 1
export const RefreshStateRefreshing = 2
export type RefreshStateIdle = typeof RefreshStateIdle
export type RefreshStateComing = typeof RefreshStateComing
export type RefreshStateRefreshing = typeof RefreshStateRefreshing

export type RefreshState = RefreshStateIdle | RefreshStateComing | RefreshStateRefreshing

interface StateChangedEventData {
  state: RefreshState
}

interface OffsetChangedEventData {
  offset: number
}

export type PullToRefreshStateChangedEvent = NativeSyntheticEvent<StateChangedEventData>
export type PullToRefreshOffsetChangedEvent = NativeSyntheticEvent<OffsetChangedEventData>

export interface PullToRefreshHeaderProps {
  onRefresh?: () => void
  refreshing: boolean
}

export interface PullToRefreshFooterProps {
  onRefresh?: () => void
  refreshing: boolean
  noMoreData?: boolean
}

export interface PullToRefreshProps extends ViewProps {
  onRefresh?: () => void
  refreshing?: boolean
  onLoadMore?: () => void
  loadingMore?: boolean
  noMoreData?: boolean
  header?: React.ReactElement<PullToRefreshHeaderProps>
  footer?: React.ReactElement<PullToRefreshFooterProps>
}

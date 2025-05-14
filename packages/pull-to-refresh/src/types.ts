import React from 'react';
import {NativeSyntheticEvent, ViewProps} from 'react-native';

export const PullToRefreshStateIdle = 0;
export const PullToRefreshStateComing = 1;
export const PullToRefreshStateRefreshing = 2;
export type PullToRefreshStateIdle = typeof PullToRefreshStateIdle;
export type PullToRefreshStateComing = typeof PullToRefreshStateComing;
export type PullToRefreshStateRefreshing = typeof PullToRefreshStateRefreshing;

export type PullToRefreshState =
  | PullToRefreshStateIdle
  | PullToRefreshStateComing
  | PullToRefreshStateRefreshing;

interface StateChangedEventData {
  state: PullToRefreshState;
}

interface OffsetChangedEventData {
  offset: number;
}

export type PullToRefreshStateChangedEvent = NativeSyntheticEvent<StateChangedEventData>;
export type PullToRefreshOffsetChangedEvent = NativeSyntheticEvent<OffsetChangedEventData>;

export interface PullToRefreshHeaderProps {
  onRefresh?: () => void;
  refreshing: boolean;
}

export interface PullToRefreshFooterProps {
  onRefresh?: () => void;
  refreshing: boolean;
  noMoreData?: boolean;
  manual?: boolean;
}

export interface PullToRefreshProps extends ViewProps {
  onRefresh?: () => void;
  refreshing?: boolean;
  onLoadMore?: () => void;
  loadingMore?: boolean;
  noMoreData?: boolean;
  header?: React.ReactElement<PullToRefreshHeaderProps>;
  footer?: React.ReactElement<PullToRefreshFooterProps>;
}

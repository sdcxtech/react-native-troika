import React, { PropsWithChildren, useEffect, useRef, useState } from 'react'
import {
  findNodeHandle,
  NativeSyntheticEvent,
  requireNativeComponent,
  StyleProp,
  View,
  ViewStyle,
} from 'react-native'
import { setNativeLoadingMoreManually, setNativeRefreshingManually } from './commands'

const PullRefreshLayoutAndroid =
  requireNativeComponent<Omit<PullRefreshLayoutProps, 'RefreshView' | 'LoadMoreView'>>(
    'PullRefreshLayout',
  )

const LoadMorePlaceholderViewAndroid = requireNativeComponent(
  'PullRefreshLayoutLoadMorePlaceholderViewManager',
)

const RefreshPlaceholderViewAndroid = requireNativeComponent(
  'PullRefreshLayoutRefreshPlaceholderViewManager',
)

interface RefreshViewWrapperProps {
  ContentView: React.ComponentType<any> | React.ReactElement | null | undefined
  viewType: 'LOAD_MORE' | 'REFRESH' | 'NONE'
}

export type PullEvent = NativeSyntheticEvent<{
  currentRefreshViewOffset: number
  currentTargetViewOffset: number
  totalRefreshViewOffset: number
  totalTargetViewOffset: number
}>

export type LoadMorePullEvent = NativeSyntheticEvent<{
  offset: number
  total: number
}>

interface PullRefreshLayoutProps {
  style?: StyleProp<ViewStyle>

  refreshing: boolean
  onRefresh: () => void

  enableRefreshAction?: boolean
  enableRefreshOverPull?: boolean
  onRefreshPull?: (event: PullEvent) => void
  onRefreshStop?: () => void
  RefreshView?: React.ComponentType<any> | React.ReactElement | null | undefined
  refreshViewOverPullLocation?: 'center' | 'top' | 'bottom'

  enableLoadMoreAction?: boolean
  loadingMore?: boolean
  onLoadMore?: () => void
  onLoadMoreStop?: () => void
  onLoadMorePull?: (event: LoadMorePullEvent) => void
  LoadMoreView?: React.ComponentType<any> | React.ReactElement | null | undefined
}

function PullRefreshLayoutPlaceholderView({ ContentView, viewType }: RefreshViewWrapperProps) {
  const _children = ContentView ? (
    React.isValidElement(ContentView) ? (
      ContentView
    ) : (
      <ContentView />
    )
  ) : null
  if (React.isValidElement(_children)) {
    if (viewType === 'REFRESH') {
      return <RefreshPlaceholderViewAndroid>{_children}</RefreshPlaceholderViewAndroid>
    }
    if (viewType === 'LOAD_MORE') {
      return <LoadMorePlaceholderViewAndroid>{_children}</LoadMorePlaceholderViewAndroid>
    }
  }

  return null
}

export default function PullRefreshLayout({
  style,
  children,
  refreshing,
  enableRefreshAction = true,
  enableRefreshOverPull = true,
  onRefresh,
  onRefreshPull,
  onRefreshStop,
  RefreshView,
  refreshViewOverPullLocation = 'top',

  enableLoadMoreAction = false,
  loadingMore = false,
  onLoadMore,
  onLoadMorePull,
  onLoadMoreStop,
  LoadMoreView,
}: PropsWithChildren<PullRefreshLayoutProps>) {
  const ref = useRef(null)
  const [nativeRefreshing, setNativeRefreshing] = useState(refreshing)
  const [nativeLoadingMore, setNativeLoadingMore] = useState(loadingMore)

  useEffect(() => {
    if (nativeRefreshing !== refreshing && ref.current) {
      setNativeRefreshingManually(findNodeHandle(ref.current), refreshing)
      setNativeRefreshing(refreshing)
    }
  }, [nativeRefreshing, refreshing])
  const _onRefresh = () => {
    setNativeRefreshing(true)
    onRefresh()
  }
  const _onStop = () => {
    setNativeRefreshing(false)
    onRefreshStop && onRefreshStop()
  }

  useEffect(() => {
    if (nativeLoadingMore !== loadingMore && ref.current) {
      setNativeLoadingMoreManually(findNodeHandle(ref.current), loadingMore)
      setNativeLoadingMore(loadingMore)
    }
  }, [loadingMore, nativeLoadingMore])
  const _onLoadMore = () => {
    setNativeLoadingMore(true)
    onLoadMore && onLoadMore()
  }
  const _onLoadMoreStop = () => {
    setNativeLoadingMore(false)
    onLoadMoreStop && onLoadMoreStop()
  }

  const _children = React.isValidElement(children) ? React.Children.only(children) : children

  return (
    <PullRefreshLayoutAndroid
      ref={ref}
      style={style}
      refreshing={refreshing}
      enableRefreshAction={enableRefreshAction}
      enableRefreshOverPull={enableRefreshOverPull}
      onRefresh={_onRefresh}
      onRefreshPull={onRefreshPull}
      onRefreshStop={_onStop}
      refreshViewOverPullLocation={refreshViewOverPullLocation}
      enableLoadMoreAction={enableLoadMoreAction}
      loadingMore={loadingMore}
      onLoadMore={_onLoadMore}
      onLoadMorePull={onLoadMorePull}
      onLoadMoreStop={_onLoadMoreStop}>
      <PullRefreshLayoutPlaceholderView ContentView={RefreshView} viewType="REFRESH" />
      {_children}
      <PullRefreshLayoutPlaceholderView ContentView={LoadMoreView} viewType="LOAD_MORE" />
    </PullRefreshLayoutAndroid>
  )
}

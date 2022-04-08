import React, { PropsWithChildren, useEffect, useRef, useState } from 'react'
import { findNodeHandle, NativeSyntheticEvent, requireNativeComponent, StyleProp, View, ViewStyle } from 'react-native'
import { setNativeLoadingMoreManually, setNativeRefreshingManually } from './commands'

const PullRefreshLayoutAndroid =
  requireNativeComponent<Omit<PullRefreshLayoutProps, 'RefreshView'>>('PullRefreshLayout')

const PullRefreshLayoutPlaceholderViewAndroid = requireNativeComponent<Omit<RefreshViewWrapperProps, 'ContentView'>>(
  'PullRefreshLayoutPlaceholderView',
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

export function PullRefreshLayoutPlaceholderView({ ContentView, viewType }: RefreshViewWrapperProps) {
  const _children = ContentView ? React.isValidElement(ContentView) ? ContentView : <ContentView /> : null
  return React.isValidElement(_children) ? (
    <PullRefreshLayoutPlaceholderViewAndroid viewType={viewType}>{_children}</PullRefreshLayoutPlaceholderViewAndroid>
  ) : null
}

function PullRefreshLayout({
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
  const _children = React.Children.only(children)
  const ref = useRef(null)
  const [nativeRefreshing, setNativeRefreshing] = useState(refreshing)
  const [nativeLoadingMore, setNativeLoadingMore] = useState(refreshing)

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
      {React.isValidElement(_children)
        ? React.cloneElement(<View style={{ height: '100%' }}>{_children}</View>, {
            removeClippedSubviews: false,
          })
        : _children}
      <PullRefreshLayoutPlaceholderView ContentView={LoadMoreView} viewType="LOAD_MORE" />
    </PullRefreshLayoutAndroid>
  )
}

export default PullRefreshLayout

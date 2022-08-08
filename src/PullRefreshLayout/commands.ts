import { UIManager } from 'react-native'

export const setNativeRefreshingManually = (viewId: number | null, refreshing: boolean) =>
  UIManager.dispatchViewManagerCommand(
    viewId,
    UIManager.getViewManagerConfig('PullRefreshLayout').Commands.setNativeRefreshing.toString(),
    [refreshing],
  )

export const setNativeLoadingMoreManually = (viewId: number | null, loadingMore: boolean) =>
  UIManager.dispatchViewManagerCommand(
    viewId,
    UIManager.getViewManagerConfig('PullRefreshLayout').Commands.setNativeLoadingMore.toString(),
    [loadingMore],
  )

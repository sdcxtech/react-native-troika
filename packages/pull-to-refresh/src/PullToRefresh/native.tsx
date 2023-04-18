import { Platform, requireNativeComponent, ViewProps } from 'react-native'

interface NativePullToRefreshProps extends ViewProps {}

export const NativePullToRefresh = requireNativeComponent<NativePullToRefreshProps>(
  Platform.OS === 'ios' ? 'PullToRefresh' : 'SPullRefreshLayout',
)

import { requireNativeComponent, ViewProps } from 'react-native'

interface NativePullToRefreshProps extends ViewProps {}

export const NativePullToRefresh = requireNativeComponent<NativePullToRefreshProps>('PullToRefresh')

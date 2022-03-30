import React, { PropsWithChildren } from 'react'
import { NativeSyntheticEvent, requireNativeComponent, StyleProp, View, ViewStyle } from 'react-native'

const PullRefreshLayoutAndroid =
  requireNativeComponent<Omit<PullRefreshLayoutProps, 'RefreshView'>>('PullRefreshLayout')

const RefreshViewWrapperAndroid = requireNativeComponent<{}>('RefreshViewWrapper')

interface RefreshViewWrapperProps {
  RefreshView: React.ComponentType<any> | React.ReactElement | null | undefined
}

export type PullEvent = NativeSyntheticEvent<{
  currentRefreshViewOffset: number
  currentTargetViewOffset: number
  totalRefreshViewOffset: number
  totalTargetViewOffset: number
}>

interface PullRefreshLayoutProps {
  style?: StyleProp<ViewStyle>
  refreshing: boolean
  enable?: boolean
  enableOverPull?: boolean
  onRefresh: () => void
  onPull?: (event: PullEvent) => void
  onStop?: () => void
  RefreshView?: React.ComponentType<any> | React.ReactElement | null | undefined
  refreshViewOverPullLocation?: 'center' | 'top' | 'bottom'
}

export function RefreshViewWrapper({ RefreshView }: RefreshViewWrapperProps) {
  const _children = RefreshView ? React.isValidElement(RefreshView) ? RefreshView : <RefreshView /> : null
  return React.isValidElement(_children) ? <RefreshViewWrapperAndroid>{_children}</RefreshViewWrapperAndroid> : null
}

function PullRefreshLayout({
  style,
  children,
  refreshing,
  enable = true,
  enableOverPull = true,
  onRefresh,
  onPull,
  onStop,
  RefreshView,
  refreshViewOverPullLocation = 'top',
}: PropsWithChildren<PullRefreshLayoutProps>) {
  const _children = React.Children.only(children)

  return (
    <PullRefreshLayoutAndroid
      style={style}
      refreshing={refreshing}
      enable={enable}
      enableOverPull={enableOverPull}
      onRefresh={onRefresh}
      onPull={onPull}
      onStop={onStop}
      refreshViewOverPullLocation={refreshViewOverPullLocation}>
      <RefreshViewWrapper RefreshView={RefreshView} />
      {React.isValidElement(_children)
        ? React.cloneElement(<View style={{ height: '100%' }}>{_children}</View>, {
            removeClippedSubviews: false,
          })
        : _children}
    </PullRefreshLayoutAndroid>
  )
}

export default PullRefreshLayout

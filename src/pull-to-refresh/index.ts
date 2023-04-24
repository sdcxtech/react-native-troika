import Navigation from 'hybrid-navigation'

import Home from './Home'
import PullRefreshFlatList from './PullRefreshFlatList'
import PullRefreshPagerView from './PullRefreshPagerView'
import PullRefreshFlatListNestedScroll from './PullRefreshFlatListNestedScroll'
import PullRefreshPagerViewNestedScroll from './PullRefreshPagerViewNestedScroll'
import PullRefreshNestedScrollPagerView from './PullRefreshNestedScrollPagerView'

export function registerPullToRefreshComponent() {
  Navigation.registerComponent('PullToRefresh', () => Home)
  Navigation.registerComponent('PullRefreshFlatList', () => PullRefreshFlatList)
  Navigation.registerComponent(
    'PullRefreshFlatListNestedScroll',
    () => PullRefreshFlatListNestedScroll,
  )
  Navigation.registerComponent('PullRefreshPagerView', () => PullRefreshPagerView)
  Navigation.registerComponent(
    'PullRefreshPagerViewNestedScroll',
    () => PullRefreshPagerViewNestedScroll,
  )
  Navigation.registerComponent(
    'PullRefreshNestedScrollPagerView',
    () => PullRefreshNestedScrollPagerView,
  )
}

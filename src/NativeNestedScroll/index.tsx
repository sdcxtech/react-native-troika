import { withNavigationItem } from 'hybrid-navigation'
import React, { useEffect, useRef, useState } from 'react'
import { findNodeHandle, StyleSheet, Text, TouchableOpacity, View } from 'react-native'
import CoordinatorLayout from '../CoordinatorLayout'
import AppBarLayout, { INVALID_VIEW_ID } from '../AppBarLayout'
import PagerView from 'react-native-pager-view'
import { DemoFlatList } from '../PullRefresh/Components/DemoFlatList'
import { DemoScrollView } from '../PullRefresh/Components/DemoScrollView'
import { DemoWebView } from '../PullRefresh/Components/DemoWebView'
import PullRefreshLayout from '../PullRefreshLayout'

export function NativeNestedScroll() {
  const ref = useRef(null)
  const [anchorViewId, setAnchorViewId] = useState(INVALID_VIEW_ID)
  const [anchorViewHeight, setAnchorViewHeight] = useState(100)

  useEffect(() => {
    const viewId = findNodeHandle(ref.current) || INVALID_VIEW_ID
    setAnchorViewId(viewId)
  }, [anchorViewHeight])

  const [refreshing, setRefreshing] = useState(false)
  const beginRefresh = () => {
    setRefreshing(true)
  }

  useEffect(() => {
    setTimeout(() => {
      if (refreshing) {
        setRefreshing(false)
      }
    }, 1500)
  }, [refreshing])

  return (
    <View style={styles.container}>
      <CoordinatorLayout style={styles.coordinator}>
        <AppBarLayout anchorViewId={anchorViewId}>
          <TouchableOpacity
            style={{ backgroundColor: 'red' }}
            onPress={() => setAnchorViewHeight(h => (h > 300 ? 100 : h + 100))}>
            <Text style={styles.text}>change appbar height</Text>
          </TouchableOpacity>
          <View
            style={[styles.text, { height: anchorViewHeight, backgroundColor: 'green', paddingVertical: 0 }]}
            ref={ref}>
            <Text>anchor-parent-sibling</Text>
            <View>
              <Text>anchor-sibling</Text>
              <Text>anchor</Text>
            </View>
            <Text>anchor-parent-sibling</Text>
          </View>
        </AppBarLayout>
        <PagerView style={styles.pager}>
          <DemoFlatList />
          <DemoScrollView />
          <PullRefreshLayout refreshing={refreshing} onRefresh={beginRefresh} refreshViewOverPullLocation="bottom">
            <DemoWebView url="https://wangdoc.com/javascript/stdlib/array.html" />
          </PullRefreshLayout>
        </PagerView>
      </CoordinatorLayout>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FF0000',
  },
  coordinator: {
    flex: 1,
    backgroundColor: '#fff',
  },
  content: {
    backgroundColor: '#0000FF',
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    paddingVertical: 20,
    fontSize: 18,
    color: '#FFFFFF',
  },
  pager: {
    position: 'relative',
    height: '100%',
    overflow: 'hidden',
  },
})

const NativeNestedScrollPage = () => <NativeNestedScroll />

export default withNavigationItem({
  titleItem: {
    title: '原生嵌套滑动',
  },
})(NativeNestedScrollPage)

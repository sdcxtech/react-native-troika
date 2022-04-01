import { withNavigationItem } from 'hybrid-navigation'
import React, { useEffect, useRef, useState } from 'react'
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native'
import CoordinatorLayout from '../CoordinatorLayout'
import AppBarLayout from '../AppBarLayout'
import PagerView from 'react-native-pager-view'
import { DemoFlatList } from '../PullRefresh/Components/DemoFlatList'
import { DemoScrollView } from '../PullRefresh/Components/DemoScrollView'
import { DemoWebView } from '../PullRefresh/Components/DemoWebView'
import PullRefreshLayout from '../PullRefreshLayout'

export function NativeNestedScroll() {
  const ref = useRef(null)
  const [anchorViewHeight, setAnchorViewHeight] = useState(-1)

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

  const [stickyHeaderBeginIndex, setStickyHeaderBeginIndex] = useState(1)
  return (
    <View style={styles.container}>
      <CoordinatorLayout style={styles.coordinator}>
        <AppBarLayout stickyHeaderBeginIndex={stickyHeaderBeginIndex}>
          <TouchableOpacity
            style={{ backgroundColor: 'red' }}
            onPress={() => {
              setStickyHeaderBeginIndex(stickyHeaderBeginIndex > 0 ? 0 : 1)
              setAnchorViewHeight(h => (h > 90 ? 30 : h + 30))
            }}>
            <Text style={styles.text}>change appbar height</Text>
          </TouchableOpacity>
          <View style={[styles.text, { height: anchorViewHeight > 0 ? anchorViewHeight : undefined }]} ref={ref}>
            <Text>anchor</Text>
          </View>
        </AppBarLayout>
        <PagerView style={styles.pager}>
          <DemoFlatList />
          <DemoScrollView />
          <PullRefreshLayout refreshing={refreshing} onRefresh={beginRefresh} refreshViewOverPullLocation="bottom">
            <DemoWebView url="https://wangdoc.com" />
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

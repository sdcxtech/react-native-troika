import { withNavigationItem } from 'hybrid-navigation'
import React, { useEffect, useRef, useState } from 'react'
import {
  Dimensions,
  findNodeHandle,
  FlatList,
  Image,
  ListRenderItem,
  PixelRatio,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native'
import CoordinatorLayout from '../CoordinatorLayout'
import AppBarLayout, { INVALID_VIEW_ID } from '../AppBarLayout'
import PagerView from 'react-native-pager-view'
import WebView from 'react-native-webview'

const MaxHeight = PixelRatio.getPixelSizeForLayoutSize(Dimensions.get('window').height) / 4
function NativeNestedScroll() {
  const ref = useRef(null)
  const [anchorViewId, setAnchorViewId] = useState(INVALID_VIEW_ID)
  const [anchorViewHeight, setAnchorViewHeight] = useState(Math.random() * MaxHeight)

  useEffect(() => {
    const viewId = findNodeHandle(ref.current) || INVALID_VIEW_ID
    setAnchorViewId(viewId)
  }, [anchorViewHeight])

  const renderListItem: ListRenderItem<string> = ({ item }) => {
    return <ListItem title={item} />
  }
  const data = Array(100)
    .fill('')
    .map((_, index) => index + '')

  return (
    <View style={styles.container}>
      <CoordinatorLayout style={styles.coordinator}>
        <AppBarLayout anchorViewId={anchorViewId}>
          <TouchableOpacity onPress={() => setAnchorViewHeight(Math.random() * MaxHeight)} style={[styles.content]}>
            <Text style={styles.text}>demo</Text>
          </TouchableOpacity>
          <View>
            <Text>anchor-parent-sibling</Text>
            <View>
              <Text>anchor-sibling</Text>
              <Text
                style={[styles.text, { height: anchorViewHeight, backgroundColor: 'green', paddingVertical: 0 }]}
                ref={ref}>
                anchor
              </Text>
            </View>
            <Text>anchor-parent-sibling</Text>
          </View>
        </AppBarLayout>
        <PagerView style={styles.pager}>
          <FlatList
            nestedScrollEnabled
            data={data}
            style={styles.list}
            keyExtractor={item => item}
            renderItem={renderListItem}
          />
          <ScrollView nestedScrollEnabled style={{ flex: 1 }}>
            <View key={'1'} style={{ height: 200, backgroundColor: 'red' }} />
            <View key={'2'} style={{ height: 300, backgroundColor: 'greed' }} />
            <View key={'3'} style={{ height: 400, backgroundColor: 'gray' }} />
          </ScrollView>
          <WebView
            contentContainerStyle={{ flexGrow: 1 }}
            style={{ flex: 1 }}
            originWhitelist={['*']}
            source={{ uri: 'https://wangdoc.com/javascript/stdlib/array.html' }}
            cacheEnabled={false}
          />
        </PagerView>
      </CoordinatorLayout>
    </View>
  )
}

interface ListItemProps {
  title: string
  onPress?: () => void
}

function ListItem({ title, onPress }: ListItemProps) {
  return (
    <TouchableOpacity style={styles.item} onPress={onPress}>
      <Text style={styles.itemText}>{title}</Text>
      <Image source={require('../assets/indicator.png')} />
    </TouchableOpacity>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FF0000',
  },
  coordinator: {
    flex: 1,
    backgroundColor: '#00FF00',
  },
  content: {
    backgroundColor: '#0000FF',
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    paddingVertical: 100,
    fontSize: 18,
    color: '#FFFFFF',
  },
  pager: {
    height: '100%',
  },
  list: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  web: {
    flex: 1,
  },
  item: {
    height: 60,
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
    borderBottomWidth: 1,
    borderBottomColor: '#EEEEEE',
    paddingLeft: 16,
    paddingRight: 16,
  },
  itemText: {
    color: '#222222',
    fontSize: 17,
  },
})

export default withNavigationItem({
  titleItem: {
    title: '原生嵌套滑动',
  },
})(NativeNestedScroll)

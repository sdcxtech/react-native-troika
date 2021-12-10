import { withNavigationItem } from 'hybrid-navigation'
import React, { useCallback, useState } from 'react'
import {
  LayoutChangeEvent,
  NativeScrollEvent,
  NativeSyntheticEvent,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native'

function NestedScroll() {
  const items = Array<string>(50).fill('')

  const [outerEnabled, setOuterEnabled] = useState(true)
  const [innerenabled, setInnerenabled] = useState(false)

  const onInnerLayout = useCallback(
    ({
      nativeEvent: {
        layout: { width, height },
      },
    }: LayoutChangeEvent) => {
      console.log(width, height)
    },
    [],
  )

  const onInnerScroll = useCallback(
    ({
      nativeEvent: { contentOffset, contentSize, layoutMeasurement, velocity },
    }: NativeSyntheticEvent<NativeScrollEvent>) => {
      console.log('inner:', contentOffset, contentSize, layoutMeasurement, velocity)
      console.log(Math.round(contentOffset.y + layoutMeasurement.height), Math.round(contentSize.height))

      if (contentOffset.y === 0 && velocity && velocity.y < 0) {
        setInnerenabled(false)
        setOuterEnabled(true)
        return
      }

      setInnerenabled(true)
    },
    [],
  )

  const [layout, setLayout] = useState({ width: 0, height: 0 })

  const onOuterLayout = useCallback(
    ({
      nativeEvent: {
        layout: { width, height },
      },
    }: LayoutChangeEvent) => {
      console.log(width, height)
      setLayout({ width, height })
    },
    [],
  )

  const onOuterScroll = useCallback(
    ({
      nativeEvent: { contentOffset, contentSize, layoutMeasurement, velocity },
    }: NativeSyntheticEvent<NativeScrollEvent>) => {
      console.log('outer:', contentOffset, contentSize, layoutMeasurement, velocity)
      console.log(Math.trunc(contentOffset.y + layoutMeasurement.height), Math.trunc(contentSize.height))

      const isReachEnd = Math.trunc(contentSize.height) <= Math.trunc(contentOffset.y + layoutMeasurement.height)

      if (isReachEnd && velocity && velocity.y > 0) {
        setInnerenabled(true)
        setOuterEnabled(false)
        return
      }

      setOuterEnabled(true)
    },
    [],
  )

  return (
    <ScrollView
      style={styles.container}
      contentContainerStyle={styles.content}
      scrollEnabled={outerEnabled}
      onLayout={onOuterLayout}
      onScroll={onOuterScroll}>
      <View style={styles.header}>
        <Text style={styles.text}>Header</Text>
      </View>
      <View style={styles.tabbar}>
        <Text style={styles.text}>tabbar</Text>
      </View>
      <ScrollView
        style={[styles.nested, { ...layout }]}
        nestedScrollEnabled
        scrollEnabled={innerenabled && !outerEnabled}
        onLayout={onInnerLayout}
        onScroll={onInnerScroll}>
        {items.map((v, i) => (
          <View style={styles.item} key={i}>
            <Text style={styles.itemText}>{i}</Text>
          </View>
        ))}
      </ScrollView>
    </ScrollView>
  )
}

export default withNavigationItem({
  titleItem: {
    title: '嵌套滚动',
  },
})(NestedScroll)

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  content: {
    flexGrow: 1,
    backgroundColor: '#FFFFFF',
  },
  header: {
    paddingVertical: 200,
    alignItems: 'center',
    backgroundColor: '#FF0000',
  },
  text: {
    fontSize: 30,
  },
  tabbar: {
    height: 60,
    backgroundColor: '#00FF00',
    justifyContent: 'center',
    alignItems: 'center',
  },
  nested: {
    flex: 1,
  },
  item: {
    height: 60,
    borderBottomColor: '#DDDDDD',
    borderBottomWidth: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  itemText: {
    fontSize: 16,
  },
})

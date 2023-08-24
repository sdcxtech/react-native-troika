import { withNavigationItem } from 'hybrid-navigation'
import React, { useEffect, useState } from 'react'
import { Button, Image, StyleSheet, View } from 'react-native'
import { NestedScrollView, NestedScrollViewHeader } from '@sdcx/nested-scroll'
import TabView from '../../components/tabview'

export function NestedScrollTabView() {
  const [height, setHeight] = useState(0)

  useEffect(() => {
    setHeight(Math.random() * 1000)
  }, [])

  const [visible, setVisible] = useState(false)

  return (
    <NestedScrollView>
      <NestedScrollViewHeader>
        <Image source={require('assets/cover.webp')} style={styles.image} resizeMode="cover" />
        <View style={{ width: '100%' }}>
          {visible && <View style={{ height: 100, backgroundColor: '#332743' }} />}
          <View style={{ height, backgroundColor: '#ff2743' }} />
        </View>
        <Button
          title={visible ? '隐藏' : '显示'}
          onPress={() => {
            setVisible(!visible)
          }}
        />
        <Button
          title="改变高度"
          onPress={() => {
            setHeight(Math.random() * 1000)
          }}
        />
      </NestedScrollViewHeader>
      <TabView />
    </NestedScrollView>
  )
}

const styles = StyleSheet.create({
  image: {
    height: 160,
    width: '100%',
  },
})

export default withNavigationItem({
  titleItem: {
    title: 'NestedScroll + TabView',
  },
})(NestedScrollTabView)

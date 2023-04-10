import React from 'react'
import { StyleSheet, View } from 'react-native'
import { statusBarHeight, topBarHeight, withNavigationItem } from 'hybrid-navigation'

import NestedScrollView from '../NestedScrollView'

import { FlatListPage } from '../components/FlatListPage'
import { ParallaxHeader } from './ParallaxHeader'
import { useAnimateScrollView } from './useAnimatedScrollView'
import AnimatedNavbar from './AnimatedNavbar'
import { TopNavBar } from './TopNavBar'
import { HeaderNavBar } from './HeaderNavBar'
import { HeaderComponent } from './HeaderComponent'

export function NestedScrollParallaxHeader() {
  const imageHeight = 220

  const [scroll, onScroll, scale, translateYDown, translateYUp] = useAnimateScrollView(
    imageHeight,
    false,
  )

  return (
    <View style={styles.fill}>
      <NestedScrollView style={styles.coordinator} bounces>
        <ParallaxHeader
          topBarHeight={topBarHeight()}
          imageHeight={imageHeight}
          imageSource={require('../components/assets/cover.webp')}
          onScroll={onScroll}
          scale={scale}
          translateYDown={translateYDown}
          translateYUp={translateYUp}>
          <HeaderComponent />
        </ParallaxHeader>
        <FlatListPage />
      </NestedScrollView>
      <AnimatedNavbar
        scroll={scroll}
        headerHeight={topBarHeight()}
        statusBarHeight={statusBarHeight()}
        imageHeight={imageHeight}
        OverflowHeaderComponent={<HeaderNavBar />}
        TopNavbarComponent={<TopNavBar />}
      />
    </View>
  )
}

const styles = StyleSheet.create({
  fill: {
    flex: 1,
  },
  coordinator: {
    height: '100%',
    backgroundColor: '#fff',
  },
  content: {
    backgroundColor: '#0000FF',
    justifyContent: 'center',
    alignItems: 'center',
  },
  imgContainer: {
    alignItems: 'center',
    overflow: 'hidden',
  },
  image: {
    height: 160,
    width: '100%',
  },
  text: {
    height: 60,
    justifyContent: 'center',
    fontSize: 18,
    alignSelf: 'center',
    color: '#FFFFFF',
  },
})

export default withNavigationItem({
  topBarHidden: true,
  titleItem: {
    title: '',
  },
})(NestedScrollParallaxHeader)

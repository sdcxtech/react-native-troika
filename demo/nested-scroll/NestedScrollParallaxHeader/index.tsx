import React from 'react';
import {StyleSheet, View} from 'react-native';
import {statusBarHeight, topBarHeight, withNavigationItem} from 'hybrid-navigation';

import {NestedScrollView} from '@sdcx/nested-scroll';

import {FlatListPage} from '../../components/FlatListPage';
import {ParallaxHeader} from './ParallaxHeader';
import {useAnimateScrollView} from './hooks/useAnimatedScrollView';
import AnimatedNavbar from './AnimatedNavbar';
import {TopNavBar} from './components/TopNavBar';
import {HeaderNavBar} from './components/HeaderNavBar';
import {HeaderComponent} from './components/HeaderComponent';

export function NestedScrollParallaxHeader() {
  const imageHeight = 220;

  const [scroll, onScroll, scale, translateYDown, translateYUp] = useAnimateScrollView(
    imageHeight,
    false,
  );

  return (
    <View style={styles.fill}>
      <NestedScrollView bounces>
        <ParallaxHeader
          topBarHeight={topBarHeight()}
          imageHeight={imageHeight}
          imageSource={require('assets/cover.webp')}
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
  );
}

const styles = StyleSheet.create({
  fill: {
    flex: 1,
  },
  image: {
    height: 160,
    width: '100%',
  },
});

export default withNavigationItem({
  topBarHidden: true,
  titleItem: {
    title: '',
  },
})(NestedScrollParallaxHeader);

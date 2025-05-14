import React, {PropsWithChildren} from 'react';
import {
  Animated,
  ImageBackground,
  ImageStyle,
  StyleProp,
  StyleSheet,
  useWindowDimensions,
  View,
} from 'react-native';

import {NestedScrollViewHeader} from '@sdcx/nested-scroll';

const NestedScrollViewHeaderAnimated = Animated.createAnimatedComponent(NestedScrollViewHeader);
const AnimatedImageBackground = Animated.createAnimatedComponent(ImageBackground);

export type ParallaxHeaderProps = {
  imageHeight: number;
  topBarHeight: number;
  onScroll?: (event: any) => void;
  translateYUp: Animated.AnimatedInterpolation | 0;
  translateYDown: Animated.AnimatedInterpolation | 0;
  scale: Animated.AnimatedInterpolation | 1;
  imageStyle?: StyleProp<ImageStyle>;
  imageSource: any;
};

export function ParallaxHeader(props: PropsWithChildren<ParallaxHeaderProps>) {
  const {
    imageHeight,
    topBarHeight,
    translateYUp,
    translateYDown,
    scale,
    imageStyle,
    imageSource,
    onScroll,
    children,
  } = props;

  const width = useWindowDimensions().width;

  return (
    <NestedScrollViewHeaderAnimated stickyHeight={topBarHeight} onScroll={onScroll}>
      <View
        style={[
          styles.imgContainer,
          {
            marginTop: -imageHeight * 4,
            paddingTop: imageHeight * 4,
          },
        ]}>
        <AnimatedImageBackground
          source={imageSource}
          style={[
            {height: imageHeight, width: width * 1.2, justifyContent: 'center'},
            {
              transform: [{scale: scale}, {translateY: translateYUp}, {translateY: translateYDown}],
            },
            imageStyle,
          ]}>
          {children}
        </AnimatedImageBackground>
      </View>
    </NestedScrollViewHeaderAnimated>
  );
}

const styles = StyleSheet.create({
  imgContainer: {
    alignItems: 'center',
    overflow: 'hidden',
  },
});

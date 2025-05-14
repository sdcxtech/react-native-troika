import PagerView, {PagerViewOnPageScrollEvent} from 'react-native-pager-view';
import {useMemo, useRef} from 'react';
import {Animated, Dimensions} from 'react-native';

export default function usePagerView(dataLen: number) {
  const width = Dimensions.get('window').width;
  const scrollOffsetAnimatedValue = useRef(new Animated.Value(0)).current;
  const positionAnimatedValue = useRef(new Animated.Value(0)).current;
  const inputRange = [0, dataLen];
  const scrollX = Animated.add(scrollOffsetAnimatedValue, positionAnimatedValue).interpolate({
    inputRange,
    outputRange: [0, dataLen * width],
  });

  const onPageScroll = useMemo(
    () =>
      Animated.event<PagerViewOnPageScrollEvent>(
        [
          {
            nativeEvent: {
              offset: scrollOffsetAnimatedValue,
              position: positionAnimatedValue,
            },
          },
        ],
        {
          useNativeDriver: false,
        },
      ),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [],
  );

  const ref = useRef<PagerView>(null);

  return {ref, onPageScroll, scrollX};
}

import React, {useMemo, useRef} from 'react';
import {
  Animated,
  NativeSyntheticEvent,
  PixelRatio,
  ScrollView,
  StyleSheet,
  View,
} from 'react-native';
import {LoremIpsum} from '../../components/LoremIpsum';
import BottomSheet, {OffsetChangedEventData} from '@sdcx/bottom-sheet';
import {withNavigationItem} from 'hybrid-navigation';
import DropShadow from 'react-native-drop-shadow';

const HEADER_HEIGHT = 50;

const AnimatedBottomSheet = Animated.createAnimatedComponent(BottomSheet);

function BottomSheetBackdropShadow() {
  const offset = useRef(new Animated.Value(0)).current;

  const backdropStyle = {
    opacity: offset.interpolate({
      inputRange: [0, 1],
      outputRange: [1, 0],
    }),
  };

  const onSlide = useMemo(
    () =>
      Animated.event<NativeSyntheticEvent<OffsetChangedEventData>>(
        [
          {
            nativeEvent: {
              progress: offset,
            },
          },
        ],
        {
          useNativeDriver: true,
          listener(event) {
            console.info('onSlide', event.nativeEvent);
          },
        },
      ),
    [offset],
  );

  return (
    <View style={styles.container}>
      <ScrollView>
        <LoremIpsum />
        <LoremIpsum />
        <LoremIpsum />
      </ScrollView>
      <Animated.View
        style={[StyleSheet.absoluteFill, styles.backdrop, backdropStyle]}
        pointerEvents="box-none"
      />
      <AnimatedBottomSheet fitToContents peekHeight={200} onSlide={onSlide}>
        <DropShadow style={styles.shadow}>
          <View style={styles.header} />
        </DropShadow>
        <View style={styles.content}>
          <LoremIpsum words={200} />
        </View>
      </AnimatedBottomSheet>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#eef',
  },
  backdrop: {
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
  },
  shadow: {
    shadowRadius: 16,
    shadowColor: '#000',
    shadowOpacity: 0.4,
    shadowOffset: {
      width: 0,
      height: 0,
    },
  },
  header: {
    height: PixelRatio.roundToNearestPixel(HEADER_HEIGHT),
    backgroundColor: 'coral',
    borderTopLeftRadius: 16,
    borderTopRightRadius: 16,
  },
  content: {
    backgroundColor: '#ff9f7A',
  },
});

export default withNavigationItem({
  titleItem: {
    title: 'BottomSheet + Backdrop + Shadow',
  },
})(BottomSheetBackdropShadow);

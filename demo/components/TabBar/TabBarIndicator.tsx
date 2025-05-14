import React from 'react';
import {Animated, StyleProp, StyleSheet, ViewStyle} from 'react-native';

interface TabBarIndicatorProps {
  style?: StyleProp<ViewStyle>;
  scrollX: Animated.AnimatedInterpolation;
}

export default function TabBarIndicator({style, scrollX}: TabBarIndicatorProps) {
  return (
    <Animated.View
      key={'indicator'}
      style={[styles.indicator, style, {transform: [{translateX: scrollX}]}]}
    />
  );
}

const styles = StyleSheet.create({
  indicator: {
    position: 'absolute',
    left: 0,
    bottom: 0,
    width: 24,
    height: 4,
    backgroundColor: '#448AFF',
    borderRadius: 2,
  },
});

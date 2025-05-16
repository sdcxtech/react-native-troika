import React from 'react';
import {Animated, StyleSheet, useWindowDimensions, View, ViewStyle} from 'react-native';
export interface ExpandingDotProps {
  data: Array<Object>;
  scrollX: Animated.Value | Animated.AnimatedInterpolation<number>;
  containerStyle?: ViewStyle;
  dotStyle?: ViewStyle;
  inActiveDotOpacity?: number;
  inActiveDotColor?: string;
  expandingDotWidth?: number;
  activeDotColor?: string;
}

const ExpandingDot = ({
  scrollX,
  data,
  dotStyle,
  containerStyle,
  inActiveDotOpacity,
  inActiveDotColor,
  expandingDotWidth,
  activeDotColor,
}: ExpandingDotProps) => {
  const {width} = useWindowDimensions();

  const defaultProps = {
    inActiveDotColor: inActiveDotColor || '#2A3080',
    inActiveDotOpacity: inActiveDotOpacity || 0.5,
    expandingDotWidth: expandingDotWidth || 12,
    dotWidth: (dotStyle?.width as number) || 4,
    activeDotColor: activeDotColor || '#2A3080',
  };

  return (
    <View pointerEvents={'none'} style={[styles.containerStyle, containerStyle]}>
      {data.map((_, index) => {
        const inputRange = [(index - 1) * width, index * width, (index + 1) * width];

        const colour = scrollX.interpolate({
          inputRange,
          outputRange: [
            defaultProps.inActiveDotColor,
            defaultProps.activeDotColor,
            defaultProps.inActiveDotColor,
          ],
          extrapolate: 'clamp',
        });
        const opacity = scrollX.interpolate({
          inputRange,
          outputRange: [defaultProps.inActiveDotOpacity, 1, defaultProps.inActiveDotOpacity],
          extrapolate: 'clamp',
        });
        const expand = scrollX.interpolate({
          inputRange,
          outputRange: [
            defaultProps.dotWidth,
            defaultProps.expandingDotWidth,
            defaultProps.dotWidth,
          ],
          extrapolate: 'clamp',
        });

        return (
          <Animated.View
            key={`dot-${index}`}
            style={[
              styles.dotStyle,
              dotStyle,
              {width: expand},
              {opacity},
              {backgroundColor: colour},
            ]}
          />
        );
      })}
    </View>
  );
};

const styles = StyleSheet.create({
  containerStyle: {
    position: 'absolute',
    bottom: 0,
    flexDirection: 'row',
    alignSelf: 'center',
  },
  dotStyle: {
    width: 4,
    height: 4,
    borderRadius: 2,
    marginHorizontal: 4,
  },
});

export default ExpandingDot;

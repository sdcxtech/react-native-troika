import React, { PropsWithChildren, useMemo } from 'react';
import { StyleProp, StyleSheet, useWindowDimensions, View, ViewStyle } from 'react-native';
import { Gesture, GestureDetector, GestureHandlerRootView } from 'react-native-gesture-handler';
import Animated, {
	runOnJS,
	useAnimatedStyle,
	useSharedValue,
	withSpring,
} from 'react-native-reanimated';
import { BallProps } from './types';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

export default function Ball({
	anchor,
	children,
	onOffsetChanged = () => {},
}: PropsWithChildren<BallProps>) {
	const insets = useSafeAreaInsets();
	const barHeight = insets.top;

	const { width: windowWidth, height: windowHeight } = useWindowDimensions();

	const gap = 8;

	const x = useSharedValue(anchor.x);
	const y = useSharedValue(anchor.y);

	const ballStyles: StyleProp<ViewStyle> = useMemo(
		() => ({
			width: anchor.size,
			height: anchor.size,
			borderRadius: anchor.size / 2,
			overflow: 'hidden',
		}),
		[anchor.size],
	);

	const animatedStyles = useAnimatedStyle(() => {
		return {
			position: 'absolute',
			left: x.value,
			top: y.value,
		};
	}, []);

	const dragGesture = Gesture.Pan()
		.onChange(e => {
			x.value = x.value + e.changeX;
			y.value = y.value + e.changeY;
		})
		.onEnd(e => {
			x.value = e.absoluteX - e.x;
			const finalX =
				x.value > (windowWidth - anchor.size) / 2 ? windowWidth - anchor.size - gap : gap;
			x.value = withSpring(finalX, {
				stiffness: 500,
				overshootClamping: true,
			});
			y.value = e.absoluteY - e.y;
			const finalY = Math.min(Math.max(barHeight, y.value), windowHeight - anchor.size - gap);
			y.value = withSpring(finalY, {
				stiffness: 500,
				overshootClamping: true,
			});

			runOnJS(onOffsetChanged)(finalX, finalY);
		});

	return (
		<Animated.View style={[animatedStyles, styles.shadow]}>
			<GestureHandlerRootView>
				<GestureDetector gesture={dragGesture}>
					<View style={ballStyles}>{children}</View>
				</GestureDetector>
			</GestureHandlerRootView>
		</Animated.View>
	);
}

const styles = StyleSheet.create({
	shadow: {
		shadowColor: '#000',
		shadowRadius: 8,
		shadowOpacity: 0.4,
		shadowOffset: {
			width: 2,
			height: 2,
		},
	},
});

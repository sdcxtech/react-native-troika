import React, { PropsWithChildren } from 'react';
import { StyleSheet, useWindowDimensions } from 'react-native';
import { Gesture, GestureDetector, GestureHandlerRootView } from 'react-native-gesture-handler';
import Animated, {
	runOnJS,
	useAnimatedStyle,
	useSharedValue,
	withSpring,
} from 'react-native-reanimated';

import DropShadow from 'react-native-drop-shadow';

const AnimatedDropShadow = Animated.createAnimatedComponent(DropShadow);

import { BallProps } from './types';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

export default function Ball({
	anchor,
	children,
	onOffsetChanged = () => {},
}: PropsWithChildren<BallProps>) {
	const insets = useSafeAreaInsets();
	const barHeight = insets.top;
	const offsetY = insets.top;

	const { width: windowWidth, height: windowHeight } = useWindowDimensions();

	const gap = 8;

	const transf = useSharedValue({ x: 0, y: 0 });
	const x = useSharedValue(anchor.x);
	const y = useSharedValue(anchor.y);

	const shadowStyles = useAnimatedStyle(() => {
		return {
			transform: [{ translateX: transf.value.x }, { translateY: transf.value.y }],
			width: anchor.size,
			height: anchor.size,
			borderRadius: anchor.size / 2,
			overflow: 'hidden',
		};
	}, []);

	const ballStyles = useAnimatedStyle(() => {
		return {
			width: anchor.size,
			height: anchor.size,
			borderRadius: anchor.size / 2,
			overflow: 'hidden',
		};
	}, []);

	const animatedStyles = useAnimatedStyle(() => {
		return {
			position: 'absolute',
			left: x.value,
			top: y.value,
		};
	}, []);

	const dragGesture = Gesture.Pan()
		.onChange(e => {
			transf.value = { x: e.changeX + transf.value.x, y: e.changeY + transf.value.y };
		})
		.onEnd(e => {
			transf.value = { x: 0, y: 0 };
			x.value = e.absoluteX - e.x;
			const finalX =
				x.value > (windowWidth - anchor.size) / 2 ? windowWidth - anchor.size - gap : gap;
			x.value = withSpring(finalX, {
				stiffness: 500,
				overshootClamping: true,
			});
			y.value = e.absoluteY + offsetY - e.y;
			const finalY = Math.min(Math.max(barHeight, y.value), windowHeight - anchor.size - gap);
			y.value = withSpring(finalY, {
				stiffness: 500,
				overshootClamping: true,
			});

			runOnJS(onOffsetChanged)(finalX, finalY);
		});

	return (
		<Animated.View style={animatedStyles}>
			<GestureHandlerRootView>
				<GestureDetector gesture={dragGesture}>
					<AnimatedDropShadow style={[styles.shadow, shadowStyles]}>
						<Animated.View style={ballStyles}>{children}</Animated.View>
					</AnimatedDropShadow>
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

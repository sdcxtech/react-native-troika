import React, { useEffect } from 'react';
import { Pressable, StyleSheet, useWindowDimensions, View } from 'react-native';
import Animated, {
	runOnJS,
	useAnimatedStyle,
	useSharedValue,
	withTiming,
} from 'react-native-reanimated';
import DropShadow from 'react-native-drop-shadow';

import { Anchor } from './types';

const AnimatedPressable = Animated.createAnimatedComponent(Pressable);
const AnimatedDropShadow = Animated.createAnimatedComponent(DropShadow);

interface MenuProps {
	anchor: Anchor;
	menuHeight?: number;
	renderAnchor?: () => React.ReactNode;
	renderMenuContent?: (collapse: () => void) => React.ReactNode;
	onClose?: () => void;
}

export default function Menu({
	anchor,
	onClose,
	renderAnchor,
	renderMenuContent,
	menuHeight = 144,
}: MenuProps) {
	const { width: windowWidth, height: windowHeight } = useWindowDimensions();

	const menuWidth = Math.max(280, windowWidth - 120);
	const menuLeft = (windowWidth - menuWidth) / 2;
	const menuTop = Math.min(anchor.y + 16, windowHeight - menuHeight - 50);

	const x = useSharedValue(anchor.x);
	const y = useSharedValue(anchor.y);
	const width = useSharedValue(anchor.size);
	const height = useSharedValue(anchor.size);

	const alpha = useSharedValue(0);
	const radius = useSharedValue(anchor.size / 2);

	const maskAnimatedStyle = useAnimatedStyle(() => {
		return {
			opacity: alpha.value,
		};
	});

	const shadowAnimatedStyle = useAnimatedStyle(() => {
		return {
			position: 'absolute',
			left: x.value,
			top: y.value,
			width: width.value,
			height: height.value,
			borderRadius: radius.value,
			shadowColor: '#000',
			shadowRadius: 8,
			shadowOpacity: 0.15,
			shadowOffset: {
				width: 2,
				height: 2,
			},
		};
	}, []);

	const contentAnimatedStyle = useAnimatedStyle(() => {
		return {
			width: width.value,
			height: height.value,
			borderRadius: radius.value,
			backgroundColor: '#FFFFFF',
			overflow: 'hidden',
		};
	}, []);

	const anchorAnimatedStyle = useAnimatedStyle(() => {
		return {
			opacity: 1 - alpha.value,
		};
	}, []);

	const close = () => {
		onClose?.();
	};

	// 收起
	const collapse = () => {
		x.value = withTiming(anchor.x, { duration: 200 });
		y.value = withTiming(anchor.y, { duration: 200 });
		width.value = withTiming(anchor.size, { duration: 200 });
		height.value = withTiming(anchor.size, { duration: 200 });

		alpha.value = withTiming(0, { duration: 200 });
		radius.value = withTiming(anchor.size / 2, { duration: 200 }, () => {
			runOnJS(close)();
		});
	};

	// 展开
	const expand = () => {
		x.value = withTiming(menuLeft, { duration: 200 });
		y.value = withTiming(menuTop, { duration: 200 });
		width.value = withTiming(menuWidth, { duration: 200 });
		height.value = withTiming(menuHeight, { duration: 200 });

		alpha.value = withTiming(1, { duration: 200 });
		radius.value = withTiming(16, { duration: 200 });
	};

	useEffect(() => {
		expand();
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	const _renderMenuContent = () => {
		if (renderMenuContent) {
			return renderMenuContent(collapse);
		}

		return null;
	};

	const _renderAnchor = () => {
		if (renderAnchor) {
			return renderAnchor();
		}

		return null;
	};

	return (
		<View style={styles.fill}>
			<AnimatedPressable style={[styles.mask, maskAnimatedStyle]} onPress={collapse} />
			<AnimatedDropShadow style={shadowAnimatedStyle}>
				<Animated.View style={contentAnimatedStyle}>
					{_renderMenuContent()}
					<Animated.View
						style={[StyleSheet.absoluteFillObject, anchorAnimatedStyle]}
						pointerEvents="none"
					>
						{_renderAnchor()}
					</Animated.View>
				</Animated.View>
			</AnimatedDropShadow>
		</View>
	);
}

const styles = StyleSheet.create({
	fill: { flex: 1 },
	mask: {
		...StyleSheet.absoluteFillObject,
		backgroundColor: 'rgba(255, 255, 255, 0.8)',
	},

	thumb: {
		flex: 1,
		justifyContent: 'center',
		alignItems: 'center',
	},
});

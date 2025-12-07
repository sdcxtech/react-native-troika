import React, { useCallback, useRef } from 'react';
import { Animated, PixelRatio, ScrollView, StyleSheet, View } from 'react-native';
import { LoremIpsum } from '../../components/LoremIpsum';
import BottomSheet, { BottomSheetOnSlideEvent } from '@sdcx/bottom-sheet';
import { withNavigationItem } from 'hybrid-navigation';
import DropShadow from 'react-native-drop-shadow';

const HEADER_HEIGHT = 50;

const AnimatedBottomSheet = Animated.createAnimatedComponent(BottomSheet);

function BottomSheetBackdropShadow() {
	const offset = useRef(new Animated.Value(1)).current;

	const backdropStyle = {
		opacity: offset.interpolate({
			inputRange: [0, 1],
			outputRange: [1, 0],
		}),
	};

	const onSlide = useCallback(
		(event: BottomSheetOnSlideEvent) => {
			const { progress } = event.nativeEvent;
			console.info('onSlide', event.nativeEvent);
			// 在 iOS 新架构中，Animated.event 可能无法正确绑定到 DirectEventHandler
			// 所以手动更新 Animated.Value
			// 使用 setValue 直接设置值，因为事件本身已经提供了平滑的进度值
			offset.setValue(progress);
		},
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

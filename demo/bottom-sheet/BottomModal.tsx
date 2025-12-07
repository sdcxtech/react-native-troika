import BottomSheet, {
	BottomSheetState,
	BottomSheetOnSlideEvent,
	BottomSheetOnStateChangedEvent,
} from '@sdcx/bottom-sheet';
import React, { PropsWithChildren, useCallback, useEffect, useRef, useState } from 'react';
import {
	BackHandler,
	Keyboard,
	Pressable,
	StyleProp,
	StyleSheet,
	View,
	ViewStyle,
} from 'react-native';

interface BottomModalProps {
	style?: StyleProp<ViewStyle>;
	modalContentStyle?: StyleProp<ViewStyle>;
	fitToContents?: boolean;
	visible: boolean;
	onClose?: () => void;
}

export function BottomModal(props: PropsWithChildren<BottomModalProps>) {
	const {
		visible = true,
		fitToContents = false,
		onClose,
		style,
		modalContentStyle,
		children,
	} = props;
	const [bottomSheetStatus, setBottomSheetStatus] = useState<BottomSheetState>('collapsed');
	const progressRef = useRef(0);

	useEffect(() => {
		if (!visible) {
			Keyboard.dismiss();
		}
		// 保证动画
		if (visible) {
			setTimeout(() => {
				setBottomSheetStatus('expanded');
			}, 0);
		} else {
			setBottomSheetStatus('collapsed');
		}
	}, [visible]);

	useEffect(() => {
		const subscription = BackHandler.addEventListener('hardwareBackPress', () => {
			setBottomSheetStatus('collapsed');
			return true;
		});

		return () => subscription.remove();
	}, []);

	const onOutsidePress = () => {
		Keyboard.dismiss();
		setBottomSheetStatus('collapsed');
	};

	const onStateChanged = (event: BottomSheetOnStateChangedEvent) => {
		const { state } = event.nativeEvent;
		setBottomSheetStatus(state);
		if (state === 'collapsed' && progressRef.current === 1) {
			onClose?.();
		}
	};

	const onSlide = useCallback((event: BottomSheetOnSlideEvent) => {
		const { progress } = event.nativeEvent;
		progressRef.current = progress;
	}, []);

	return (
		<View style={styles.container}>
			<Pressable style={styles.overlay} onPress={onOutsidePress} />
			<BottomSheet
				fitToContents={fitToContents}
				peekHeight={0}
				draggable={false}
				state={bottomSheetStatus}
				onStateChanged={onStateChanged}
				onSlide={onSlide}
				style={style}
				contentContainerStyle={modalContentStyle}
			>
				{children}
			</BottomSheet>
		</View>
	);
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
	},
	overlay: {
		position: 'absolute',
		left: 0,
		right: 0,
		top: 0,
		bottom: 0,
	},
});

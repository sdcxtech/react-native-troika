import { useCallback } from 'react';
import { useSharedValue } from 'react-native-reanimated';
import { KeyboardPositionPayload, KeyboardStatusPayload } from '@sdcx/keyboard-insets';

export function useRekeyboard() {
	const position = useSharedValue(0);
	const height = useSharedValue(0);
	const shown = useSharedValue(false);
	const transitioning = useSharedValue(false);

	const onStatusChanged = useCallback(
		(e: KeyboardStatusPayload) => {
			'worklet';
			shown.value = e.shown;
			transitioning.value = e.transitioning;
			height.value = e.height;
		},
		[shown, transitioning, height],
	);

	const onPositionChanged = useCallback(
		(e: KeyboardPositionPayload) => {
			'worklet';
			position.value = e.position;
		},
		[position],
	);

	const keyboard = {
		position,
		height,
		shown,
		transitioning,
	};

	return { keyboard, onStatusChanged, onPositionChanged };
}

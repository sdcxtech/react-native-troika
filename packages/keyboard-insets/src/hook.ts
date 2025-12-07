import { useCallback, useState } from 'react';
import { Animated } from 'react-native';

interface KeyboardState {
	height: number;
	shown: boolean;
	transitioning: boolean;
	position: Animated.Value;
}

export function useKeyboard() {
	const [keyboard, setKeyboard] = useState<KeyboardState>({
		height: 0,
		shown: false,
		transitioning: false,
		position: new Animated.Value(0),
	});

	const onKeyboard = useCallback((state: KeyboardState) => {
		setKeyboard(state);
	}, []);

	return { keyboard, onKeyboard };
}

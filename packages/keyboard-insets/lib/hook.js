import { useCallback, useState } from 'react';
import { Animated } from 'react-native';
export function useKeyboard() {
    const [keyboard, setKeyboard] = useState({
        height: 0,
        shown: false,
        transitioning: false,
        position: new Animated.Value(0),
    });
    const onKeyboard = useCallback((state) => {
        setKeyboard(state);
    }, []);
    return { keyboard, onKeyboard };
}

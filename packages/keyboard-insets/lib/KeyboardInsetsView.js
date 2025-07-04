// import React, { useCallback, useMemo, useRef } from 'react';
// import { Animated } from 'react-native';
// import { NativeKeyboardInsetsView } from './native';
// const NativeKeyboardInsetsViewAnimated = Animated.createAnimatedComponent(NativeKeyboardInsetsView);
// export function KeyboardInsetsView(props) {
//     const { children, onKeyboard, ...rest } = props;
//     const position = useRef(new Animated.Value(0)).current;
//     const onPositionChanged = useMemo(() => Animated.event([
//         {
//             nativeEvent: {
//                 position,
//             },
//         },
//     ], {
//         useNativeDriver: true,
//     }), [position]);
//     const onStatusChanged = useCallback((event) => {
//         onKeyboard?.({ ...event.nativeEvent, position });
//     }, [position, onKeyboard]);
//     if (onKeyboard) {
//         return (<NativeKeyboardInsetsViewAnimated mode="manual" onStatusChanged={onStatusChanged} onPositionChanged={onPositionChanged} {...rest}>
//         {children}
//       </NativeKeyboardInsetsViewAnimated>);
//     }
//     return <NativeKeyboardInsetsView {...rest}>{children}</NativeKeyboardInsetsView>;
// }

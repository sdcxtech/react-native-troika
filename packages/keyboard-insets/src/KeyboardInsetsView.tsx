import React, { PropsWithChildren, useCallback, useMemo, useRef } from 'react';
import { Animated, ViewProps } from 'react-native';
import KeyboardInsetsViewNativeComponent, {
	KeyboardStatusChangedEvent,
	NativeProps,
} from './KeyboardInsetsViewNativeComponent';

const NativeKeyboardInsetsViewAnimated = Animated.createAnimatedComponent(
	KeyboardInsetsViewNativeComponent,
);

export interface KeyboardState {
	height: number;
	shown: boolean;
	transitioning: boolean;
	position: Animated.Value;
}

interface KeyboardInsetsViewProps extends Animated.AnimatedProps<ViewProps> {
	extraHeight?: number;
	explicitly?: boolean;
	onKeyboard?: (status: KeyboardState) => void;
}

export function KeyboardInsetsView(props: PropsWithChildren<KeyboardInsetsViewProps>) {
	const { children, onKeyboard, ...rest } = props;

	const position = useRef(new Animated.Value(0)).current;

	const onPositionChanged = useMemo(
		() =>
			Animated.event(
				[
					{
						nativeEvent: {
							position,
						},
					},
				],
				{
					useNativeDriver: true,
				},
			),
		[position],
	);

	const onStatusChanged = useCallback(
		(event: KeyboardStatusChangedEvent) => {
			onKeyboard?.({ ...event.nativeEvent, position });
		},
		[position, onKeyboard],
	);

	if (onKeyboard) {
		return (
			<NativeKeyboardInsetsViewAnimated
				mode="manual"
				onStatusChanged={onStatusChanged}
				onPositionChanged={onPositionChanged}
				{...rest}
			>
				{children}
			</NativeKeyboardInsetsViewAnimated>
		);
	}

	return (
		<KeyboardInsetsViewNativeComponent {...(rest as NativeProps)}>
			{children}
		</KeyboardInsetsViewNativeComponent>
	);
}

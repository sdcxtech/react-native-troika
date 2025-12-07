import React from 'react';

import {
	KeyboardPositionChangedEvent,
	KeyboardStatusChangedEvent,
	KeyboardStatusPayload,
	KeyboardPositionPayload,
	KeyboardInsetsViewNativeComponent,
} from '@sdcx/keyboard-insets';

import Reanimated, { AnimatedProps, useEvent, useHandler } from 'react-native-reanimated';
import type { ViewProps } from 'react-native';

export interface KeyboardState {
	height: number;
	shown: boolean;
	transitioning: boolean;
}

interface KeyboardInsetsViewProps extends AnimatedProps<ViewProps> {
	onPositionChanged?: (e: KeyboardPositionPayload) => void;
	onStatusChanged?: (e: KeyboardStatusPayload) => void;
}

const NativeKeyboardInsetsViewReanimated = Reanimated.createAnimatedComponent(
	KeyboardInsetsViewNativeComponent,
);

export function RekeyboardInsetsView(props: KeyboardInsetsViewProps) {
	const { children, onPositionChanged, onStatusChanged, ...rest } = props;

	const handlePositionChanged = useKeyboardPositionChangedHandler(
		{
			onPositionChanged: e => {
				'worklet';
				onPositionChanged?.(e);
			},
		},
		[onPositionChanged],
	);

	const handleStatusChanged = useKeyboardStatusChangedHandler(
		{
			onStatusChanged: e => {
				'worklet';
				onStatusChanged?.(e);
			},
		},
		[onStatusChanged],
	);

	return (
		<NativeKeyboardInsetsViewReanimated
			mode="manual"
			onStatusChanged={handleStatusChanged}
			onPositionChanged={handlePositionChanged}
			{...rest}
		>
			{children}
		</NativeKeyboardInsetsViewReanimated>
	);
}

interface CustomKeyboardPositionChangedEventData extends KeyboardPositionPayload {
	eventName: string;
}

function useKeyboardPositionChangedHandler<TContext extends Record<string, unknown>>(
	handlers: {
		onPositionChanged: (e: KeyboardPositionPayload, ctx: TContext) => void;
	},
	dependencies?: Array<unknown>,
) {
	const { context, doDependenciesDiffer } = useHandler(handlers, dependencies);

	return useEvent<KeyboardPositionChangedEvent>(
		event => {
			'worklet';
			const { onPositionChanged } = handlers;
			if (
				onPositionChanged &&
				(event as CustomKeyboardPositionChangedEventData).eventName.endsWith(
					'onPositionChanged',
				)
			) {
				onPositionChanged(event, context);
			}
		},
		['onPositionChanged'],
		doDependenciesDiffer,
	);
}

interface CustomKeyboardStatusChangedEventData extends KeyboardStatusPayload {
	eventName: string;
}

function useKeyboardStatusChangedHandler<TContext extends Record<string, unknown>>(
	handlers: {
		onStatusChanged: (e: KeyboardStatusPayload, ctx: TContext) => void;
	},
	dependencies?: Array<unknown>,
) {
	const { context, doDependenciesDiffer } = useHandler(handlers, dependencies);

	return useEvent<KeyboardStatusChangedEvent>(
		event => {
			'worklet';
			const { onStatusChanged } = handlers;
			if (
				onStatusChanged &&
				(event as CustomKeyboardStatusChangedEventData).eventName.endsWith(
					'onStatusChanged',
				)
			) {
				onStatusChanged(event, context);
			}
		},
		['onStatusChanged'],
		doDependenciesDiffer,
	);
}

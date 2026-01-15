import React, { useCallback, useRef, useState } from 'react';
import Lottie from 'lottie-react-native';
import HapticFeedback from 'react-native-haptic-feedback';
import { StyleSheet } from 'react-native';
import {
	PullToRefreshHeader,
	PullToRefreshHeaderProps,
	PullToRefreshOffsetChangedEvent,
	PullToRefreshStateChangedEvent,
	PullToRefreshState,
	PullToRefreshStateIdle,
	PullToRefreshStateRefreshing,
} from '@sdcx/pull-to-refresh';

function LottiePullToRefreshHeader(props: PullToRefreshHeaderProps) {
	const [progress, setProgress] = useState(0);
	const lottieRef = useRef<Lottie>(null);
	const stateRef = useRef<PullToRefreshState>(PullToRefreshStateIdle);
	const progressRef = useRef(0);

	const onOffsetChanged = useCallback((event: PullToRefreshOffsetChangedEvent) => {
		const offset = event.nativeEvent.offset;
		if (stateRef.current !== PullToRefreshStateRefreshing) {
			// console.log('PullToRefresh offset:', offset);
			progressRef.current = Math.min(1, offset / 50);
			setProgress(progressRef.current);
		}
	}, []);

	const onStateChanged = useCallback((event: PullToRefreshStateChangedEvent) => {
		const state = event.nativeEvent.state;
		stateRef.current = state;
		console.log('PullToRefresh state:', state);
		if (state === PullToRefreshStateIdle) {
			lottieRef.current?.pause();
		} else if (state === PullToRefreshStateRefreshing) {
			lottieRef.current?.play(progressRef.current);
		} else {
			HapticFeedback.trigger('effectClick');
		}
	}, []);

	return (
		<PullToRefreshHeader
			style={styles.header}
			{...props}
			onOffsetChanged={onOffsetChanged}
			onStateChanged={onStateChanged}
		>
			<Lottie
				ref={lottieRef}
				style={styles.spinner}
				source={require('./square-loading.json')}
				autoPlay={false}
				speed={1}
				loop
				progress={progress}
			/>
		</PullToRefreshHeader>
	);
}

const styles = StyleSheet.create({
	header: {
		alignItems: 'center',
		position: 'absolute',
		zIndex: -99,
	},
	spinner: {
		height: 50,
		width: 50,
	},
});

export { LottiePullToRefreshHeader };

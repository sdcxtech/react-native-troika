import React, { useCallback, useState } from 'react';
import { StyleSheet, Text } from 'react-native';
import {
	PullToRefreshFooterProps,
	PullToRefreshOffsetChangedEvent,
	PullToRefreshStateChangedEvent,
	PullToRefreshStateIdle,
	PullToRefreshStateRefreshing,
} from '../types';
import { PullToRefreshFooter } from '../Footer';

export default function DefaultPullToRefreshFooter(props: PullToRefreshFooterProps) {
	const { onRefresh, refreshing, noMoreData } = props;

	const [text, setText] = useState('上拉加载更多');

	const onStateChanged = useCallback((event: PullToRefreshStateChangedEvent) => {
		const state = event.nativeEvent.state;
		if (state === PullToRefreshStateIdle) {
			setText('上拉加载更多');
		} else if (state === PullToRefreshStateRefreshing) {
			setText('正在加载更多...');
		} else {
			setText('松开加载更多');
		}
	}, []);

	const onOffsetChanged = useCallback((event: PullToRefreshOffsetChangedEvent) => {
		console.log('refresh footer offset', event.nativeEvent.offset);
	}, []);

	return (
		<PullToRefreshFooter
			style={styles.container}
			manual
			onOffsetChanged={onOffsetChanged}
			onStateChanged={onStateChanged}
			onRefresh={onRefresh}
			refreshing={refreshing}
			noMoreData={noMoreData}
		>
			<Text style={styles.text}>{noMoreData ? '没有更多数据了' : text}</Text>
		</PullToRefreshFooter>
	);
}

const styles = StyleSheet.create({
	container: {
		alignItems: 'center',
		backgroundColor: 'red',
	},
	text: {
		paddingVertical: 16,
		fontSize: 17,
		color: 'white',
	},
});

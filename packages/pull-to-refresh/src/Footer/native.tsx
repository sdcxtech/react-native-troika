import React, { Component, ReactNode } from 'react';
import { findNodeHandle, NativeSyntheticEvent, UIManager, ViewProps } from 'react-native';
import {
	PullToRefreshOffsetChangedEvent,
	PullToRefreshState,
	PullToRefreshStateChangedEvent,
} from '../types';
import PullToRefreshFooterNativeComponent from './PullToRefreshFooterNativeComponent';
import { StateChangedEventPayload } from './PullToRefreshFooterNativeComponent';

interface NativePullToRefreshFooterProps extends ViewProps {
	onRefresh?: () => void;
	onStateChanged?: (event: PullToRefreshStateChangedEvent) => void;
	onOffsetChanged?: (event: PullToRefreshOffsetChangedEvent) => void;
	refreshing: boolean;
	noMoreData?: boolean;
	manual?: boolean;
}

type NativePullToRefreshFooterInstance = InstanceType<typeof PullToRefreshFooterNativeComponent>;

const PullToRefreshFooterCommands = {
	setNativeRefreshing(componentOrHandle: NativePullToRefreshFooterInstance, refreshing: boolean) {
		UIManager.dispatchViewManagerCommand(
			findNodeHandle(componentOrHandle),
			'setNativeRefreshing',
			[refreshing],
		);
	},
};
class PullToRefreshFooter extends Component<NativePullToRefreshFooterProps> {
	_nativeRef: NativePullToRefreshFooterInstance | null = null;
	_lastNativeRefreshing = false;

	_onRefresh = () => {
		this._lastNativeRefreshing = true;
		this.props?.onRefresh?.();
		this.forceUpdate();
	};

	_onStateChanged = (event: NativeSyntheticEvent<StateChangedEventPayload>) => {
		this.props?.onStateChanged?.({
			...event,
			nativeEvent: {
				state: event.nativeEvent.state as PullToRefreshState,
			},
		});
	};

	_setNativeRef = (ref: NativePullToRefreshFooterInstance) => {
		this._nativeRef = ref;
	};

	componentDidMount() {
		this._lastNativeRefreshing = this.props.refreshing;
	}

	componentDidUpdate(prevProps: NativePullToRefreshFooterProps) {
		if (this.props.refreshing !== prevProps.refreshing) {
			this._lastNativeRefreshing = this.props.refreshing;
		} else if (this.props.refreshing !== this._lastNativeRefreshing && this._nativeRef) {
			PullToRefreshFooterCommands.setNativeRefreshing(this._nativeRef, this.props.refreshing);
			this._lastNativeRefreshing = this.props.refreshing;
		}
	}

	render(): ReactNode {
		return (
			<PullToRefreshFooterNativeComponent
				{...this.props}
				ref={this._setNativeRef}
				onRefresh={this._onRefresh}
				onStateChanged={this._onStateChanged}
			/>
		);
	}
}

export { PullToRefreshFooter };

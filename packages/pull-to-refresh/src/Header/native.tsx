import React, { Component, ReactNode } from 'react';
import { findNodeHandle, NativeSyntheticEvent, UIManager, ViewProps } from 'react-native';
import {
	PullToRefreshOffsetChangedEvent,
	PullToRefreshState,
	PullToRefreshStateChangedEvent,
} from '../types';
import PullToRefreshHeaderNativeComponent, {
	StateChangedEventPayload,
} from './PullToRefreshHeaderNativeComponent';

interface NativePullToRefreshHeaderProps extends ViewProps {
	onRefresh?: () => void;
	onStateChanged?: (event: PullToRefreshStateChangedEvent) => void;
	onOffsetChanged?: (event: PullToRefreshOffsetChangedEvent) => void;
	refreshing: boolean;
	progressViewOffset?: number | undefined;
}

type NativePullToRefreshHeaderInstance = InstanceType<typeof PullToRefreshHeaderNativeComponent>;

const PullToRefreshHeaderCommands = {
	setNativeRefreshing(componentOrHandle: NativePullToRefreshHeaderInstance, refreshing: boolean) {
		UIManager.dispatchViewManagerCommand(
			findNodeHandle(componentOrHandle),
			'setNativeRefreshing',
			[refreshing],
		);
	},
};
class PullToRefreshHeader extends Component<NativePullToRefreshHeaderProps> {
	_nativeRef: NativePullToRefreshHeaderInstance | null = null;
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

	_setNativeRef = (ref: NativePullToRefreshHeaderInstance) => {
		this._nativeRef = ref;
	};

	componentDidMount() {
		this._lastNativeRefreshing = this.props.refreshing;
	}

	componentDidUpdate(prevProps: NativePullToRefreshHeaderProps) {
		if (this.props.refreshing !== prevProps.refreshing) {
			this._lastNativeRefreshing = this.props.refreshing;
		} else if (this.props.refreshing !== this._lastNativeRefreshing && this._nativeRef) {
			console.warn('setNativeRefreshing', this.props.refreshing);
			PullToRefreshHeaderCommands.setNativeRefreshing(this._nativeRef, this.props.refreshing);
			this._lastNativeRefreshing = this.props.refreshing;
		}
	}

	render(): ReactNode {
		return (
			<PullToRefreshHeaderNativeComponent
				{...this.props}
				ref={this._setNativeRef}
				onRefresh={this._onRefresh}
				onStateChanged={this._onStateChanged}
				progressViewOffset={this.props.progressViewOffset}
			/>
		);
	}
}

export { PullToRefreshHeader };

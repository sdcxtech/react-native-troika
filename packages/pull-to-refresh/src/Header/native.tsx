import React, {Component, ReactNode} from 'react';
import {findNodeHandle, requireNativeComponent, UIManager, ViewProps} from 'react-native';
import {PullToRefreshOffsetChangedEvent, PullToRefreshStateChangedEvent} from '../types';

interface NativePullToRefreshHeaderProps extends ViewProps {
  onRefresh?: () => void;
  onStateChanged?: (event: PullToRefreshStateChangedEvent) => void;
  onOffsetChanged?: (event: PullToRefreshOffsetChangedEvent) => void;
  refreshing: boolean;
}

const NativePullToRefreshHeader =
  requireNativeComponent<NativePullToRefreshHeaderProps>('RefreshHeader');

type NativePullToRefreshHeaderInstance = InstanceType<typeof NativePullToRefreshHeader>;

const PullToRefreshHeaderCommands = {
  setNativeRefreshing(componentOrHandle: NativePullToRefreshHeaderInstance, refreshing: boolean) {
    UIManager.dispatchViewManagerCommand(findNodeHandle(componentOrHandle), 'setNativeRefreshing', [
      refreshing,
    ]);
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
      PullToRefreshHeaderCommands.setNativeRefreshing(this._nativeRef, this.props.refreshing);
      this._lastNativeRefreshing = this.props.refreshing;
    }
  }

  render(): ReactNode {
    return (
      <NativePullToRefreshHeader
        {...this.props}
        ref={this._setNativeRef}
        onRefresh={this._onRefresh}
      />
    );
  }
}

export {PullToRefreshHeader};

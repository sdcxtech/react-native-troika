import React, {useCallback, useState} from 'react';
import {StyleSheet, Text} from 'react-native';
import {
  PullToRefreshHeaderProps,
  PullToRefreshOffsetChangedEvent,
  PullToRefreshStateChangedEvent,
  PullToRefreshStateIdle,
  PullToRefreshStateRefreshing,
} from '../types';
import {PullToRefreshHeader} from './native';

export function DefaultPullToRefreshHeader(props: PullToRefreshHeaderProps) {
  const {onRefresh, refreshing} = props;

  const [text, setText] = useState('下拉刷新');

  const onStateChanged = useCallback((event: PullToRefreshStateChangedEvent) => {
    const state = event.nativeEvent.state;
    if (state === PullToRefreshStateIdle) {
      setText('下拉刷新');
    } else if (state === PullToRefreshStateRefreshing) {
      setText('正在刷新...');
    } else {
      setText('松开刷新');
    }
  }, []);

  const onOffsetChanged = useCallback((event: PullToRefreshOffsetChangedEvent) => {
    console.log('refresh header offset', event.nativeEvent.offset);
  }, []);

  return (
    <PullToRefreshHeader
      style={styles.container}
      onOffsetChanged={onOffsetChanged}
      onStateChanged={onStateChanged}
      onRefresh={onRefresh}
      refreshing={refreshing}>
      <Text style={styles.text}>{text}</Text>
    </PullToRefreshHeader>
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

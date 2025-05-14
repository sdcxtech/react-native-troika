import React from 'react';
import {Platform, RefreshControlProps} from 'react-native';
import {PullToRefresh} from './PullToRefresh';

export default function PullToRefreshControl(props: RefreshControlProps) {
  if (Platform.OS === 'android') {
    return <PullToRefresh {...props} />;
  }
  // @ts-ignore
  return <PullToRefresh.DefaultHeader {...props} />;
}

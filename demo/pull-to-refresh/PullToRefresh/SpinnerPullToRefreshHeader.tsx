import React, {useCallback, useRef, useState} from 'react';
import HapticFeedback from 'react-native-haptic-feedback';
import {StyleSheet} from 'react-native';
import {
  PullToRefreshHeader,
  PullToRefreshHeaderProps,
  PullToRefreshStateChangedEvent,
  PullToRefreshState,
  PullToRefreshStateIdle,
  PullToRefreshStateRefreshing,
} from '@sdcx/pull-to-refresh';
import ActivityIndicator from '@sdcx/activity-indicator';

function SpinnerPullToRefreshHeader(props: PullToRefreshHeaderProps) {
  const stateRef = useRef<PullToRefreshState>(PullToRefreshStateIdle);
  const [animating, setAnimating] = useState(false);

  const onStateChanged = useCallback((event: PullToRefreshStateChangedEvent) => {
    const state = event.nativeEvent.state;
    stateRef.current = state;
    if (state === PullToRefreshStateIdle) {
      setAnimating(false);
    } else if (state === PullToRefreshStateRefreshing) {
      setAnimating(true);
    } else {
      HapticFeedback.trigger('effectClick');
    }
  }, []);

  return (
    <PullToRefreshHeader style={styles.header} {...props} onStateChanged={onStateChanged}>
      <ActivityIndicator animating={animating} size="large" style={styles.spinner} />
    </PullToRefreshHeader>
  );
}

const styles = StyleSheet.create({
  header: {
    alignItems: 'center',
    justifyContent: 'center',
    height: 50,
  },
  spinner: {
    transform: [{scale: 0.75}],
  },
});

export {SpinnerPullToRefreshHeader};

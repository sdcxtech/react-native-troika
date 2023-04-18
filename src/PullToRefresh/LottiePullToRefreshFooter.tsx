import React, { useCallback, useRef } from 'react'
import Lottie from 'lottie-react-native'
import { StyleSheet, Text } from 'react-native'
import HapticFeedback from 'react-native-haptic-feedback'
import {
  PullToRefreshFooter,
  PullToRefreshFooterProps,
  PullToRefreshStateChangedEvent,
  RefreshStateIdle,
  RefreshStateRefreshing,
} from '@sdcx/pull-to-refresh'

function LottiePullToRefreshFooter(props: PullToRefreshFooterProps) {
  const { onRefresh, refreshing, noMoreData } = props
  const lottieRef = useRef<Lottie>(null)

  const onStateChanged = useCallback((event: PullToRefreshStateChangedEvent) => {
    const state = event.nativeEvent.state
    if (state === RefreshStateIdle) {
      lottieRef.current?.pause()
      lottieRef.current?.reset()
    } else if (state === RefreshStateRefreshing) {
      lottieRef.current?.play()
    } else {
      HapticFeedback.trigger('impactLight')
    }
  }, [])

  return (
    <PullToRefreshFooter
      style={styles.footer}
      manual
      onStateChanged={onStateChanged}
      onRefresh={onRefresh}
      refreshing={refreshing}
      noMoreData={noMoreData}>
      {noMoreData ? (
        <Text style={styles.text}>没有更多数据了</Text>
      ) : (
        <Lottie
          ref={lottieRef}
          style={{ height: 44 }}
          source={require('./google-loading.json')}
          autoPlay={false}
          speed={1.5}
          cacheStrategy={'strong'}
          loop
        />
      )}
    </PullToRefreshFooter>
  )
}

const styles = StyleSheet.create({
  footer: {
    alignItems: 'center',
  },
  text: {
    paddingVertical: 8,
    fontSize: 13,
    color: '#999999',
  },
})

export { LottiePullToRefreshFooter }

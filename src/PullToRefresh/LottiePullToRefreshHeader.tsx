import React, { useCallback, useRef, useState } from 'react'
import Lottie from 'lottie-react-native'
import HapticFeedback from 'react-native-haptic-feedback'
import { StyleSheet } from 'react-native'
import {
  PullToRefreshHeader,
  PullToRefreshHeaderProps,
  PullToRefreshOffsetChangedEvent,
  PullToRefreshStateChangedEvent,
  PullToRefreshState,
  PullToRefreshStateIdle,
  PullToRefreshStateRefreshing,
} from '@sdcx/pull-to-refresh'

function LottiePullToRefreshHeader(props: PullToRefreshHeaderProps) {
  const [progress, setProgress] = useState(0)
  const lottieRef = useRef<Lottie>(null)
  const stateRef = useRef<PullToRefreshState>(PullToRefreshStateIdle)

  const onOffsetChanged = useCallback((event: PullToRefreshOffsetChangedEvent) => {
    const offset = event.nativeEvent.offset
    if (stateRef.current !== PullToRefreshStateRefreshing) {
      setProgress(Math.min(1, offset / 50))
    }
  }, [])

  const onStateChanged = useCallback(
    (event: PullToRefreshStateChangedEvent) => {
      const state = event.nativeEvent.state
      stateRef.current = state
      if (state === PullToRefreshStateIdle) {
        lottieRef.current?.reset()
        setProgress(0)
      } else if (state === PullToRefreshStateRefreshing) {
        lottieRef.current?.play(progress)
      } else {
        HapticFeedback.trigger('impactLight')
      }
    },
    [progress],
  )

  return (
    <PullToRefreshHeader
      style={styles.header}
      {...props}
      onOffsetChanged={onOffsetChanged}
      onStateChanged={onStateChanged}>
      <Lottie
        ref={lottieRef}
        style={{ height: 50 }}
        source={require('./square-loading.json')}
        autoPlay={false}
        speed={1}
        cacheStrategy={'strong'}
        loop
        progress={progress}
      />
    </PullToRefreshHeader>
  )
}

const styles = StyleSheet.create({
  header: {
    alignItems: 'center',
  },
})

export { LottiePullToRefreshHeader }

import React, { useCallback, useRef, useState } from 'react'
import Lottie from 'lottie-react-native'
import HapticFeedback from 'react-native-haptic-feedback'

import RefreshHeader, {
  RefreshState,
  RefreshStateIdle,
  RefreshStateRefreshing,
} from '../RefreshHeader'
import { StyleSheet } from 'react-native'

interface LottieRefreshHeaderProps {
  onRefresh?: () => void
  refreshing: boolean
}

function LottieRefreshHeader(props: LottieRefreshHeaderProps) {
  const { onRefresh, refreshing } = props

  const [progress, setProgress] = useState(0)
  const lottieRef = useRef<Lottie>(null)
  const stateRef = useRef<RefreshState>(RefreshStateIdle)

  const onOffsetChanged = useCallback((offset: number) => {
    if (stateRef.current !== RefreshStateRefreshing) {
      setProgress(Math.min(1, offset / 50))
    }
  }, [])

  const onStateChanged = useCallback(
    (state: RefreshState) => {
      stateRef.current = state
      if (state === RefreshStateIdle) {
        lottieRef.current?.pause()
        setProgress(0)
      } else if (state === RefreshStateRefreshing) {
        lottieRef.current?.play(progress)
      } else {
        HapticFeedback.trigger('impactLight')
      }
    },
    [progress],
  )

  return (
    <RefreshHeader
      style={styles.header}
      onRefresh={onRefresh}
      refreshing={refreshing}
      onOffsetChanged={onOffsetChanged}
      onStateChanged={onStateChanged}>
      <Lottie
        ref={lottieRef}
        style={{ height: 50 }}
        source={require('./car.json')}
        autoPlay={false}
        speed={1}
        cacheStrategy={'strong'}
        loop
        progress={progress}
      />
    </RefreshHeader>
  )
}

const styles = StyleSheet.create({
  header: {
    alignItems: 'center',
  },
})

export { LottieRefreshHeader }

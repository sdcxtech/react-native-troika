import React, { useCallback, useRef } from 'react'
import Lottie from 'lottie-react-native'
import RefreshFooter, {
  RefreshState,
  RefreshStateIdle,
  RefreshStateRefreshing,
} from '../RefreshFooter'
import { StyleSheet, Text } from 'react-native'
import HapticFeedback from 'react-native-haptic-feedback'

interface LottieRefreshFooterProps {
  onRefresh?: () => void
  refreshing: boolean
  noMoreData?: boolean
}

function LottieRefreshFooter(props: LottieRefreshFooterProps) {
  const { onRefresh, refreshing, noMoreData } = props
  const lottieRef = useRef<Lottie>(null)

  const onStateChanged = useCallback((state: RefreshState) => {
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
    <RefreshFooter
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
    </RefreshFooter>
  )
}

const styles = StyleSheet.create({
  footer: {
    alignItems: 'center',
  },
  text: {
    paddingVertical: 8,
    fontSize: 17,
    color: '#222222',
  },
})

export { LottieRefreshFooter }

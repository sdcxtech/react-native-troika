import React from 'react'
import { StyleSheet, Animated } from 'react-native'
import { useAnimatedNavbar } from './hooks/useAnimatedNavbar'

export type AnimatedNavbarProps = {
  scroll: Animated.Value
  OverflowHeaderComponent?: JSX.Element
  TopNavbarComponent?: JSX.Element
  imageHeight: number
  headerHeight: number
  statusBarHeight: number
}

const AnimatedNavbar = ({
  scroll,
  imageHeight,
  OverflowHeaderComponent,
  TopNavbarComponent,
  headerHeight,
  statusBarHeight,
}: AnimatedNavbarProps) => {
  const [headerOpacity, overflowHeaderOpacity] = useAnimatedNavbar(
    scroll,
    imageHeight,
    headerHeight,
  )

  return (
    <>
      <Animated.View
        style={[
          styles.container,
          styles.header,
          {
            paddingTop: statusBarHeight,
            zIndex: headerOpacity,
            height: headerHeight,
            opacity: headerOpacity,
          },
        ]}>
        {TopNavbarComponent}
      </Animated.View>
      <Animated.View
        style={[
          styles.container,
          styles.overflowHeader,
          {
            paddingTop: statusBarHeight,
            zIndex: overflowHeaderOpacity,
            height: headerHeight,
            opacity: overflowHeaderOpacity,
          },
        ]}>
        {OverflowHeaderComponent}
      </Animated.View>
    </>
  )
}

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    elevation: 2,
    top: 0,
    width: '100%',
    backgroundColor: 'white',
    alignItems: 'center',
    justifyContent: 'center',
  },
  header: {
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderBottomColor: '#a4a4a4',
  },
  overflowHeader: {
    backgroundColor: 'transparent',
  },
})

export default AnimatedNavbar

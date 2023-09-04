import React, { useEffect, useRef } from 'react'
import { Animated, AppRegistry, StyleSheet, View } from 'react-native'
import { Overlay, OverlayProps } from '@sdcx/overlay'
import Title from './Title'
import Message from './Message'
import Button from './Button'

interface AlertButton {
  onPress: () => void
  text: string
}

interface AlertProps {
  title: string
  message: string
  buttons: AlertButton[]
}

function ModalBase({ id, passThroughTouches }: OverlayProps) {
  const opacity = useRef(new Animated.Value(0))

  useEffect(() => {
    Animated.timing(opacity.current, {
      useNativeDriver: true,
      duration: 150,
      toValue: 1,
    }).start()
  }, [])

  const props = propsMap.get(id)
  const { title, message, buttons } = props!

  const marginLeft = 12

  return (
    <View style={styles.container} pointerEvents={passThroughTouches ? 'box-none' : 'auto'}>
      <Animated.View style={[styles.mask, StyleSheet.absoluteFill, { opacity: opacity.current }]} />
      <View style={styles.box}>
        <Title title={title} />
        <Message message={message} />
        <View style={styles.ops}>
          {buttons.map((button, index) => (
            <Button
              key={index}
              text={button.text}
              onPress={button.onPress}
              style={index !== 0 ? { marginLeft } : undefined}
            />
          ))}
        </View>
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  mask: {
    backgroundColor: '#00000077',
  },
  box: {
    marginHorizontal: 32,
    paddingVertical: 12,
    backgroundColor: '#FFFFFF',
    borderRadius: 8,
  },
  ops: {
    flexDirection: 'row',
    alignContent: 'space-between',
    marginBottom: 6,
    marginTop: 22,
    marginHorizontal: 12,
  },
})

function registerIfNeeded() {
  if (AppRegistry.getAppKeys().includes('__overlay_alert__')) {
    return
  }
  AppRegistry.registerComponent('__overlay_alert__', () => ModalBase)
}

let key = 0

function showInternal() {
  registerIfNeeded()
  key += 1
  Overlay.show('__overlay_alert__', { passThroughTouches: false, id: key })
  return key
}

function hideInternal(key: number) {
  propsMap.delete(key)
  Overlay.hide('__overlay_alert__', key)
}

const propsMap = new Map<number, AlertProps>()

const Alert = {
  alert(title: string, message: string, buttons: AlertButton[]) {
    const key = showInternal()
    const params = {
      title,
      message,
      buttons: buttons.map(button => ({
        ...button,
        onPress: () => {
          hideInternal(key)
          button.onPress()
        },
      })),
    }
    propsMap.set(key, params)
  },
}

export default Alert

import React from 'react'
import { TextInput, Keyboard, Animated } from 'react-native'
import { KeyboardState } from '@sdcx/keyboard-insets'
import { Driver, DriverState } from './Driver'

export class KeyboardDriver implements Driver {
  constructor(private inputRef: React.RefObject<TextInput>) {}
  // 输入框距屏幕底部的距离
  private senderBottom = 0
  private y = 0
  private position = new Animated.Value(0)

  name = 'keyboard'
  shown = false
  height = 0

  show = () => {
    this.inputRef.current?.focus()
  }

  hide = () => {
    Keyboard.dismiss()
  }

  toggle = () => {
    this.shown ? this.hide() : this.show()
  }

  createCallback = (state: DriverState) => {
    return (keyboard: KeyboardState) => {
      const { shown, height, position } = keyboard

      const { bottom, driver, setDriver, setTranslateY } = state
      const heightChanged = height !== this.height

      this.height = height
      this.position = position
      this.senderBottom = bottom

      if (shown && (!this.shown || heightChanged)) {
        this.shown = true
        if (driver && driver !== this) {
          // 记录主界面当前位置
          this.y = driver.shown ? driver.height : 0
          // 隐藏前一个 driver
          driver.hide({ bottom, driver: this, setDriver, setTranslateY })
        }
        setDriver(this)
        setTranslateY(this.translateY)
      }

      if (!shown && this.shown) {
        this.shown = false
        this.y = 0
        if (driver === this) {
          setDriver(undefined)
          setTranslateY(this.translateY)
        }
      }
    }
  }

  private get translateY() {
    const extraHeight = this.senderBottom
    console.log(this.name, 'height', this.height, 'y', this.y, 'extraHeight', extraHeight)

    if (!this.shown || this.y === 0) {
      return this.position.interpolate({
        inputRange: [extraHeight, this.height],
        outputRange: [0, extraHeight - this.height],
        extrapolate: 'clamp',
      }) as Animated.Value
    } else {
      return this.position.interpolate({
        inputRange: [0, this.height],
        outputRange: [extraHeight - this.y, extraHeight - this.height],
        extrapolate: 'clamp',
      }) as Animated.Value
    }
  }
}

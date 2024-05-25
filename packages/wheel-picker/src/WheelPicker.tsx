import React, { useCallback } from 'react'
import { requireNativeComponent, StyleProp, TextStyle, ViewProps, StyleSheet } from 'react-native'
import { PickerItem } from './typing'

interface WheelPickerNativeProps extends ViewProps {
  onItemSelected: (event: Event) => void
  selectedIndex: number
  items: string[]
  fontSize: number
  itemHeight: number
  textColorCenter?: string
  textColorOut?: string
}

const WheelPickerNative = requireNativeComponent<WheelPickerNativeProps>('WheelPicker')

interface Event {
  nativeEvent: {
    selectedIndex: number
  }
}

interface WheelPickerProps<T> {
  onValueChange?: (value: T, index: number) => void
  selectedValue: T
  items: PickerItem<T>[]
  style?: StyleProp<TextStyle>
  itemStyle?: StyleProp<TextStyle>
}

function WheelPicker<T>({ selectedValue, onValueChange, items = [], style, itemStyle = {} }: WheelPickerProps<T>) {
  const handleItemSelected = useCallback(
    (event: Event) => {
      const selectedIndex = event.nativeEvent.selectedIndex
      if (onValueChange && items.length > selectedIndex) {
        onValueChange(items[selectedIndex].value, selectedIndex)
      }
    },
    [onValueChange, items],
  )

  const selectedIndex = items.findIndex(v => v.value === selectedValue)
  const s = StyleSheet.flatten(itemStyle)
  const lineHeight = s.lineHeight || 36
  const itemHeight = s.height && typeof s.height === 'number' ? s.height : lineHeight
  const fontSize = s.fontSize ?? 14
  const fontColor = s.color && typeof s.color === 'string' ? s.color : undefined
  const height = (itemHeight * 16) / Math.PI

  return (
    <WheelPickerNative
      style={[{ height }, style]}
      onItemSelected={handleItemSelected}
      selectedIndex={selectedIndex === -1 ? 0 : selectedIndex}
      items={items.map(item => item.label)}
      itemHeight={Math.max(itemHeight, lineHeight)}
      fontSize={fontSize}
      textColorCenter={fontColor}
    />
  )
}

export default WheelPicker

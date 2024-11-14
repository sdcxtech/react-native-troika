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
  const _style = StyleSheet.flatten(style)
  const _itemStyle = StyleSheet.flatten(itemStyle)
  const lineHeight = _itemStyle.lineHeight || 36
  const itemHeight = _itemStyle.height && typeof _itemStyle.height === 'number' ? _itemStyle.height : lineHeight
  const fontSize = _itemStyle.fontSize ?? 14
  const colorCenter = _itemStyle?.color && typeof _itemStyle.color === 'string' ? _itemStyle.color : undefined
  const colorOut = _style?.color && typeof _style.color === 'string' ? _style.color : colorCenter
  const height = (itemHeight * 16) / Math.PI

  return (
    <WheelPickerNative
      style={[{ height }, _style]}
      onItemSelected={handleItemSelected}
      selectedIndex={selectedIndex === -1 ? 0 : selectedIndex}
      items={items.map(item => item.label)}
      itemHeight={Math.max(itemHeight, lineHeight)}
      fontSize={fontSize}
      textColorCenter={colorCenter}
      textColorOut={colorOut}
    />
  )
}

export default WheelPicker

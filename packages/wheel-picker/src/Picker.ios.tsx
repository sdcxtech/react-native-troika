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

function PickerIOS<T>({ selectedValue, onValueChange, items = [], style, itemStyle = {} }: WheelPickerProps<T>) {
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
  const itemHeight = s.height && typeof s.height === 'number' ? s.height : 32
  const fontSize = s.fontSize ?? 14
  const color = s.color && typeof s.color === 'string' ? s.color : undefined

  return (
    <WheelPickerNative
      style={style}
      onItemSelected={handleItemSelected}
      selectedIndex={selectedIndex === -1 ? 0 : selectedIndex}
      items={items.map(item => item.label)}
      itemHeight={itemHeight}
      fontSize={fontSize}
      textColorCenter={color}
    />
  )
}

export default PickerIOS

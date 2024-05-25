import React, { useState } from 'react'
import { StyleSheet, View } from 'react-native'
import Picker from '@sdcx/wheel-picker'
import TimePicker from './TimePicker'
import { withNavigationItem } from 'hybrid-navigation'

const items = [
  { value: '1', label: '福田区' },
  { value: '2', label: '南山区' },
  { value: '3', label: '龙华区' },
  { value: '4', label: '罗湖区' },
  { value: '5', label: '宝安区' },
  { value: '6', label: '坪山区' },
]

export default withNavigationItem({
  titleItem: {
    title: 'WheelPicker',
  },
})(WheelPicker)

function WheelPicker() {
  const [selectedValue, setSelectedValue] = useState('3')

  const handleValueChange = (itemValue: string) => {
    console.log('handleValueChange', itemValue)
    console.info('------------------------------------------')
    setSelectedValue(itemValue)
  }

  return (
    <View style={styles.container}>
      <Picker
        style={styles.picker}
        items={items}
        itemStyle={{ height: 40 }}
        selectedValue={selectedValue}
        onValueChange={handleValueChange}
      />

      <TimePicker style={styles.time} />
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  picker: {
    width: 200,
    height: 180,
  },
  time: {
    height: 224,
    width: 200,
  },
})

import React, { useState } from 'react'
import { StyleSheet, View } from 'react-native'
import TimePicker from './TimePicker'
import { withNavigationItem } from 'hybrid-navigation'
import CityPicker from './CityPicker'

export default withNavigationItem({
  titleItem: {
    title: 'WheelPicker',
  },
})(WheelPicker)

function WheelPicker() {
  const [citycode, setCitycode] = useState('15')

  return (
    <View style={styles.container}>
      <CityPicker citycode={citycode} onCitycodeChange={setCitycode} style={styles.region} />
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
  region: {
    width: 300,
    height: 224,
  },
  time: {
    width: 200,
    height: 224,
  },
})

import React from 'react'
import { View, StyleSheet } from 'react-native'
import { withNavigationItem } from 'hybrid-navigation'
import ActivityIndicator from '@sdcx/activity-indicator'

function ActivityIndicatorScreen() {
  return (
    <View style={styles.container}>
      <ActivityIndicator />
      <ActivityIndicator size="large" />
      <ActivityIndicator size="small" color="#0000ff" />
      <ActivityIndicator size="large" color="#00ff00" animating={false} />
    </View>
  )
}

export default withNavigationItem({
  titleItem: {
    title: '菊花 Loading',
  },
})(ActivityIndicatorScreen)

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
    paddingHorizontal: 80,
  },
})

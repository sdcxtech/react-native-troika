import React from 'react'
import { ActivityIndicator as ActivityIndicatorIOS, ActivityIndicatorProps, Platform } from 'react-native'
import ActivityIndicatorAndroid from './ActivityIndicatorAndroid'

const ActivityIndicator = (props: ActivityIndicatorProps) => {
  if (Platform.OS === 'ios') {
    return <ActivityIndicatorIOS hidesWhenStopped={false} {...props} />
  } else {
    return <ActivityIndicatorAndroid {...props} />
  }
}

export default ActivityIndicator

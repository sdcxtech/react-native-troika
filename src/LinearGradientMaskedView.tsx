import React, { PropsWithChildren } from 'react'
import MaskedView from '@react-native-community/masked-view'
import LinearGradient from 'react-native-linear-gradient'
import { StyleProp, ViewStyle } from 'react-native'

interface LinearGradientMaskedViewProps {
  maskStyle?: StyleProp<ViewStyle>
}

export default function LinearGradientMaskedView({
  maskStyle,
  children,
}: PropsWithChildren<LinearGradientMaskedViewProps>) {
  return (
    <MaskedView
      style={[{ flex: 1, flexDirection: 'row' }, maskStyle]}
      maskElement={
        <LinearGradient
          style={{ flex: 1, height: '100%' }}
          colors={['rgba(255, 255, 255, 1)', 'rgba(255, 255, 255, 0)']}
          locations={[0.5, 1.0]}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 0 }}
        />
      }>
      {children}
    </MaskedView>
  )
}

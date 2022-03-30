import React from 'react'
import { Pressable, Text } from 'react-native'

export type PressType = 'show' | 'hide' | 'webview' | 'scrollview' | 'flatlist' | 'refresh'
export function MenuBtn({ onPress, isActive }: { onPress: (type: PressType) => void; isActive: boolean }) {
  if (isActive) {
    return (
      <Pressable
        onPress={() => {
          onPress('hide')
        }}>
        {['hide', 'nested', 'webview', 'scrollview', 'flatlist', 'refresh'].map((item, index) => {
          return <FloatingBtn key={item} title={item} onPress={() => onPress(item as PressType)} offset={80 * index} />
        })}
      </Pressable>
    )
  }

  return <FloatingBtn title="menu" onPress={() => onPress('show')} />
}

function FloatingBtn({
  onPress,
  title = 'refresh',
  offset = 0,
}: {
  onPress: () => void
  title?: string
  offset?: number
}) {
  return (
    <Pressable
      style={({ pressed }) => ({
        position: 'absolute',
        bottom: 100 + offset,
        right: 20,
        borderRadius: 200,
        height: 70,
        width: 70,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'blue',
        opacity: pressed ? 0.5 : 1,
        zIndex: 100,
        elevation: 50,
      })}
      onPress={onPress}>
      <Text style={{ color: 'white' }}>{title}</Text>
    </Pressable>
  )
}

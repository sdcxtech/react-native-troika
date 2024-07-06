import React from 'react'
import { StyleSheet, View } from 'react-native'
import PrimaryButton from '../../components/PrimaryButton'

export default function Page() {
  const onPress = () => {
    console.log('click test')
  }
  return (
    <View style={styles.container}>
      <View style={styles.card}>
        <PrimaryButton style={styles.button} onPress={onPress} text="测试" />
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    width: '100%',
    height: '100%',
    justifyContent: 'center',
    zIndex: 1,
  },
  card: {
    marginHorizontal: 32,
    height: 400,
    backgroundColor: '#FFFFFF',
    borderRadius: 20,
    justifyContent: 'space-around',
  },
  button: {
    marginHorizontal: 32,
  },
})

import React, { useState } from 'react'
import { View, StyleSheet, Button, Text } from 'react-native'
import { withNavigationItem } from 'hybrid-navigation'
import Alert from './Alert'

function AlertScreen() {
  const [result, setResult] = useState('')

  return (
    <View style={styles.container}>
      <Button
        title="Show Alert"
        onPress={() => {
          Alert.alert('Hello World!', '你好，世界！', [
            {
              text: 'Cancel',
              onPress: () => {
                setResult('Cancel')
              },
            },
            {
              text: 'OK',
              onPress: () => {
                setResult('OK')
              },
            },
          ])
        }}
      />
      <Text style={styles.result}>{result}</Text>
    </View>
  )
}

export default withNavigationItem({
  titleItem: {
    title: 'Alert',
  },
})(AlertScreen)

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'stretch',
    paddingTop: 16,
    paddingLeft: 32,
    paddingRight: 32,
  },
  text: {
    backgroundColor: 'transparent',
    fontSize: 17,
    alignSelf: 'center',
  },
  result: {
    color: '#222222',
    fontSize: 17,
    marginTop: 16,
  },
})

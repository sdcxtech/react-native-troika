import React from 'react'
import { Text, StyleSheet } from 'react-native'

const ContactHeader = () => {
  return <Text style={styles.headerTitle}>Android 的 SectionList 无 sticky header</Text>
}

export default ContactHeader

const styles = StyleSheet.create({
  headerTitle: {
    backgroundColor: 'white',
    paddingHorizontal: 10,
    paddingVertical: 20,
    fontSize: 17,
    fontWeight: 'bold',
  },
})

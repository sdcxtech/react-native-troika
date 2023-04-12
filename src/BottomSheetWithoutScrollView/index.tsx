import React from 'react'
import { ScrollView, StyleSheet, View } from 'react-native'
import { LoremIpsum } from '../components/LoremIpsum'
import BottomSheet from '../BottomSheet'
import { withNavigationItem } from 'hybrid-navigation'

const HEADER_HEIGTH = 50

function BottomSheetWithoutScrollView() {
  return (
    <View style={styles.container}>
      <ScrollView>
        <LoremIpsum />
        <LoremIpsum />
        <LoremIpsum />
      </ScrollView>
      <BottomSheet style={styles.bottomSheet}>
        <View style={styles.header} />
        <LoremIpsum words={200} />
      </BottomSheet>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#eef',
  },
  header: {
    height: HEADER_HEIGTH,
    backgroundColor: 'coral',
  },
  bottomSheet: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: '#ff9f7A',
  },
})

export default withNavigationItem({
  titleItem: {
    title: 'BottomSheet without ScrollView',
  },
})(BottomSheetWithoutScrollView)

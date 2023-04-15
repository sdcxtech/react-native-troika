import React from 'react'
import { ScrollView, StyleSheet, View } from 'react-native'
import { LoremIpsum } from '../components/LoremIpsum'
import BottomSheet from '../BottomSheet'
import { withNavigationItem } from 'hybrid-navigation'
import Contacts from '../components/contacts/Contacts'

const HEADER_HEIGTH = 50

function BottomSheetFlashList() {
  return (
    <View style={styles.container}>
      <ScrollView>
        <LoremIpsum />
        <LoremIpsum />
        <LoremIpsum />
      </ScrollView>
      <BottomSheet onSlide={e => console.log(e.nativeEvent)} style={styles.bottomSheet}>
        <View style={styles.header} />
        <Contacts />
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
    top: 80,
    backgroundColor: '#ff9f7A',
  },
})

export default withNavigationItem({
  titleItem: {
    title: 'BottomSheet + FlashList',
  },
})(BottomSheetFlashList)

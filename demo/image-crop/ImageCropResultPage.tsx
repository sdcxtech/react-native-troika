import React from 'react'
import { NavigationProps, withNavigationItem } from 'hybrid-navigation'
import { StyleSheet, View } from 'react-native'
import FastImage from 'react-native-fast-image'

interface Props extends NavigationProps {
  fileUri: string
}

function ImageCropResultPage({ fileUri }: Props) {
  return (
    <View style={styles.container}>
      <FastImage
        style={styles.image}
        source={{
          uri: fileUri,
          priority: FastImage.priority.normal,
        }}
        resizeMode={FastImage.resizeMode.contain}
      />
    </View>
  )
}

export default withNavigationItem({ titleItem: { title: 'CropResultPage' } })(ImageCropResultPage)

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'stretch',
    backgroundColor: '#eef',
  },
  image: {
    flex: 1,
  },
})

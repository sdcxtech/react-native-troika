import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef } from 'react'
import { StyleSheet, View } from 'react-native'
import { CropView } from 'react-native-image-crop-tools'

function AvatarCrop() {
  const cropViewRef = useRef<any>()

  return (
    <View>
      <CropView
        sourceUrl={''}
        style={styles.cropview}
        ref={cropViewRef}
        onImageCrop={res => console.warn(res)}
        keepAspectRatio
        aspectRatio={{ width: 16, height: 9 }}
      />
    </View>
  )
}

export default withNavigationItem({
  titleItem: {
    title: 'Avatar',
  },
})(AvatarCrop)

const styles = StyleSheet.create({
  cropview: {},
})

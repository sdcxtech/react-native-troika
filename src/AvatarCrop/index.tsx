import { withNavigationItem } from 'hybrid-navigation'
import React, { useRef, useState } from 'react'
import { StyleSheet, View } from 'react-native'
import { isIphoneX } from 'react-native-iphone-x-helper'
import { openSettings } from 'react-native-permissions'
import { ImagePickerPermissionError, useImagePicker } from '../hooks/useImagePicker'
import ActionSheet from '../Modal/ActionSheet'
import SecondaryButton from '../SecondaryButton'
import ImagePicker from 'react-native-image-crop-picker'
import { useEffect } from 'react'

function AvatarCrop() {
  const [showsActionSheet, setShowsActionSheet] = useState(false)
  const { launchCamera, launchImageLibrary } = useImagePicker()

  const [imageUri, setImageUri] = useState('')

  useEffect(() => {
    if (imageUri) {
      console.log('------openCropper-------')
      setTimeout(() => {
        ImagePicker.openCropper({
          path: imageUri,
          width: 400,
          height: 400,
          mediaType: 'photo',
          cropperCircleOverlay: true,
          showCropGuidelines: false,
          showCropFrame: false,
          hideBottomControls: true,
        }).then(r => console.log(r))
      }, 450)
    }
  }, [imageUri])

  const handleImageResult = (uri: string | null) => {
    console.log(uri)
    if (uri !== null) {
      setImageUri(uri)
    }
  }

  const handleImageError = (error: any) => {
    if (error instanceof ImagePickerPermissionError) {
      openSettings()
    }
  }

  return (
    <View style={styles.container}>
      <SecondaryButton
        style={styles.button}
        titleStyle={styles.buttonText}
        title="更换头像"
        onPress={() => setShowsActionSheet(true)}
      />
      <ActionSheet
        visible={showsActionSheet}
        onDismiss={() => setShowsActionSheet(false)}
        actions={[
          {
            title: '拍照',
            onPress: async () => {
              try {
                const uri = await launchCamera()
                handleImageResult(uri)
              } catch (error) {
                handleImageError(error)
              }
            },
          },
          {
            title: '手机相册',
            onPress: async () => {
              try {
                const uri = await launchImageLibrary()
                handleImageResult(uri)
              } catch (error) {
                handleImageError(error)
              }
            },
          },
        ]}
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
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#FFFFFF',
  },
  cropview: {
    flex: 1,
    width: '100%',
    height: '100%',
  },
  button: {
    width: 130,
    height: 35,
    backgroundColor: '#202020',
    position: 'absolute',
    bottom: isIphoneX() ? 49 : 20,
  },
  buttonText: {
    color: '#FFFEFE',
    fontSize: 14,
    fontWeight: 'bold',
  },
})

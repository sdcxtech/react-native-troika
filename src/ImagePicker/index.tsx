import React, { useState } from 'react'
import {
  View,
  Image,
  StyleSheet,
  ViewStyle,
  StyleProp,
  ImageStyle,
  TouchableOpacity,
  ImageSourcePropType,
  Platform,
} from 'react-native'
import { launchImageLibrary, launchCamera, ErrorCode, ImagePickerResponse } from 'react-native-image-picker'
import { withCameraPermission, withImageLibraryPermission, PermissionError } from './permission'
import AlertModal from '../Modal/AlertModal'
import { openSettings } from 'react-native-permissions'
import ActionSheet from '../Modal/ActionSheet'

function pickImageFromImageLibrary(): Promise<string | null> {
  return new Promise((resolve, reject) => {
    launchImageLibrary(
      {
        selectionLimit: 1,
        mediaType: 'photo',
        quality: 1,
        includeBase64: false,
      },
      response => hanldeResponse(response, resolve, reject, 'library'),
    )
  })
}

function takePhotoFromCamera(): Promise<string | null> {
  return new Promise((resolve, reject) => {
    launchCamera(
      {
        mediaType: 'photo',
        quality: 1,
        includeBase64: false,
      },
      response => hanldeResponse(response, resolve, reject, 'camera'),
    )
  })
}

function hanldeResponse(
  response: ImagePickerResponse,
  resolve: (uri: string | null) => void,
  reject: (e: any) => void,
  sourceType: 'camera' | 'library',
) {
  const { assets, errorCode, didCancel } = response

  if (errorCode) {
    reject(errorForErrorCode(errorCode, sourceType))
    return
  }

  if (didCancel || !assets || assets.length === 0) {
    resolve(null)
    return
  }

  resolve(assets[0].uri!)
}

function errorForErrorCode(errorCode: ErrorCode, sourceType: 'camera' | 'library') {
  switch (errorCode) {
    case 'camera_unavailable':
      return new Error('相机不可用')
    case 'permission':
      if (sourceType === 'camera') {
        return new PermissionError('没有访问相机的权限')
      }
      return new PermissionError('没有访问相册的权限')
    case 'others':
      return new Error('未知错误')
  }
}

function alertMsgFromErrorMessage(error: Error | null) {
  if (!error) {
    return ''
  }

  if (error instanceof PermissionError) {
    return error.message
  }
  return ''
}

function alertTitleFromErrorMessage(error: Error | null) {
  if (!error) {
    return ''
  }

  if (error instanceof PermissionError) {
    if (error.message.includes('相机')) {
      return '请打开相机权限'
    } else {
      if (Platform.OS === 'ios') {
        return '请打开相册权限'
      } else {
        return '请打开文件存储权限'
      }
    }
  }

  return error.message
}

type ImagePickerSourceType = 'camera' | 'library' | 'both'

interface ImagePickerProps {
  style?: StyleProp<ViewStyle>
  imageStyle?: StyleProp<ImageStyle>
  sourceType?: ImagePickerSourceType
  placeholder?: ImageSourcePropType
  uri?: string
  onImageUriChange?: (uri: string) => void
}

const defaultImage = require('./add_pic.png')

export default function ImagePicker({
  style,
  imageStyle,
  sourceType = 'both',
  placeholder = defaultImage,
  uri,
  onImageUriChange,
}: ImagePickerProps) {
  const [error, setError] = useState<Error | null>(null)
  const [showsActionSheet, setShowsActionSheet] = useState(false)

  const pickImage = async () => {
    try {
      const uri = await withImageLibraryPermission(pickImageFromImageLibrary)()
      if (uri !== null) {
        onImageUriChange && onImageUriChange(uri)
      }
    } catch (e) {
      setError(e)
    }
  }

  const takePhoto = async () => {
    try {
      const uri = await withCameraPermission(takePhotoFromCamera)()
      if (uri !== null) {
        console.log(uri)
        onImageUriChange && onImageUriChange(uri)
      }
    } catch (e) {
      setError(e)
    }
  }

  const handlePress = () => {
    if (sourceType === 'library') {
      pickImage()
    } else if (sourceType === 'camera') {
      takePhoto()
    } else {
      setShowsActionSheet(true)
    }
  }

  return (
    <>
      <TouchableOpacity onPress={handlePress} activeOpacity={0.75} style={[styles.container, style]}>
        <View style={styles.content}>
          <Image style={[styles.image, imageStyle]} source={uri ? { uri } : placeholder} resizeMode="cover" />
        </View>
      </TouchableOpacity>
      <ActionSheet
        visible={showsActionSheet}
        onDismiss={() => setShowsActionSheet(false)}
        actions={[
          {
            title: '拍照',
            onPress: takePhoto,
          },
          {
            title: '手机相册',
            onPress: pickImage,
          },
        ]}
      />
      <AlertModal
        visible={!!error}
        title={alertTitleFromErrorMessage(error)}
        msg={alertMsgFromErrorMessage(error)}
        onNegativePress={() => setError(null)}
        onPositivePress={() => {
          setError(null)
          if (error instanceof PermissionError) {
            openSettings()
          }
        }}
      />
    </>
  )
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  content: { flex: 1 },
  image: { width: '100%', height: '100%' },
})

import { Platform } from 'react-native'
import {
  ErrorCode,
  ImagePickerResponse,
  launchCamera as launchCameraCallback,
  launchImageLibrary as launchImageLibraryCallback,
} from 'react-native-image-picker'
import { check, request, openSettings, PERMISSIONS, RESULTS } from 'react-native-permissions'

export class ImagePickerError extends Error {
  constructor(message: string) {
    super(message)
  }
}

export class ImagePickerPermissionError extends ImagePickerError {
  constructor(message: string) {
    super(message)
  }
}

type Callback<T> = (() => Promise<T>) | (() => T)

function withImageLibraryPermission<T>(fn: Callback<T>) {
  if (Platform.OS === 'ios') {
    return withImageLibraryPermissionIOS(fn)
  } else {
    return withImageLibraryPermissionAndroid(fn)
  }
}

function withImageLibraryPermissionAndroid<T>(fn: Callback<T>) {
  return async function () {
    const result = await check(PERMISSIONS.ANDROID.READ_EXTERNAL_STORAGE)
    if (result === RESULTS.GRANTED || result === RESULTS.LIMITED) {
      return fn()
    } else if (result === RESULTS.BLOCKED) {
      throw new ImagePickerPermissionError('没有访问手机存储的权限')
    } else if (result === RESULTS.DENIED) {
      const response = await request(PERMISSIONS.ANDROID.READ_EXTERNAL_STORAGE)
      if (response === RESULTS.GRANTED || response === RESULTS.LIMITED) {
        return fn()
      } else {
        throw new ImagePickerPermissionError('没有访问手机存储的权限')
      }
    } else {
      throw new ImagePickerError('设备不可用')
    }
  }
}

function withImageLibraryPermissionIOS<T>(fn: Callback<T>) {
  return async function () {
    const result = await check(PERMISSIONS.IOS.PHOTO_LIBRARY)
    if (result === RESULTS.GRANTED || result === RESULTS.LIMITED) {
      return fn()
    } else if (result === RESULTS.BLOCKED) {
      throw new ImagePickerPermissionError('没有访问相册的权限')
    } else if (result === RESULTS.DENIED) {
      const response = await request(PERMISSIONS.IOS.PHOTO_LIBRARY)
      if (response === RESULTS.GRANTED || response === RESULTS.LIMITED) {
        return fn()
      } else {
        throw new ImagePickerPermissionError('没有访问相册的权限')
      }
    } else {
      throw new ImagePickerError('设备不可用')
    }
  }
}

function withCameraPermission<T>(fn: Callback<T>) {
  if (Platform.OS === 'ios') {
    return withCameraPermissionIOS(fn)
  } else {
    return withCameraPermissionAndroid(fn)
  }
}

function withCameraPermissionAndroid<T>(fn: Callback<T>) {
  return async function () {
    const result = await check(PERMISSIONS.ANDROID.CAMERA)
    if (result === RESULTS.GRANTED || result === RESULTS.LIMITED) {
      return fn()
    } else if (result === RESULTS.BLOCKED) {
      throw new ImagePickerPermissionError('没有访问相机的权限')
    } else if (result === RESULTS.DENIED) {
      const response = await request(PERMISSIONS.ANDROID.CAMERA)
      if (response === RESULTS.GRANTED || response === RESULTS.LIMITED) {
        return fn()
      } else {
        throw new ImagePickerPermissionError('没有访问相机的权限')
      }
    } else {
      throw new ImagePickerError('相机不可用')
    }
  }
}

function withCameraPermissionIOS<T>(fn: Callback<T>) {
  return async function () {
    const result = await check(PERMISSIONS.IOS.CAMERA)
    if (result === RESULTS.GRANTED || result === RESULTS.LIMITED) {
      return fn()
    } else if (result === RESULTS.BLOCKED) {
      throw new ImagePickerPermissionError('没有访问相机的权限')
    } else if (result === RESULTS.DENIED) {
      const response = await request(PERMISSIONS.IOS.CAMERA)
      if (response === RESULTS.GRANTED || response === RESULTS.LIMITED) {
        return fn()
      } else {
        throw new ImagePickerPermissionError('没有访问相机的权限')
      }
    } else {
      throw new ImagePickerError('相机不可用')
    }
  }
}

function launchImageLibraryAsync(): Promise<string | null> {
  return new Promise((resolve, reject) => {
    launchImageLibraryCallback(
      {
        selectionLimit: 1,
        mediaType: 'photo',
        quality: 1,
        includeBase64: false,
      },
      response => hanldeImagePickerResponse(response, resolve, reject, 'library'),
    )
  })
}

function launchCameraAsync(): Promise<string | null> {
  return new Promise((resolve, reject) => {
    launchCameraCallback(
      {
        mediaType: 'photo',
        quality: 1,
        includeBase64: false,
      },
      response => hanldeImagePickerResponse(response, resolve, reject, 'camera'),
    )
  })
}

function hanldeImagePickerResponse(
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
      return new ImagePickerError('相机不可用')
    case 'permission':
      if (sourceType === 'camera') {
        return new ImagePickerPermissionError('没有访问相机的权限')
      }
      return new ImagePickerPermissionError('没有访问相册的权限')
    case 'others':
      return new ImagePickerError('未知错误')
  }
}

export function useImagePicker() {
  return {
    launchCamera: withCameraPermission(launchCameraAsync),
    launchImageLibrary: withImageLibraryPermission(launchImageLibraryAsync),
    openSettings,
  }
}

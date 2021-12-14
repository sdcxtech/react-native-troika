import { Platform } from 'react-native'
import { PERMISSIONS, request, check, RESULTS } from 'react-native-permissions'

export class PermissionError extends Error {
  constructor(message: string) {
    super(message)
  }
}

type Callback<T> = (() => Promise<T>) | (() => T)

export function withImageLibraryPermission<T>(fn: Callback<T>) {
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
      throw new PermissionError('没有访问手机存储的权限')
    } else if (result === RESULTS.DENIED) {
      const response = await request(PERMISSIONS.ANDROID.READ_EXTERNAL_STORAGE)
      if (response === RESULTS.GRANTED || response === RESULTS.LIMITED) {
        return fn()
      } else {
        throw new PermissionError('没有访问手机存储的权限')
      }
    } else {
      throw new Error('设备不可用')
    }
  }
}

function withImageLibraryPermissionIOS<T>(fn: Callback<T>) {
  return async function () {
    const result = await check(PERMISSIONS.IOS.PHOTO_LIBRARY)
    if (result === RESULTS.GRANTED || result === RESULTS.LIMITED) {
      return fn()
    } else if (result === RESULTS.BLOCKED) {
      throw new PermissionError('没有访问相册的权限')
    } else if (result === RESULTS.DENIED) {
      const response = await request(PERMISSIONS.IOS.PHOTO_LIBRARY)
      if (response === RESULTS.GRANTED || response === RESULTS.LIMITED) {
        return fn()
      } else {
        throw new PermissionError('没有访问相册的权限')
      }
    } else {
      throw new Error('设备不可用')
    }
  }
}

export function withCameraPermission<T>(fn: Callback<T>) {
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
      throw new PermissionError('没有访问相机的权限')
    } else if (result === RESULTS.DENIED) {
      const response = await request(PERMISSIONS.ANDROID.CAMERA)
      if (response === RESULTS.GRANTED || response === RESULTS.LIMITED) {
        return fn()
      } else {
        throw new PermissionError('没有访问相机的权限')
      }
    } else {
      throw new Error('相机不可用')
    }
  }
}

function withCameraPermissionIOS<T>(fn: Callback<T>) {
  return async function () {
    const result = await check(PERMISSIONS.IOS.CAMERA)
    if (result === RESULTS.GRANTED || result === RESULTS.LIMITED) {
      return fn()
    } else if (result === RESULTS.BLOCKED) {
      throw new PermissionError('没有访问相机的权限')
    } else if (result === RESULTS.DENIED) {
      const response = await request(PERMISSIONS.IOS.CAMERA)
      if (response === RESULTS.GRANTED || response === RESULTS.LIMITED) {
        return fn()
      } else {
        throw new PermissionError('没有访问相机的权限')
      }
    } else {
      throw new Error('相机不可用')
    }
  }
}

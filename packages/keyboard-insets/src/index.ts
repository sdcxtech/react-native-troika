import { NativeModules } from 'react-native'

const { RNKeyboardInsets } = NativeModules

export default RNKeyboardInsets

export function lib(a: number, b: number) {
  return a + b + 2
}

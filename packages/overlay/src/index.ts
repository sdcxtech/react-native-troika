import { NativeModules } from 'react-native'

const { RNOverlay } = NativeModules

export default RNOverlay

export function lib(a: number, b: number) {
  return a + b + 2
}

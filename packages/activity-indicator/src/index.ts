import { NativeModules } from 'react-native'

const { RNActivityIndicator } = NativeModules

export default RNActivityIndicator

export function lib(a: number, b: number) {
  return a + b + 2
}

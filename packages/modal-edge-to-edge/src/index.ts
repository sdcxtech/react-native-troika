import { NativeModules } from 'react-native'

const { RNModalx } = NativeModules

export default RNModalx

export function lib(a: number, b: number) {
  return a + b + 2
}

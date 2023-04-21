import { NativeModules } from 'react-native'

const { RNImageCrop } = NativeModules

export default RNImageCrop

export function lib(a: number, b: number) {
  return a + b + 2
}

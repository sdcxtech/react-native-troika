import { NativeModules } from 'react-native'

const { RNWheelPacker } = NativeModules

export default RNWheelPacker

export function lib(a: number, b: number) {
  return a + b + 2
}

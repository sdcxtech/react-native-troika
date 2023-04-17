import { NativeModules } from 'react-native'

const { RNPullToRefresh } = NativeModules

export default RNPullToRefresh

export function lib(a: number, b: number) {
  return a + b + 2
}

import { NativeModules } from 'react-native'

const { RNNestedScrollWebView } = NativeModules

export default RNNestedScrollWebView

export function lib(a: number, b: number) {
  return a + b + 2
}

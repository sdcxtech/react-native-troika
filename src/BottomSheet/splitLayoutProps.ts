import { ViewStyle } from 'react-native'

type StyleKey = keyof ViewStyle

export default function splitLayoutProps(props?: ViewStyle) {
  let outer: { [key: string]: ViewStyle[StyleKey] } = {}
  let inner: { [key: string]: ViewStyle[StyleKey] } = {}

  if (props != null) {
    for (const prop of Object.keys(props) as StyleKey[]) {
      switch (prop) {
        case 'margin':
        case 'marginHorizontal':
        case 'marginVertical':
        case 'marginBottom':
        case 'marginTop':
        case 'marginLeft':
        case 'marginRight':
        case 'flex':
        case 'flexGrow':
        case 'flexShrink':
        case 'flexBasis':
        case 'alignSelf':
        case 'height':
        case 'minHeight':
        case 'maxHeight':
        case 'width':
        case 'minWidth':
        case 'maxWidth':
        case 'position':
        case 'left':
        case 'right':
        case 'bottom':
        case 'top':
        case 'transform':
        case 'zIndex':
          outer[prop] = props[prop]
          break
        default:
          inner[prop] = props[prop]
          break
      }
    }
  }

  return { outer, inner }
}

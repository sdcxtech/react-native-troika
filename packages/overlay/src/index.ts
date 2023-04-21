import { NativeModule, NativeModules } from 'react-native'

interface OverlayOptions {
  passThroughTouches?: boolean
}

interface PropsType {
  [index: string]: any
}
interface OverlayInterface extends NativeModule {
  show<P extends PropsType = {}>(moduleName: string, props?: P, options?: OverlayOptions): void
  hide(moduleName: string): void
}

const OverlayHost: OverlayInterface = NativeModules.OverlayHost

function show<P extends PropsType = {}>(moduleName: string, props?: P, options: OverlayOptions = {}) {
  OverlayHost.show(moduleName, props, options)
}

function hide(moduleName: string) {
  OverlayHost.hide(moduleName)
}

const Overlay = { show, hide }

export { Overlay }

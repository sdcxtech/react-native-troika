import { NativeModule, NativeModules } from 'react-native'

export interface OverlayOptions {
  id: number
  passThroughTouches?: boolean
}

interface OverlayInterface extends NativeModule {
  show(moduleName: string, options: OverlayOptions): void
  hide(moduleName: string, id: number): void
}

const OverlayHost: OverlayInterface = NativeModules.OverlayHost

function show(moduleName: string, options: OverlayOptions) {
  OverlayHost.show(moduleName, options)
}

function hide(moduleName: string, id: number) {
  OverlayHost.hide(moduleName, id)
}

const Overlay = { show, hide }

export { Overlay }

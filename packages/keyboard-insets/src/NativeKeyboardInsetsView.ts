import type { TurboModule } from "react-native";
import { TurboModuleRegistry } from 'react-native'
import { Insets } from "react-native";
export interface Spec extends TurboModule {
  //TODO:callback: (insets: Insets) => void, Insets类型不支持
  getEdgeInsetsForView(viewTag: number, ): void;
}

export default TurboModuleRegistry.get<Spec>('RNCKeyboardInsetsView')

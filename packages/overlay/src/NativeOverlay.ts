import type { TurboModule, CodegenTypes } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface OverlayOptions {
	overlayId: CodegenTypes.Int32;
	passThroughTouches?: boolean;
}

export interface Spec extends TurboModule {
	show(moduleName: string, options: OverlayOptions): void;
	hide(moduleName: string, overlayId: CodegenTypes.Int32): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('OverlayHost');

import { type Insets } from 'react-native';
import NativeOverlay, { type OverlayOptions } from './NativeOverlay';

export interface OverlayProps extends OverlayOptions {
	insets: Insets;
}

function show(moduleName: string, options: OverlayOptions) {
	NativeOverlay.show(moduleName, options);
}

function hide(moduleName: string, id: number) {
	NativeOverlay.hide(moduleName, id);
}

const Overlay = { show, hide };

export { Overlay };

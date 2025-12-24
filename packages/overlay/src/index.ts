import NativeOverlay, { type OverlayOptions } from './NativeOverlay';

export type OverlayProps = OverlayOptions;

function show(moduleName: string, options: OverlayOptions) {
	NativeOverlay.show(moduleName, options);
}

function hide(moduleName: string, id: number) {
	NativeOverlay.hide(moduleName, id);
}

const Overlay = { show, hide };

export { Overlay };

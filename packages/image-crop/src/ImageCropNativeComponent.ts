import type { CodegenTypes, HostComponent, ViewProps } from 'react-native';
import { codegenNativeComponent, codegenNativeCommands } from 'react-native';

export interface ObjectRect {
	top: CodegenTypes.Float;
	left: CodegenTypes.Float;
	width: CodegenTypes.Float;
	height: CodegenTypes.Float;
}

export type OnCropEventPayload = {
	uri: string;
};

export interface NativeProps extends ViewProps {
	fileUri?: string;
	cropStyle?: CodegenTypes.WithDefault<'circular' | 'default', 'default'>;
	objectRect?: ObjectRect;
	onCrop?: CodegenTypes.DirectEventHandler<OnCropEventPayload>;
}

export interface NativeCommands {
	// In TypeScript, the React.ElementRef is deprecated.
	// The correct type to use is actually React.ComponentRef.
	// However, due to a bug in Codegen, using ComponentRef will crash the app.
	// We have the fix already, but we need to release a new version of React Native to apply it.
	crop: (viewRef: React.ElementRef<HostComponent<NativeProps>>) => void;
}

export const Commands: NativeCommands = codegenNativeCommands<NativeCommands>({
	supportedCommands: ['crop'],
});

export default codegenNativeComponent<NativeProps>('ImageCropView') as HostComponent<NativeProps>;

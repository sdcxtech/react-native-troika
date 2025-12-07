import type { ColorValue, HostComponent, ViewProps } from 'react-native';
import { codegenNativeComponent } from 'react-native';
import type { CodegenTypes } from 'react-native';

export interface NativeProps extends ViewProps {
	animating?: CodegenTypes.WithDefault<boolean, true>;
	color?: ColorValue;
	size?: CodegenTypes.WithDefault<'small' | 'large', 'small'>;
}

export default codegenNativeComponent<NativeProps>('ActivityIndicator', {
	interfaceOnly: true,
}) as HostComponent<NativeProps>;

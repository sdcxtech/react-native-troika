import type { HostComponent, ViewProps } from 'react-native';
import { codegenNativeComponent } from 'react-native';

export interface NativeProps extends ViewProps {
	//iOS Only
	bounces?: boolean;
}

export default codegenNativeComponent<NativeProps>('NestedScrollView', {
	interfaceOnly: true,
}) as HostComponent<NativeProps>;

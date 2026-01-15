import type { HostComponent, ViewProps, CodegenTypes } from 'react-native';
import { codegenNativeComponent } from 'react-native';

export interface StateChangedEventPayload {
	state: CodegenTypes.Int32;
}

export interface OffsetChangedEventPayload {
	offset: CodegenTypes.Float;
}

interface RefreshingEventPayload {}

export interface NativeProps extends ViewProps {
	onRefresh?: CodegenTypes.DirectEventHandler<RefreshingEventPayload>;
	onStateChanged?: CodegenTypes.DirectEventHandler<StateChangedEventPayload>;
	onOffsetChanged?: CodegenTypes.DirectEventHandler<OffsetChangedEventPayload>;
	refreshing?: CodegenTypes.WithDefault<boolean, false>;
	progressViewOffset?: CodegenTypes.Float | undefined;
}

export default codegenNativeComponent<NativeProps>('PullToRefreshHeader', {
	interfaceOnly: true,
}) as HostComponent<NativeProps>;

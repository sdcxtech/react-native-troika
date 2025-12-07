import type { CodegenTypes, HostComponent, ViewProps } from 'react-native';
import { codegenNativeComponent } from 'react-native';

export interface NestedScrollEventPayload {
	contentOffset: {
		x: CodegenTypes.Float;
		y: CodegenTypes.Float;
	};
}

export interface NativeProps extends ViewProps {
	stickyHeaderHeight?: CodegenTypes.WithDefault<CodegenTypes.Float, -1>;
	stickyHeaderBeginIndex?: CodegenTypes.WithDefault<CodegenTypes.Int32, -1>;
	onScroll?: CodegenTypes.DirectEventHandler<NestedScrollEventPayload>;
}

export default codegenNativeComponent<NativeProps>(
	'NestedScrollViewHeader',
) as HostComponent<NativeProps>;

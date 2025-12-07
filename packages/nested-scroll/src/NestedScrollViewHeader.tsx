import React from 'react';
import { NativeScrollPoint, NativeSyntheticEvent, ViewProps } from 'react-native';
import NestedScrollViewHeaderNativeComponent from './NestedScrollViewHeaderNativeComponent';

interface NestedScrollEventData {
	contentOffset: NativeScrollPoint;
}

export type NestedScrollEvent = NativeSyntheticEvent<NestedScrollEventData>;

export interface NestedScrollViewHeaderProps extends ViewProps {
	stickyHeaderHeight?: number;
	stickyHeaderBeginIndex?: number;
	onScroll?: (event: NestedScrollEvent) => void;
}

type NativeNestedScrollViewHeaderInstance = InstanceType<
	typeof NestedScrollViewHeaderNativeComponent
>;

const NestedScrollViewHeader = React.forwardRef<
	NativeNestedScrollViewHeaderInstance,
	NestedScrollViewHeaderProps
>((props, ref) => {
	return <NestedScrollViewHeaderNativeComponent {...props} ref={ref} />;
});

export default NestedScrollViewHeader;

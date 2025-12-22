import React, { useImperativeHandle, useRef } from 'react';
import ImageCropNativeComponent, { Commands } from './ImageCropNativeComponent';
import type { ObjectRect, OnCropEventPayload, NativeProps } from './ImageCropNativeComponent';
import type { NativeSyntheticEvent } from 'react-native';

export type OnCropEvent = NativeSyntheticEvent<OnCropEventPayload>;
export type { ObjectRect };

export interface ImageCropViewInstance {
	crop: () => void;
}

export type ImageCropViewProps = NativeProps;

const ImageCropView = React.forwardRef<ImageCropViewInstance, ImageCropViewProps>((props, ref) => {
	const viewRef = useRef<React.ComponentRef<typeof ImageCropNativeComponent>>(null);
	useImperativeHandle(ref, () => ({
		crop: () => {
			if (viewRef.current) {
				Commands.crop(viewRef.current);
			}
		},
	}));
	return <ImageCropNativeComponent {...props} ref={viewRef} />;
});

export default ImageCropView;

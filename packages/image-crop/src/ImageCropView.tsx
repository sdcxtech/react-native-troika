import {
  findNodeHandle,
  Platform,
  requireNativeComponent,
  UIManager,
  ViewProps,
  ViewStyle,
} from 'react-native';
import React, {forwardRef, useCallback, useImperativeHandle, useRef} from 'react';
import {ImageCropViewRef} from './ImageCropViewRef';
import {ObjectRect} from './typings';

const CropViewNativeComponent = requireNativeComponent<CropViewNativeComponentProps>('RNImageCrop');

interface SupperProps extends ViewProps {
  fileUri: string;
  objectRect?: ObjectRect;
  cropStyle?: 'circular' | 'default';
}

interface CropViewNativeComponentProps extends SupperProps {
  onCropped: (callback: any) => void;
}

interface CropViewProps extends SupperProps {
  style?: ViewStyle;
  onCropped: (uri: string) => void;
}

const ImageCropView = forwardRef<ImageCropViewRef, CropViewProps>(
  ({fileUri, cropStyle, objectRect, style, onCropped}: CropViewProps, ref) => {
    const reactTag = useRef<number | null>(null);
    const crop = useCallback(() => {
      UIManager.dispatchViewManagerCommand(
        reactTag.current,
        Platform.OS === 'ios'
          ? UIManager.getViewManagerConfig('RNImageCrop').Commands.crop
          : UIManager.getViewManagerConfig('RNImageCrop').Commands.crop.toString(),
        Platform.OS === 'ios' ? [] : [reactTag.current],
      );
    }, []);

    const onNativeCropped = useCallback(
      ({nativeEvent}: any) => {
        const uri = nativeEvent.uri;
        if (onCropped) {
          onCropped(uri);
        }
      },
      [onCropped],
    );

    useImperativeHandle(
      ref,
      () => ({
        crop,
      }),
      [crop],
    );

    return (
      <CropViewNativeComponent
        fileUri={fileUri}
        cropStyle={cropStyle}
        objectRect={objectRect}
        onCropped={onNativeCropped}
        style={style}
        ref={mRef => {
          reactTag.current = findNodeHandle(mRef);
        }}
      />
    );
  },
);

export default ImageCropView;

import React from 'react';
import {
  NativeSyntheticEvent,
  requireNativeComponent,
  StyleSheet,
  View,
  ViewProps,
} from 'react-native';
import splitLayoutProps from './splitLayoutProps';

export interface OffsetChangedEventData {
  progress: number;
  offset: number;
  expandedOffset: number;
  collapsedOffset: number;
}

export type BottomSheetState = 'collapsed' | 'expanded' | 'hidden';

export interface StateChangedEventData {
  state: BottomSheetState;
}

interface NativeBottomSheetProps extends ViewProps {
  onSlide?: (event: NativeSyntheticEvent<OffsetChangedEventData>) => void;
  onStateChanged?: (event: NativeSyntheticEvent<StateChangedEventData>) => void;
  peekHeight?: number;
  draggable?: boolean;
  state?: BottomSheetState;
  contentContainerStyle?: ViewProps['style'];
}

type BottomSheetProps = NativeBottomSheetProps & {
  fitToContents?: boolean;
};

const NativeBottomSheet = requireNativeComponent<NativeBottomSheetProps>('BottomSheet');

type NativeBottomSheetInstance = InstanceType<typeof NativeBottomSheet>;

const BottomSheet = React.forwardRef<NativeBottomSheetInstance, BottomSheetProps>((props, ref) => {
  const {
    style,
    contentContainerStyle,
    children,
    peekHeight = 200,
    draggable = true,
    state = 'collapsed',
    fitToContents,
    ...rest
  } = props;
  const {outer, inner} = splitLayoutProps(StyleSheet.flatten(style));
  return (
    <NativeBottomSheet
      style={[StyleSheet.absoluteFill, outer]}
      peekHeight={peekHeight}
      draggable={draggable}
      state={state}
      {...rest}
      ref={ref}>
      <View
        style={[
          fitToContents ? styles.fitToContents : StyleSheet.absoluteFill,
          inner,
          contentContainerStyle,
        ]}
        collapsable={false}>
        {children}
      </View>
    </NativeBottomSheet>
  );
});

const styles = StyleSheet.create({
  fitToContents: {
    position: 'absolute',
    left: 0,
    right: 0,
    bottom: 0,
  },
});

export default BottomSheet;

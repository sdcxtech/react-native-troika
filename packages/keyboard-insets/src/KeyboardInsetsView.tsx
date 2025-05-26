import React, {PropsWithChildren, useCallback, useMemo, useRef} from 'react';
import {Animated, ViewProps, View} from 'react-native';
import {KeyboardPositionChangedEvent, KeyboardStatusChangedEvent, } from './native';
import RNCKeyboardInsetsViewNativeComponent from './RNCKeyboardInsetsViewNativeComponent';
const isFabricEnabled = (global as any)?.nativeFabricUIManager != null
// const KeyboardInsetsViewC = isFabricEnabled
//     ? KeyboardInsetsViewNativeComponent
//     : NativeKeyboardInsetsView
export interface KeyboardState {
  height: number;
  shown: boolean;
  transitioning: boolean;
  position: Animated.Value;
}
const RNCKeyboardInsetsView =  require('./RNCKeyboardInsetsViewNativeComponent').default
const NativeKeyboardInsetsViewAnimated = Animated.createAnimatedComponent(RNCKeyboardInsetsView);

interface KeyboardInsetsViewProps extends Animated.AnimatedProps<ViewProps> {
  extraHeight?: number;
  explicitly?: boolean;
  onKeyboard?: (status: KeyboardState) => void;
}

export function KeyboardInsetsView(props: PropsWithChildren<KeyboardInsetsViewProps>) {
  const {children, onKeyboard, ...rest} = props;
  const position = useRef(new Animated.Value(0)).current;

  // const onPositionChanged = useMemo(
  //   () =>
  //     Animated.event(
  //       [
  //         {
  //           nativeEvent: {
  //             position,
  //           },
  //         },
  //       ],
  //       {
  //         useNativeDriver: true,
  //       },
  //     ),
  //   [position],
  // );

  const _onPositionChanged = useCallback(
    (event: KeyboardPositionChangedEvent) => {
      position.setValue(event.nativeEvent.position)
    },
    [position],
  );

  const onStatusChanged = useCallback(
    (event: KeyboardStatusChangedEvent) => {
      onKeyboard?.({...event.nativeEvent, position});
    },
    [position, onKeyboard],
  );

  // if (onKeyboard) {
    return (
      <NativeKeyboardInsetsViewAnimated
       collapsable={false}
        mode="manual"
        extraHeight={0}
        explicitly={false}
        onStatusChanged={onStatusChanged}
        onPositionChanged={_onPositionChanged}
        {...rest}>
        {children}
      </NativeKeyboardInsetsViewAnimated>
    );
  // }

  // return <NativeKeyboardInsetsView {...rest}>{children}</NativeKeyboardInsetsView>;
}

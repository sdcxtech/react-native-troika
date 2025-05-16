import React, {PropsWithChildren} from 'react';
import {
  NativeScrollEvent,
  NativeSyntheticEvent,
  Platform,
  requireNativeComponent,
  StyleProp,
  StyleSheet,
  View,
  ViewStyle,
} from 'react-native';

export type NestedScrollViewProps = PropsWithChildren<{
  style?: StyleProp<ViewStyle>;
  onScroll?: (event: NativeSyntheticEvent<Omit<NativeScrollEvent, 'velocity'>>) => void;
  //Android Only
  contentContainerStyle?: StyleProp<ViewStyle>;
  //iOS Only
  bounces?: boolean;
}>;

type NestedScrollViewNativeProps = Omit<NestedScrollViewProps, 'contentContainerStyle'>;

const NestedScrollViewNative =
  requireNativeComponent<NestedScrollViewNativeProps>('NestedScrollView');

function NestedScrollView({children, style, ...props}: NestedScrollViewProps) {
  if (Platform.OS === 'android') {
    return (
      <NestedScrollViewAndroid style={[styles.fill, style]} {...props}>
        {children}
      </NestedScrollViewAndroid>
    );
  }
  return (
    <NestedScrollViewNative style={[styles.fill, style]} {...props}>
      {children}
    </NestedScrollViewNative>
  );
}

export function NestedScrollViewAndroid({
  children,
  contentContainerStyle,
  ...props
}: NestedScrollViewProps) {
  return (
    <NestedScrollViewNative {...props}>
      <View style={[styles.content, contentContainerStyle]} collapsable={false}>
        {children}
      </View>
    </NestedScrollViewNative>
  );
}

const styles = StyleSheet.create({
  fill: {
    flex: 1,
  },
  content: {
    flex: 1,
  },
});

export {NestedScrollView};

export {NestedScrollViewHeader} from './NestedScrollViewHeader';

export type {NestedScrollEvent, NestedScrollViewHeaderProps} from './NestedScrollViewHeader';

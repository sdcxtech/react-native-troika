import React from 'react';
import {AppRegistry, Pressable, StyleSheet, Text, View} from 'react-native';
import {Overlay, OverlayProps} from '@sdcx/overlay';

interface ToastProps {
  message: string;
  duration?: number;
  position?: 'top' | 'center' | 'bottom';
  onPress: () => void;
}

function ToastView({id, insets, passThroughTouches}: OverlayProps) {
  console.log('id', id, 'insets', insets);
  const props = propsMap.get(id);
  const {message, onPress} = props || {};
  return (
    <View style={styles.container} pointerEvents={passThroughTouches ? 'box-none' : 'auto'}>
      <Pressable style={styles.box} onPress={onPress}>
        <Text style={styles.message}>{message}</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  box: {
    backgroundColor: 'rgba(0,0,0,0.8)',
    padding: 8,
    paddingHorizontal: 16,
    borderRadius: 8,
  },
  message: {
    color: 'white',
  },
});

function registerIfNeeded() {
  if (AppRegistry.getAppKeys().includes('__overlay_toast__')) {
    return;
  }
  AppRegistry.registerComponent('__overlay_toast__', () => ToastView);
}

let key = 0;

function showInternal() {
  registerIfNeeded();
  key += 1;
  Overlay.show('__overlay_toast__', {passThroughTouches: true, id: key});
  return key;
}

function hideInternal(key: number) {
  propsMap.delete(key);
  Overlay.hide('__overlay_toast__', key);
}

const propsMap = new Map<number, ToastProps>();

interface ToastConfig {
  message: string;
  duration?: number;
  position?: 'top' | 'center' | 'bottom';
  onPress?: () => void;
}

const Toast = {
  show: (config: ToastConfig) => {
    const key = showInternal();
    const params = {
      ...config,
      onPress: () => {
        if (config.onPress) {
          hideInternal(key);
          config.onPress();
        }
      },
    };
    propsMap.set(key, params);

    if (config.duration) {
      setTimeout(() => {
        hideInternal(key);
      }, config.duration);
    }
  },
};

export default Toast;

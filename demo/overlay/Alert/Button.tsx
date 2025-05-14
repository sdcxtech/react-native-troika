import React from 'react';
import {StyleSheet, Text, ViewProps, TextStyle, StyleProp, Pressable} from 'react-native';

interface Props extends ViewProps {
  onPress: () => void;
  text: string;
  textStyle?: StyleProp<TextStyle>;
}

export default function Button({style, text, onPress, textStyle}: Props) {
  return (
    <Pressable onPress={onPress} style={[styles.button, style]}>
      <Text style={[styles.text, textStyle]}>{text}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  button: {
    height: 44,
    flex: 1,
    borderRadius: 4,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#F2F6FC',
  },
  text: {
    fontSize: 16,
    color: '#606972',
    fontWeight: 'bold',
  },
});

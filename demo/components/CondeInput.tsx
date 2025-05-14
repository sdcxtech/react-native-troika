import {useLayout} from '@react-native-community/hooks';
import React from 'react';
import {ViewStyle, StyleProp, StyleSheet, Text, View, TextInput} from 'react-native';

interface CodeInputProps {
  value: string;
  onValueChange: (value: string) => void;
  style?: StyleProp<ViewStyle>;
  length?: number;
  spacing?: number;
}

export default function CodeInput({
  value,
  onValueChange,
  style,
  length = 4,
  spacing = 8,
}: CodeInputProps) {
  const {onLayout, height} = useLayout();
  const size = height;

  const cells = value.split('').concat(Array(length - value.length).fill(''));

  return (
    <View style={[styles.container, style]} onLayout={onLayout}>
      <TextInput
        style={styles.input}
        autoFocus={true}
        keyboardType="number-pad"
        maxLength={length}
        value={value}
        clearButtonMode="never"
        onChangeText={onValueChange}
      />
      <View style={[styles.cover, StyleSheet.absoluteFillObject]} pointerEvents="none">
        {cells.map((text: string, index: number) => (
          <View
            style={[
              styles.cell,
              {width: size, height: size, marginLeft: index === 0 ? 0 : spacing},
            ]}
            key={index}>
            <Text style={styles.text}>{text}</Text>
          </View>
        ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    height: 44,
  },
  input: {
    flex: 1,
    padding: 0,
    margin: 0,
    opacity: 0,
  },
  cover: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
  },
  cell: {
    borderRadius: 8,
    backgroundColor: '#ECECEC',
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    color: '#333333',
    fontSize: 17,
    fontWeight: 'bold',
  },
});

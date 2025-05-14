import React, {useEffect, useState} from 'react';
import {StyleProp, StyleSheet, View, ViewStyle} from 'react-native';
import Picker, {PickerItem} from '@sdcx/wheel-picker';

interface Province {
  code: string;
  name: string;
  children: City[];
}

interface City {
  code: string;
  name: string;
}

const data: Province[] = require('./pc-code.json');
const provinces = genItems(data);

function genItems(data: Province[] | City[]): PickerItem<string>[] {
  return data.map(p => ({label: p.name, value: p.code}));
}

function genCitiesItem(pcode: string) {
  for (let i = 0; i < data.length; i++) {
    const p = data[i];
    if (p.code === pcode) {
      return genItems(p.children);
    }
  }
  return [];
}

interface CityPickerProps {
  citycode?: string;
  onCitycodeChange?: (citycode: string) => void;
  style?: StyleProp<ViewStyle>;
}

export default function CityPicker({citycode = '11', onCitycodeChange, style}: CityPickerProps) {
  const pcode = citycode.substring(0, 2);
  const [cities, setCities] = useState(genCitiesItem(pcode));

  useEffect(() => {
    setCities(genCitiesItem(pcode));
  }, [pcode]);

  const _onProvinceChange = (code: string) => {
    setCities(genCitiesItem(code));
  };

  const _onCityChange = (code: string) => {
    onCitycodeChange?.(code);
  };

  return (
    <View style={[styles.compose, style]}>
      <View style={styles.curtain} />
      <Picker
        selectedValue={pcode}
        onValueChange={_onProvinceChange}
        style={styles.picker}
        itemStyle={styles.itemStyle}
        items={provinces}
      />
      <Picker
        selectedValue={citycode}
        onValueChange={_onCityChange}
        style={styles.picker}
        itemStyle={styles.itemStyle}
        items={cities}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  compose: {
    height: 224,
    paddingHorizontal: 12,
    flexDirection: 'row',
    alignItems: 'center',
  },
  curtain: {
    position: 'absolute',
    left: 12,
    height: 40,
    width: '100%',
    backgroundColor: '#F2F5F7',
    borderRadius: 8,
  },
  picker: {
    flex: 1,
  },
  itemStyle: {
    color: '#9A9EFF',
    fontSize: 15,
  },
});

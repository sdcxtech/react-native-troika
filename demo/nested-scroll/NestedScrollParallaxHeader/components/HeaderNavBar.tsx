import {RoundButton} from './RoundButton';
import {StyleSheet, View} from 'react-native';
import * as React from 'react';
import ArrowLeft from '../icons/ArrowLeft';
import ArrowRight from '../icons/ArrowRight';
import {Share} from '../icons/Share';
import {useNavigator} from 'hybrid-navigation';

export const HeaderNavBar = () => {
  const navigator = useNavigator();
  return (
    <View style={styles.container}>
      <RoundButton icon={<ArrowLeft />} onPress={navigator.pop} />
      <View style={styles.btnRightContainer}>
        <View style={styles.btnRight}>
          <RoundButton icon={<ArrowRight />} onPress={() => null} />
        </View>
        <RoundButton icon={<Share />} onPress={() => null} />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    width: '100%',
    paddingHorizontal: 8,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  btnRightContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  btnRight: {
    marginRight: 8,
  },
});

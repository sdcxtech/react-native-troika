import React, {useState} from 'react';
import {NativeSyntheticEvent, ScrollView, StyleSheet, View} from 'react-native';
import {LoremIpsum} from '../../components/LoremIpsum';
import BottomSheet, {BottomSheetState, StateChangedEventData} from '@sdcx/bottom-sheet';
import {withNavigationItem} from 'hybrid-navigation';

import Button from './Button';

const HEADER_HEIGHT = 50;

function BottomSheetWithoutScrollView() {
  const [state, setBottomSheetState] = useState<BottomSheetState>('collapsed');

  const onStateChanged = (e: NativeSyntheticEvent<StateChangedEventData>) => {
    console.log(e.nativeEvent);
    setBottomSheetState(e.nativeEvent.state);
  };

  return (
    <View style={styles.container}>
      <ScrollView>
        <View style={styles.toolbar}>
          <Button
            text="expand"
            onPress={() => {
              setBottomSheetState('expanded');
            }}
          />
        </View>
        <LoremIpsum />
        <LoremIpsum />
        <LoremIpsum />
      </ScrollView>
      <BottomSheet
        fitToContents
        peekHeight={200}
        state={state}
        onStateChanged={onStateChanged}
        onSlide={e => console.log(e.nativeEvent)}
        style={styles.bottomSheet}>
        <View style={styles.header}>
          <Button
            text="collapse"
            onPress={() => {
              setBottomSheetState('collapsed');
            }}
          />
          <Button
            text="expand"
            onPress={() => {
              setBottomSheetState('expanded');
            }}
          />

          <Button
            text="hide"
            onPress={() => {
              setBottomSheetState('hidden');
            }}
          />
        </View>
        <LoremIpsum words={200} />
      </BottomSheet>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#eef',
  },
  header: {
    height: HEADER_HEIGHT,
    backgroundColor: 'coral',
    paddingHorizontal: 16,
    flexDirection: 'row',
  },
  toolbar: {
    height: HEADER_HEIGHT,
    backgroundColor: 'cadetblue',
    paddingHorizontal: 16,
    flexDirection: 'row',
  },
  bottomSheet: {
    backgroundColor: '#ff9f7A',
  },
});

export default withNavigationItem({
  titleItem: {
    title: 'BottomSheet without ScrollView',
  },
})(BottomSheetWithoutScrollView);

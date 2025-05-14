import React from 'react';
import {View, StyleSheet, Button} from 'react-native';
import {withNavigationItem} from 'hybrid-navigation';
import Toast from './Toast';

function ToastScreen() {
  return (
    <View style={styles.container}>
      <Button
        title="Show Toast"
        onPress={() => {
          Toast.show({
            message: 'Hello World!',
            duration: 2000,
          });
        }}
      />
    </View>
  );
}

export default withNavigationItem({
  titleItem: {
    title: 'Toast',
  },
})(ToastScreen);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'stretch',
    paddingTop: 16,
    paddingLeft: 32,
    paddingRight: 32,
  },
  text: {
    backgroundColor: 'transparent',
    fontSize: 17,
    alignSelf: 'center',
  },
});

import React, {useCallback, useRef, useState} from 'react';
import {
  Animated,
  findNodeHandle,
  Image,
  Pressable,
  ScrollView,
  Text,
  TextInput,
  View,
} from 'react-native';

import {SafeAreaProvider, SafeAreaView} from 'react-native-safe-area-context';
import {KeyboardInsetsView, getEdgeInsetsForView} from '@sdcx/keyboard-insets';
import {withNavigationItem} from 'hybrid-navigation';

import Message from './Message';
import {history} from './Message/data';
import styles from './styles';
import {ViewDriver} from './driver/ViewDriver';
import {Driver} from './driver/Driver';
import {KeyboardDriver} from './driver/KeyboardDriver';

function KeyboardChat() {
  const inputRef = useRef<TextInput>(null);
  const senderRef = useRef<View>(null);
  const [bottom, setBottom] = useState(0);

  const onLayout = useCallback(() => {
    const viewTag = findNodeHandle(senderRef.current);
    if (viewTag === null) {
      return;
    }

    getEdgeInsetsForView(viewTag, insets => {
      setBottom(insets.bottom!);
    });
  }, []);

  const emoji = useRef(new ViewDriver('emoji')).current;
  const toolbox = useRef(new ViewDriver('toolbox')).current;
  const keyboard = useRef(new KeyboardDriver(inputRef)).current;

  const [driver, setDriver] = useState<Driver>();
  const [translateY, setTranslateY] = useState(new Animated.Value(0));
  const driverState = {bottom, driver, setDriver, setTranslateY};

  const mainStyle = {
    transform: [
      {
        translateY: translateY,
      },
    ],
  };

  return (
    <SafeAreaProvider style={styles.provider}>
      <KeyboardInsetsView
        onKeyboard={keyboard.createCallback(driverState)}
        style={[styles.fill, mainStyle]}>
        <ScrollView showsVerticalScrollIndicator={false} style={styles.inverted}>
          <View style={styles.inverted}>
            {history.map((message, index) => (
              <Message key={index} {...message} />
            ))}
          </View>
        </ScrollView>
        <View style={styles.sender} ref={senderRef} onLayout={onLayout}>
          <TextInput ref={inputRef} style={styles.input} multiline textAlignVertical="center" />
          <Pressable
            style={styles.button}
            onPress={() => (emoji.shown ? keyboard.show() : emoji.show(driverState))}>
            <Image
              source={emoji.shown ? require('./icon/keyboard.png') : require('./icon/emoji.png')}
            />
          </Pressable>
          <Pressable style={styles.button} onPress={() => toolbox.toggle(driverState)}>
            <Image source={require('./icon/plus.png')} />
          </Pressable>
        </View>
      </KeyboardInsetsView>
      <SafeAreaView edges={['bottom']} />

      <Animated.View style={[styles.absolute, styles.red, emoji.style]} onLayout={emoji.onLayout}>
        <View style={styles.emoji}>
          <Text style={styles.text}>表情包</Text>
        </View>
        <SafeAreaView edges={['bottom']} />
      </Animated.View>
      <Animated.View
        style={[styles.absolute, styles.blue, toolbox.style]}
        onLayout={toolbox.onLayout}>
        <View style={styles.toolbox}>
          <Text style={styles.text}>工具箱</Text>
        </View>
        <SafeAreaView edges={['bottom']} />
      </Animated.View>
    </SafeAreaProvider>
  );
}

export default withNavigationItem({
  fitsOpaqueNavigationBarAndroid: false, // 透明导航栏
  titleItem: {
    title: '聊天键盘处理',
  },
})(KeyboardChat);

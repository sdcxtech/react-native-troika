import {StyleSheet} from 'react-native';
import {FlashList} from '@shopify/flash-list';
import React, {useState} from 'react';

import MessageType from './models/MessageType';
import initialMessages from './data/messages';
import TextInputBar from './TextInputBar';
import userName from './userName';
import MessageItem from './MessageItem';
import Message from './models/Message';
import {KeyboardInsetsView} from '@sdcx/keyboard-insets';

const Messages = () => {
  const [messages, setMessages] = useState(initialMessages);

  const appendMessage = (text: string) => {
    const message = {
      id: Math.floor(Math.random() * 1000000).toString(),
      text,
      sender: userName,
      type: MessageType.Text,
    } as Message;
    setMessages([message, ...messages]);
  };

  return (
    <KeyboardInsetsView style={styles.keyboardAvoidingViewStyles} extraHeight={8}>
      <FlashList
        nestedScrollEnabled
        renderItem={MessageItem}
        inverted
        estimatedItemSize={100}
        keyExtractor={item => {
          return item.id;
        }}
        overrideItemLayout={(layout, item) => {
          switch (item.type) {
            case MessageType.Image:
              layout.size = 200;
              break;
          }
        }}
        getItemType={item => {
          return item.type;
        }}
        data={messages}
      />
      <TextInputBar
        onSend={text => {
          appendMessage(text);
        }}
      />
    </KeyboardInsetsView>
  );
};

const styles = StyleSheet.create({
  keyboardAvoidingViewStyles: {
    flex: 1,
    backgroundColor: 'white',
  },
});

export default Messages;

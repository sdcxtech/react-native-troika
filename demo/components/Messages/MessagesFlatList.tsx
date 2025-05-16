import {StyleSheet, FlatList} from 'react-native';
import React, {useState} from 'react';

import MessageType from './models/MessageType';
import initialMessages from './data/messages';
import TextInputBar from './TextInputBar';
import userName from './userName';
import MessageItem from './MessageItem';
import Message from './models/Message';
import {KeyboardInsetsView} from '@sdcx/keyboard-insets';

const MessagesFlatList = () => {
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
      <FlatList
        renderItem={MessageItem}
        inverted
        data={messages}
        style={styles.list}
        keyExtractor={item => {
          return item.id;
        }}
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
  list: {
    flex: 1,
  },
});

export default MessagesFlatList;

import React, { useState } from 'react'
import { useNavigator, withNavigationItem } from 'hybrid-navigation'
import { Image, Pressable, StyleSheet, Text, TextInput, View } from 'react-native'
import { KeyboardInsetsView } from '@sdcx/keyboard-insets'
import { BottomModal } from '../BottomModal'
import PrimaryButton from '../../components/PrimaryButton'

function BottomModalTextInput() {
  const navigator = useNavigator()
  const [visible, setVisible] = useState(true)
  const [text, setText] = useState('')

  const closeModal = () => {
    setVisible(false)
  }

  const onClose = () => {
    navigator.hideModal()
  }

  return (
    <BottomModal fitToContents visible={visible} onClose={onClose} modalContentStyle={styles.modal}>
      <Pressable style={StyleSheet.absoluteFill} onPress={closeModal} />
      <KeyboardInsetsView style={styles.content} extraHeight={80}>
        <View style={styles.header}>
          <Text style={styles.title}>BottomModal</Text>
          <Pressable style={styles.close} onPress={closeModal}>
            <Image source={require('../../assets/window_closed.png')} />
          </Pressable>
        </View>
        <TextInput style={styles.input} value={text} onChangeText={setText} autoFocus />
        <PrimaryButton style={styles.button} text="保存" />
      </KeyboardInsetsView>
    </BottomModal>
  )
}

const styles = StyleSheet.create({
  modal: {
    position: 'relative',
    flex: 1,
    justifyContent: 'flex-end',
  },
  content: {
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 16,
    borderTopRightRadius: 16,
    paddingBottom: 32,
  },
  header: {
    flexDirection: 'row',
    height: 60,
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    color: '#292F33',
    fontSize: 18,
  },
  close: {
    position: 'absolute',
    right: 16,
  },
  input: {
    height: 48,
    backgroundColor: '#F2F5F7',
    marginHorizontal: 16,
    paddingHorizontal: 16,
    borderRadius: 12,
    color: '#292F33',
    fontSize: 14,
  },
  button: {
    marginTop: 24,
    marginHorizontal: 16,
  },
})

export default withNavigationItem({
  navigationBarColorAndroid: '#FFFFFF',
})(BottomModalTextInput)

import React, { useState } from 'react'
import { useNavigator, withNavigationItem } from 'hybrid-navigation'
import { StyleSheet, Text, View } from 'react-native'
import { KeyboardInsetsView } from '@sdcx/keyboard-insets'
import { Modal } from '../../components/Modal'
import CodeInput from '../../components/CondeInput'
import { ModalToolbar } from '../../components/ModalToolbar'

function ModalTextInput() {
  const [text, setText] = useState('')
  const navigator = useNavigator()

  const onConfirm = () => {
    navigator.hideModal()
  }

  return (
    <KeyboardInsetsView style={styles.container} extraHeight={100}>
      <Modal style={styles.modal} cancelable={false}>
        <Text style={styles.title}>输入验证码</Text>
        <View style={styles.content}>
          <CodeInput value={text} onValueChange={setText} />
        </View>
        <ModalToolbar onCancel={() => navigator.hideModal()} onConfirm={onConfirm} />
      </Modal>
    </KeyboardInsetsView>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  modal: {
    minHeight: 192,
  },
  title: {
    color: '#292F33',
    fontSize: 14,
    marginTop: 20,
    paddingHorizontal: 32,
    textAlign: 'center',
  },
  content: {
    justifyContent: 'center',
    flex: 1,
  },
})

export default withNavigationItem({
  //
})(ModalTextInput)

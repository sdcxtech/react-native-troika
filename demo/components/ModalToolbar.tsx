import React from 'react'
import { StyleSheet, View } from 'react-native'
import ModalButton from './ModalButton'

interface ModalToolbarProps {
  onCancel?: () => void
  onConfirm?: () => void
}

export function ModalToolbar(props: ModalToolbarProps) {
  const { onCancel, onConfirm } = props
  return (
    <View style={styles.buttons}>
      <ModalButton key="cancel" text="Cancel" textStyle={styles.textCancel} onPress={onCancel} />
      <ModalButton
        key="confirm"
        text="Confirm"
        onPress={onConfirm}
        textStyle={styles.textConfirm}
        buttonStyle={styles.divider}
      />
    </View>
  )
}

const styles = StyleSheet.create({
  divider: {
    borderLeftColor: '#DFE3E579',
    borderLeftWidth: 1,
  },
  textCancel: {
    color: '#292F33',
  },
  textConfirm: {
    color: '#1A9EFF',
  },
  buttons: {
    borderTopColor: '#DFE3E579',
    borderTopWidth: 1,
    flexDirection: 'row',
    height: 48,
  },
})

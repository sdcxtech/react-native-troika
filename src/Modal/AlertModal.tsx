import React from 'react'
import { Modal, View, Text, StyleSheet, StyleProp, ViewStyle, TextStyle } from 'react-native'
import TextButton from '../TextButton'

interface AlertModalProps {
  visible: boolean
  title?: string
  msg?: string
  positiveText?: string
  style?: StyleProp<ViewStyle>
  contentStyle?: StyleProp<ViewStyle>
  titleStyle?: StyleProp<TextStyle>
  msgStyle?: StyleProp<TextStyle>
  positiveTextStyle?: StyleProp<TextStyle>
  onNegativePress?: () => void
  onPositivePress?: () => void
}

export default function AlertModal({
  visible,
  style,
  contentStyle,
  titleStyle,
  msgStyle,
  positiveTextStyle,
  positiveText,
  title,
  msg,
  onNegativePress,
  onPositivePress,
}: AlertModalProps) {
  const both = !!title && !!msg

  return (
    <Modal visible={visible} transparent statusBarTranslucent presentationStyle="overFullScreen" animationType="fade">
      <View style={styles.bezel}>
        <View style={[styles.modal, style]}>
          <View style={[styles.content, contentStyle]}>
            {!!title && (
              <Text style={[styles.title, { marginTop: 24, marginBottom: both ? 0 : 24 }, titleStyle]}>{title}</Text>
            )}
            {!!msg && (
              <Text style={[styles.msg, { marginTop: both ? 12 : 24, marginBottom: 24 }, msgStyle]}>{msg}</Text>
            )}
          </View>
          <View style={styles.buttons}>
            <TextButton onPress={onNegativePress} style={styles.button} textStyle={styles.negativeText} title="取消" />
            <View style={styles.divider} />
            <TextButton
              onPress={onPositivePress}
              style={styles.button}
              textStyle={[styles.positiveText, positiveTextStyle]}
              title={positiveText ?? '确定'}
            />
          </View>
        </View>
      </View>
    </Modal>
  )
}

const styles = StyleSheet.create({
  bezel: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#00000088',
  },
  modal: {
    backgroundColor: '#FFFFFF',
    borderRadius: 16,
    width: 280,
    alignItems: 'center',
    overflow: 'hidden',
    justifyContent: 'flex-end',
  },
  content: {
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    color: '#333333',
    fontSize: 20,
    fontWeight: 'bold',
  },
  msg: {
    fontSize: 15,
    lineHeight: 21,
    color: '#666666',
    fontWeight: '500',
    marginHorizontal: 28,
    textAlign: 'center',
  },
  buttons: {
    width: '100%',
    height: 60,
    flexDirection: 'row',
    alignItems: 'stretch',
    borderTopColor: '#DDDDDD',
    borderTopWidth: 1,
  },
  divider: {
    backgroundColor: '#DDDDDD',
    width: 1,
  },
  button: {
    flex: 1,
    flexGrow: 1,
  },
  negativeText: {
    color: '#333333',
    fontSize: 16,
  },
  positiveText: {
    color: '#57DE9E',
    fontSize: 16,
  },
})

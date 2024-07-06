import React, { useState } from 'react'
import { useNavigator, withNavigationItem } from 'hybrid-navigation'
import { FlatList, Image, ListRenderItem, Pressable, StyleSheet, Text, View } from 'react-native'
import { BottomModal } from '../BottomModal'

const data = [
  '1',
  '2',
  '3',
  '4',
  '5',
  '6',
  '7',
  '8',
  '9',
  '10',
  '11',
  '12',
  '13',
  '14',
  '15',
  '16',
  '17',
  '18',
  '19',
  '20',
]

function BottomModalFlatList() {
  const navigator = useNavigator()

  const [visible, setVisible] = useState(true)

  const onModalClose = () => {
    navigator.hideModal()
  }

  const onClosePress = () => {
    setVisible(false)
  }

  const renderItem: ListRenderItem<string> = info => {
    return (
      <Pressable style={[styles.item, info.index === 0 ? styles.divider : null]}>
        <Text style={styles.name}>{info.item}</Text>
      </Pressable>
    )
  }

  return (
    <BottomModal
      visible={visible}
      onClose={onModalClose}
      style={styles.modal}
      modalContentStyle={styles.content}>
      <View style={styles.header}>
        <Text style={styles.title}>选择数据</Text>
        <Pressable style={styles.close} onPress={onClosePress}>
          <Image source={require('../../assets/window_closed.png')} />
        </Pressable>
      </View>
      <FlatList
        style={styles.list}
        contentContainerStyle={styles.listContent}
        data={data}
        renderItem={renderItem}
        keyExtractor={item => item}
      />
    </BottomModal>
  )
}

const styles = StyleSheet.create({
  modal: {
    top: '20%',
  },
  content: {
    backgroundColor: '#FFFFFF',
    borderTopLeftRadius: 16,
    borderTopRightRadius: 16,
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
  list: {
    //backgroundColor: 'red',
  },
  listContent: {
    paddingHorizontal: 16,
  },
  item: {
    flexDirection: 'row',
    height: 48,
    alignItems: 'center',
    justifyContent: 'flex-end',
    borderBottomColor: '#DFE3E5',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  divider: {
    borderTopColor: '#DFE3E5',
    borderTopWidth: StyleSheet.hairlineWidth,
  },
  name: { position: 'absolute', left: 0 },
})

export default withNavigationItem({
  // forceTransparentDialogWindow: true,
  // screenBackgroundColor: '#33000000',
  navigationBarColorAndroid: '#FFFFFF',
})(BottomModalFlatList)

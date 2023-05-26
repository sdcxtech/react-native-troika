import React from 'react'
import {
  StyleSheet,
  FlatList,
  ListRenderItem,
  Text,
  TouchableOpacity,
  Image,
  View,
} from 'react-native'
import { useNavigator, withNavigationItem } from 'hybrid-navigation'
import Lottie from 'lottie-react-native'
interface Item {
  title: string
  routeName: string
}

const data: Array<Item> = [
  {
    title: 'NestedScroll',
    routeName: 'NestedScroll',
  },
  {
    title: 'PullToRefresh',
    routeName: 'PullToRefresh',
  },
  {
    title: 'BottomSheet',
    routeName: 'BottomSheet',
  },
  {
    title: 'Keyboard',
    routeName: 'Keyboard',
  },
  {
    title: '悬浮球',
    routeName: 'HoverBall',
  },
  {
    title: 'ImageCrop',
    routeName: 'ImageCropDemo',
  },
  {
    title: '菊花 Loading',
    routeName: 'ActivityIndicator',
  },
]

function App() {
  const navigator = useNavigator()

  const renderListItem: ListRenderItem<Item> = ({ item }) => {
    return <ListItem {...item} onPress={() => navigator.push(item.routeName)} />
  }

  return (
    <FlatList
      ListHeaderComponent={Header}
      data={data}
      keyExtractor={item => item.title}
      renderItem={renderListItem}
    />
  )
}

function Header() {
  return (
    <View style={styles.header}>
      <Lottie
        style={styles.lottie}
        source={require('assets/trilo-3.json')}
        autoPlay
        loop
        speed={1}
      />
      <Lottie
        style={styles.lottie}
        source={require('assets/trilo-4.json')}
        autoPlay
        loop
        speed={1}
      />
      <Lottie
        style={styles.lottie}
        source={require('assets/trilo-5.json')}
        autoPlay
        loop
        speed={1}
      />
    </View>
  )
}

interface ListItemProps {
  title: string
  onPress?: () => void
}

function ListItem({ title, onPress }: ListItemProps) {
  return (
    <TouchableOpacity style={styles.item} onPress={onPress}>
      <Text style={styles.text}>{title}</Text>
      <Image source={require('assets/indicator.png')} />
    </TouchableOpacity>
  )
}

export default withNavigationItem({
  titleItem: {
    title: 'MyUiDemo',
  },
})(App)

const styles = StyleSheet.create({
  header: {
    flexDirection: 'row',
    justifyContent: 'center',
    borderBottomWidth: 1,
    borderBottomColor: '#EEEEEE',
    paddingVertical: 12,
  },
  lottie: {
    height: 100,
  },
  item: {
    height: 60,
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
    borderBottomWidth: 1,
    borderBottomColor: '#EEEEEE',
    paddingLeft: 16,
    paddingRight: 16,
  },
  text: {
    color: '#222222',
    fontSize: 17,
  },
})

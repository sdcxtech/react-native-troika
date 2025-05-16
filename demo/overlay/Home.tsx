import React from 'react';
import {StyleSheet, FlatList, ListRenderItem, Text, TouchableOpacity, Image} from 'react-native';
import {useNavigator, withNavigationItem} from 'hybrid-navigation';

interface Item {
  title: string;
  routeName: string;
}

const data: Array<Item> = [
  {
    title: 'Hoverball',
    routeName: 'Hoverball',
  },
  {
    title: 'Toast',
    routeName: 'Toast',
  },
  {
    title: 'Alert',
    routeName: 'Alert',
  },
];

function Home() {
  const navigator = useNavigator();

  const renderListItem: ListRenderItem<Item> = ({item}) => {
    return <ListItem {...item} onPress={() => navigator.push(item.routeName)} />;
  };

  return <FlatList data={data} keyExtractor={item => item.title} renderItem={renderListItem} />;
}

interface ListItemProps {
  title: string;
  onPress?: () => void;
}

function ListItem({title, onPress}: ListItemProps) {
  return (
    <TouchableOpacity style={styles.item} onPress={onPress}>
      <Text style={styles.text}>{title}</Text>
      <Image source={require('assets/indicator.png')} />
    </TouchableOpacity>
  );
}

export default withNavigationItem({
  titleItem: {
    title: 'Overlay',
  },
})(Home);

const styles = StyleSheet.create({
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
});

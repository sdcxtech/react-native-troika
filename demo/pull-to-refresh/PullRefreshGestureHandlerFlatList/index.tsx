import { withNavigationItem } from 'hybrid-navigation';
import React, { useRef, useState } from 'react';
import { StyleSheet, Text, TouchableHighlight, View } from 'react-native';
import { GestureHandlerRootView, FlatList } from 'react-native-gesture-handler';
import { RefreshControl } from '@sdcx/pull-to-refresh';

const FLATLIST_DATA = Array(40)
	.fill(Math.random() + '')
	.map((item, index) => ({
		id: item + index,
		title: `index: ${index} `,
	}));

const generateFlatlistItem = (index: number, extra: string) => ({
	id: Math.random() + '' + index,
	title: `${extra} index ${index}`,
});

function useDemoFlatlistData() {
	const [flatlistData, setFlatlistData] = useState(FLATLIST_DATA);
	return {
		flatlistData,
		addFlatlistRefreshItem: () =>
			setFlatlistData(data => [generateFlatlistItem(data.length, 'refresh'), ...data]),
		addFlatlistLoadMoreItem: () =>
			setFlatlistData([
				...flatlistData,
				generateFlatlistItem(flatlistData.length, 'load more'),
			]),
	};
}

function GestureHandlerFlatList() {
	const [refreshing, setRefreshing] = useState(false);

	const { flatlistData, addFlatlistRefreshItem } = useDemoFlatlistData();
	const pendingAction = useRef<ReturnType<typeof setTimeout> | null>(null);

	const clearPendingAction = () => {
		if (pendingAction.current) {
			clearTimeout(pendingAction.current);
		}
	};

	const beginRefresh = async () => {
		setRefreshing(true);
		pendingAction.current = setTimeout(() => {
			addFlatlistRefreshItem();
			endRefresh();
		}, 2000);
	};

	const endRefresh = () => {
		clearPendingAction();
		setRefreshing(false);
	};

	const renderItem = ({ item }: { item: { title: string } }) => <Item title={item.title} />;

	return (
		<GestureHandlerRootView>
			<FlatList
				refreshControl={<RefreshControl refreshing={refreshing} onRefresh={beginRefresh} />}
				onLayout={e => console.log('flatlist', e.nativeEvent.layout.height)}
				contentContainerStyle={{ flexGrow: 1 }}
				data={flatlistData}
				renderItem={renderItem}
				keyExtractor={item => item.id}
				nestedScrollEnabled
			/>
		</GestureHandlerRootView>
	);
}
const Item = ({ title }: { title: string }) => {
	const [clickCount, setClickCount] = useState(0);
	return (
		<TouchableHighlight onPress={() => setClickCount(v => v + 1)} underlayColor="#DDDDDD">
			<View style={styles.item}>
				<Text style={styles.title}>
					{title} {clickCount}
				</Text>
			</View>
		</TouchableHighlight>
	);
};

export default withNavigationItem({
	titleItem: {
		title: 'PullRefresh + GestureHandlerFlatList',
	},
})(GestureHandlerFlatList);

const styles = StyleSheet.create({
	item: {
		backgroundColor: '#f9c2ff',
		padding: 20,
		marginVertical: 8,
		marginHorizontal: 16,
	},
	title: {
		fontSize: 32,
	},
});

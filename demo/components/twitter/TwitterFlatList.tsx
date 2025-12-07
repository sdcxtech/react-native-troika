import React, { useRef, useState } from 'react';
import { FlatList } from 'react-native';

import TweetCell from './TweetCell';
import { Header, Divider, Empty, Footer } from './Twitter';

import { tweets as tweetsData } from './data/tweets';
import { RefreshControl } from '@sdcx/pull-to-refresh';

const TwitterFlatList = () => {
	const emptyListEnabled = false;

	const [refreshing, setRefreshing] = useState(false);
	const [noMoreData, setNoMoreData] = useState(false);
	const remainingTweets = useRef([...tweetsData].splice(10, tweetsData.length));
	const [tweets, setTweets] = useState([...tweetsData].splice(0, 10));
	return (
		<FlatList
			testID="FlatList"
			nestedScrollEnabled
			keyExtractor={item => {
				return item.id;
			}}
			renderItem={({ item }) => {
				return <TweetCell tweet={item} />;
			}}
			ListHeaderComponent={Header}
			ListHeaderComponentStyle={{ backgroundColor: '#ccc' }}
			refreshControl={
				<RefreshControl
					refreshing={refreshing}
					onRefresh={() => {
						setRefreshing(true);
						setTimeout(() => {
							setRefreshing(false);
							const reversedTweets = [...tweets];
							reversedTweets.reverse();
							setTweets(reversedTweets);
						}, 500);
					}}
				/>
			}
			onEndReached={() => {
				setTimeout(() => {
					setTweets([...tweets, ...remainingTweets.current.splice(0, 10)]);
					if (remainingTweets.current.length === 0) {
						setNoMoreData(true);
					}
				}, 1000);
			}}
			ListFooterComponent={
				<Footer
					isLoading={tweets.length !== tweetsData.length}
					isPagingEnabled={!noMoreData}
				/>
			}
			ItemSeparatorComponent={Divider}
			ListEmptyComponent={Empty()}
			data={emptyListEnabled ? [] : tweets}
			viewabilityConfig={{
				waitForInteraction: true,
				itemVisiblePercentThreshold: 50,
				minimumViewTime: 1000,
			}}
			// onViewableItemsChanged={info => {
			//   console.log(info)
			// }}
		/>
	);
};

export default TwitterFlatList;

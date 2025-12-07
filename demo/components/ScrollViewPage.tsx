import React, { useState } from 'react';
import { Image, ScrollView, StyleSheet, Switch, Text, TouchableOpacity, View } from 'react-native';

export function ScrollViewPage() {
	const [on, setOn] = useState(false);
	return (
		<ScrollView nestedScrollEnabled>
			<View style={styles.item}>
				<TouchableOpacity onPress={() => console.log('按压标题')}>
					<Text style={styles.label}>标题</Text>
				</TouchableOpacity>
				<TouchableOpacity
					onPress={() => {
						console.log('switch');
						setOn(!on);
					}}
				>
					<Switch value={on} onValueChange={setOn} />
				</TouchableOpacity>
			</View>
			<View style={styles.author}>
				<Image style={styles.avatar} source={require('assets/avatar-1.png')} />
				<View style={styles.meta}>
					<Text style={styles.name}>Knowledge Bot</Text>
					<Text style={styles.timestamp}>1st Jan 2025</Text>
				</View>
			</View>
			<Text style={styles.title}>Lorem Ipsum</Text>
			<Image style={styles.image} source={require('assets/book.jpg')} />
			<Text style={styles.paragraph}>
				Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a
				piece of classical Latin literature from 45 BC, making it over 2000 years old.
			</Text>
		</ScrollView>
	);
}

const styles = StyleSheet.create({
	container: {
		backgroundColor: 'white',
	},
	author: {
		flexDirection: 'row',
		marginVertical: 8,
		marginHorizontal: 16,
	},
	meta: {
		marginHorizontal: 8,
		justifyContent: 'center',
	},
	name: {
		color: '#000',
		fontWeight: 'bold',
		fontSize: 16,
		lineHeight: 24,
	},
	timestamp: {
		color: '#999',
		fontSize: 14,
		lineHeight: 21,
	},
	avatar: {
		height: 48,
		width: 48,
		borderRadius: 24,
	},
	title: {
		color: '#000',
		fontWeight: 'bold',
		fontSize: 36,
		marginVertical: 8,
		marginHorizontal: 16,
	},
	paragraph: {
		color: '#000',
		fontSize: 16,
		lineHeight: 24,
		marginVertical: 8,
		marginHorizontal: 16,
	},
	image: {
		width: '100%',
		height: 200,
		resizeMode: 'cover',
		marginVertical: 8,
	},
	item: {
		paddingRight: 20,
		paddingLeft: 16,
		backgroundColor: '#f9c2ff',
		flexDirection: 'row',
		justifyContent: 'space-between',
		alignItems: 'center',
		height: 60,
	},
	label: {
		color: '#1D2023',
		fontSize: 18,
	},
});

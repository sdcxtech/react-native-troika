import React from 'react';
import { StyleSheet, Text } from 'react-native';

interface TitleProps {
	title: string;
}

export default function Title({ title }: TitleProps) {
	return <Text style={styles.title}>{title}</Text>;
}

const styles = StyleSheet.create({
	title: {
		color: '#1D2023',
		fontSize: 17,
		fontWeight: 'bold',
		lineHeight: 26,
		alignSelf: 'center',
		marginVertical: 6,
	},
});

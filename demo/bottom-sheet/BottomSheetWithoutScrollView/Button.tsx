import React from 'react';
import { StyleSheet, Text, TouchableOpacity } from 'react-native';

interface ButtonProps {
	onPress?: () => void;
	text: string;
}

export default function Button({ onPress, text }: ButtonProps) {
	return (
		<TouchableOpacity onPress={onPress} style={styles.button}>
			<Text style={styles.text}>{text}</Text>
		</TouchableOpacity>
	);
}

const styles = StyleSheet.create({
	button: {
		marginRight: 16,
		justifyContent: 'center',
	},
	text: {
		fontSize: 16,
		color: '#FFFFFF',
	},
});

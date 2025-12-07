import React from 'react';
import { Pressable, StyleProp, StyleSheet, Text, TextStyle, ViewStyle } from 'react-native';

interface ModalButtonProps {
	text: string;
	onPress?: () => void;
	buttonStyle?: StyleProp<ViewStyle>;
	textStyle?: StyleProp<TextStyle>;
}

export default function ModalButton(props: ModalButtonProps) {
	const { text, onPress, buttonStyle, textStyle } = props;
	return (
		<Pressable style={[styles.button, buttonStyle]} onPress={onPress}>
			<Text style={[styles.text, textStyle]}>{text}</Text>
		</Pressable>
	);
}

const styles = StyleSheet.create({
	button: {
		flex: 1,
		alignItems: 'center',
		justifyContent: 'center',
	},

	text: {
		fontSize: 18,
	},
});

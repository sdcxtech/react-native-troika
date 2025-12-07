import React, { PropsWithChildren } from 'react';
import { useNavigator } from 'hybrid-navigation';
import { Pressable, StyleProp, StyleSheet, View, ViewStyle } from 'react-native';

interface ModalProps {
	cancelable?: boolean;
	style?: StyleProp<ViewStyle>;
}

export function Modal(props: PropsWithChildren<ModalProps>) {
	const { cancelable = true, style, children } = props;

	const navigator = useNavigator();

	const onPress = () => {
		if (cancelable) {
			navigator.hideModal();
		}
	};

	return (
		<View style={styles.container}>
			<Pressable style={styles.overlay} onPress={onPress} />
			<View style={[styles.box, style]}>{children}</View>
		</View>
	);
}

const styles = StyleSheet.create({
	container: {
		flex: 1,
		justifyContent: 'center',
	},
	overlay: {
		position: 'absolute',
		left: 0,
		right: 0,
		top: 0,
		bottom: 0,
	},
	box: {
		backgroundColor: '#FFFFFF',
		borderRadius: 12,
		marginHorizontal: 48,
		minHeight: 160,
		overflow: 'hidden',
	},
});

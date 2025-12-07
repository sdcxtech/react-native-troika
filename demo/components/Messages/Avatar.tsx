import React from 'react';
import { Image, StyleSheet } from 'react-native';

interface AvatarProps {
	avatar?: string;
}

const Avatar = ({ avatar }: AvatarProps) => {
	if (avatar === undefined) {
		return null;
	}
	return <Image style={styles.avatar} source={{ uri: avatar }} />;
};

export default Avatar;

const styles = StyleSheet.create({
	avatar: {
		height: 30,
		width: 30,
		borderRadius: 15,
		margin: 8,
		marginRight: 0,
		marginTop: 0,
		flexShrink: 0,
	},
});

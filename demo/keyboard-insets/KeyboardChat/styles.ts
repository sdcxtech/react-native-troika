import { StyleSheet } from 'react-native';

export default StyleSheet.create({
	provider: {
		backgroundColor: '#FFFFFF',
	},
	fill: {
		flex: 1,
	},
	header: {
		marginRight: 12,
	},
	inverted: {
		transform: [
			{
				scaleY: -1,
			},
		],
	},
	sender: {
		flexDirection: 'row',
		backgroundColor: 'white',
		alignItems: 'center',
		borderTopColor: '#DDDDDD',
		borderTopWidth: 1,
		paddingRight: 6,
		paddingVertical: 6,
	},
	input: {
		borderWidth: 1,
		borderColor: '#DDDDDD',
		backgroundColor: '#F5F5F5',
		flex: 1,
		minHeight: 36,
		maxHeight: 80,
		fontSize: 14,
		paddingTop: 10,
		paddingBottom: 10,
		paddingHorizontal: 6,
		marginHorizontal: 6,
	},
	button: {
		width: 36,
		height: 36,
		justifyContent: 'center',
		alignItems: 'center',
	},
	absolute: {
		position: 'absolute',
		bottom: 0,
		left: 0,
		right: 0,
	},
	red: {
		backgroundColor: 'cadetblue',
	},
	emoji: {
		height: 300,
		justifyContent: 'center',
		alignItems: 'center',
	},
	blue: {
		backgroundColor: 'darkkhaki',
	},
	toolbox: {
		height: 200,
		justifyContent: 'center',
		alignItems: 'center',
	},
	text: {
		fontSize: 48,
		color: 'darkgray',
	},
});

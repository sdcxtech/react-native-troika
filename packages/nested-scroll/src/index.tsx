import React, { PropsWithChildren } from 'react';
import { Platform, StyleProp, StyleSheet, ViewStyle } from 'react-native';
import NestedScrollViewNativeComponent from './NestedScrollViewNativeComponent';
import NestedScrollViewContent from './NestedScrollViewContent';
import NestedScrollViewChild from './NestedScrollViewChild';
import NestedScrollViewHeader from './NestedScrollViewHeader';

export type NestedScrollViewProps = PropsWithChildren<{
	style?: StyleProp<ViewStyle>;
	//Android Only
	contentContainerStyle?: StyleProp<ViewStyle>;
	//iOS Only
	bounces?: boolean;
}>;

function NestedScrollView({ children, style, ...props }: NestedScrollViewProps) {
	if (React.Children.count(children) > 2) {
		console.error('NestedScrollView can only accept two child views.');
	}

	if (Platform.OS === 'android') {
		return (
			<NestedScrollViewAndroid style={[styles.fill, style]} {...props}>
				{children}
			</NestedScrollViewAndroid>
		);
	}

	return (
		<NestedScrollViewNativeComponent style={[styles.fill, style]} {...props}>
			{React.Children.map(children, (child, index) => {
				if (index === 0) {
					return child;
				}
				return <NestedScrollViewChild collapsable={false}>{child}</NestedScrollViewChild>;
			})}
		</NestedScrollViewNativeComponent>
	);
}

export function NestedScrollViewAndroid({
	children,
	contentContainerStyle,
	...props
}: NestedScrollViewProps) {
	return (
		<NestedScrollViewNativeComponent {...props}>
			<NestedScrollViewContent
				style={[styles.content, contentContainerStyle]}
				collapsable={false}
			>
				{React.Children.map(children, (child, index) => {
					if (index === 0) {
						return child;
					}
					return (
						<NestedScrollViewChild collapsable={false}>{child}</NestedScrollViewChild>
					);
				})}
			</NestedScrollViewContent>
		</NestedScrollViewNativeComponent>
	);
}

const styles = StyleSheet.create({
	fill: {
		flex: 1,
	},
	content: {
		flex: 1,
	},
});

export { NestedScrollView, NestedScrollViewHeader };

export type { NestedScrollEvent, NestedScrollViewHeaderProps } from './NestedScrollViewHeader';

import React, { useCallback, useMemo, useRef, useState } from 'react';
import { Animated, StyleProp, StyleSheet, TextStyle, View, ViewStyle } from 'react-native';
import TabBarIndicator from './TabBarIndicator';
import TabBarItem from './TabBarItem';

export interface TabBarProps {
	tabs: string[];
	onTabPress: (index: number) => void;
	onTabsLayout?: (layouts: Layout[]) => void;
	position: Animated.Value;
	offset: Animated.Value;
	page: number;
	isIdle: boolean;
	spacing?: number;
	style?: StyleProp<ViewStyle>;
	tabStyle?: StyleProp<ViewStyle>;
	labelStyle?: StyleProp<TextStyle>;
	indicatorStyle?: StyleProp<ViewStyle>;
}

interface Layout {
	x: number;
	y: number;
	width: number;
	height: number;
}

export default function TabBar({
	tabs,
	onTabPress,
	onTabsLayout,
	position,
	offset,
	isIdle,
	spacing = 8,
	style,
	tabStyle,
	labelStyle,
	indicatorStyle,
}: TabBarProps) {
	const inputRange = useMemo(() => tabs.map((_, index) => index), [tabs]);
	const [outputRange, setOutputRange] = useState(inputRange.map(() => 0));
	const indicatorWidth = getIndicatorWidth(indicatorStyle);
	const layouts = useRef<Layout[]>([]).current;

	const handleTabLayout = useCallback(
		(index: number, layout: Layout) => {
			layouts[index] = layout;

			const length = layouts.filter(layout => layout.width > 0).length;
			if (length !== tabs.length) {
				return;
			}

			const range: number[] = [];
			for (let index = 0; index < length; index++) {
				const { x, width } = layouts[index];
				// 我们希望指示器和所选 Tab 垂直居中对齐
				// 那么指示器的 x 轴偏移量就是 Tab 的 center.x - 指示器的 center.x
				const tabCenterX = x + width / 2;
				const indicatorCenterX = indicatorWidth / 2;
				range.push(tabCenterX - indicatorCenterX);
			}

			console.log('---------------onTabLayout-------------------');
			setOutputRange(range);
			onTabsLayout?.(layouts);
		},
		[onTabsLayout, tabs, layouts, indicatorWidth],
	);

	const scrollX = useMemo(
		() =>
			Animated.add(offset, position).interpolate({
				inputRange,
				outputRange,
			}),
		[offset, position, inputRange, outputRange],
	);

	const handleTabPress = useCallback(
		(index: number) => {
			if (isIdle) {
				onTabPress?.(index);
			} else {
				console.log('不空闲，无法点击');
			}
		},
		[onTabPress, isIdle],
	);

	return (
		<View style={[styles.tabbar, style]}>
			{tabs.map((tab: string, index: number) => {
				return (
					<TabBarItem
						key={tab}
						title={tab}
						onPress={() => handleTabPress(index)}
						onLayout={event => handleTabLayout(index, event.nativeEvent.layout)}
						style={[tabStyle, { marginLeft: index === 0 ? 0 : spacing }]}
						labelStyle={[labelStyle]}
					/>
				);
			})}
			<TabBarIndicator style={[styles.indicator, indicatorStyle]} scrollX={scrollX} />
		</View>
	);
}

function getIndicatorWidth(indicatorStyle: StyleProp<ViewStyle>) {
	const { width } = StyleSheet.flatten([styles.indicator, indicatorStyle]);
	if (typeof width === 'number') {
		return width;
	}
	return styles.indicator.width;
}

const styles = StyleSheet.create({
	tabbar: {
		flexDirection: 'row',
		height: 48,
		alignItems: 'center',
		backgroundColor: '#FFFFFF',
	},
	indicator: {
		width: 24,
	},
});

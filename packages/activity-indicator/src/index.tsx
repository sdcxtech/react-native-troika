import React from 'react';
import { ActivityIndicator as ActivityIndicatorIOS, Platform, ViewProps } from 'react-native';
import ActivityIndicatorAndroid from './ActivityIndicatorAndroid';

interface ActivityIndicatorProps extends ViewProps {
	size?: 'small' | 'large';
	color?: string;
	animating?: boolean;
}

const ActivityIndicator = (props: ActivityIndicatorProps) => {
	if (Platform.OS === 'ios') {
		return <ActivityIndicatorIOS hidesWhenStopped={false} {...props} />;
	} else {
		return <ActivityIndicatorAndroid {...props} />;
	}
};

export default ActivityIndicator;

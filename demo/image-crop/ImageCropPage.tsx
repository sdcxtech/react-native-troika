import React, { useCallback, useEffect, useRef } from 'react';
import { NavigationProps, withNavigationItem } from 'hybrid-navigation';
import { StyleSheet, View } from 'react-native';
import ImageCropView, { ObjectRect, OnCropEvent, ImageCropViewInstance } from '@sdcx/image-crop';
import Navigation from 'hybrid-navigation';

interface Props extends NavigationProps {
	fileUri: string;
	cropStyle?: 'circular' | 'default';
	objectRect?: ObjectRect;
}

function ImageCropPage({ fileUri, cropStyle, objectRect, sceneId, navigator }: Props) {
	const cropViewRef = useRef<ImageCropViewInstance>(null);
	useEffect(() => {
		Navigation.setRightBarButtonItem(sceneId, {
			title: '确认',
			action: () => {
				console.log('裁剪确认');
				cropViewRef.current?.crop();
			},
		});
	}, [sceneId]);

	const onCropped = useCallback(
		({ nativeEvent }: OnCropEvent) => {
			console.log('RN获取到剪切成功的uri = ', nativeEvent.uri);
			navigator.redirectTo('ImageCropResultPage', {
				fileUri: nativeEvent.uri,
			});
		},
		[navigator],
	);

	return (
		<View style={styles.container}>
			<ImageCropView
				ref={cropViewRef}
				style={styles.cropView}
				fileUri={fileUri}
				cropStyle={cropStyle}
				onCrop={onCropped}
				objectRect={objectRect}
			/>
		</View>
	);
}

export default withNavigationItem({ titleItem: { title: 'CropPage' } })(ImageCropPage);

const styles = StyleSheet.create({
	container: {
		flex: 1,
		justifyContent: 'flex-start',
		alignItems: 'stretch',
	},
	cropView: {
		flex: 1,
	},
});

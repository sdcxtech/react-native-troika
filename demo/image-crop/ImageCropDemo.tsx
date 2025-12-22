import React, { useCallback } from 'react';
import { NavigationProps, withNavigationItem } from 'hybrid-navigation';
import { StyleSheet, TouchableOpacity, Text, View, Image } from 'react-native';
import { launchImageLibrary } from 'react-native-image-picker';
import { useToast } from 'react-native-toast-hybrid';
import { ObjectRect } from '@sdcx/image-crop';
import RNFS from 'react-native-fs';
const qs = require('qs');

interface ListItemProps {
	title: string;
	onPress?: () => void;
}

function ListItem({ title, onPress }: ListItemProps) {
	return (
		<TouchableOpacity style={styles.item} onPress={onPress}>
			<Text style={styles.text}>{title}</Text>
			<Image source={require('assets/indicator.png')} />
		</TouchableOpacity>
	);
}

function ImageCropDemo({ navigator }: NavigationProps) {
	const toast = useToast();

	const getAccessToken = useCallback(async () => {
		const client_id = '4G5y6AkjXjOBrYN1xl6hiCGt';
		const client_secret = 'vxhUyS18CQb2EZ0QGsDGHiw0qFm5U0g2';
		const url = `https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=${client_id}&client_secret=${client_secret}`;
		const result = await (await fetch(url, { method: 'GET' })).json();
		return result.access_token;
	}, []);

	const detectObject = useCallback(
		async (uri: string) => {
			toast.loading('图片检测中...');
			try {
				const accessToken = await getAccessToken();
				const imageBase64 = await RNFS.readFile(uri, 'base64');
				const url = `https://aip.baidubce.com/rest/2.0/image-classify/v1/object_detect?access_token=${accessToken}`;

				const result = await fetch(url, {
					method: 'POST',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded',
					},
					body: qs.stringify({ image: imageBase64, with_face: 1 }),
				});
				const objectRect: ObjectRect = (await result.json()).result;

				console.log('图像检测结果：' + JSON.stringify(objectRect));
				toast.hide();
				await navigator.push('ImageCropPage', { fileUri: uri, objectRect: objectRect });
			} catch (e: any) {
				toast.text('图片检测失败' + e.message);
			}
		},
		[getAccessToken, navigator, toast],
	);

	const handlePhotoDetectPress = useCallback(async () => {
		const result = await launchImageLibrary({
			selectionLimit: 1,
			mediaType: 'photo',
		});
		console.log('选择照片结果：', JSON.stringify(result));

		if (result.assets && result.assets.length > 0 && result.assets[0].uri) {
			await detectObject(result.assets[0].uri);
		}
	}, [detectObject]);

	const handlePhotoPress = useCallback(async () => {
		const result = await launchImageLibrary({
			selectionLimit: 1,
			mediaType: 'photo',
		});
		console.log('选择照片结果：', JSON.stringify(result));

		if (result.assets && result.assets.length > 0 && result.assets[0].uri) {
			await navigator.push('ImageCropPage', { fileUri: result.assets[0].uri });
		}
	}, [navigator]);

	const handleHeadPhotoPress = useCallback(async () => {
		const result = await launchImageLibrary({
			selectionLimit: 1,
			mediaType: 'photo',
		});
		console.log('选择照片结果：', JSON.stringify(result));
		if (result.assets && result.assets.length > 0 && result.assets[0].uri) {
			await navigator.push('ImageCropPage', {
				fileUri: result.assets[0].uri,
				cropStyle: 'circular',
			});
		}
	}, [navigator]);

	return (
		<View style={styles.container}>
			<ListItem title={'头像裁剪（圆形）'} onPress={handleHeadPhotoPress} />
			<ListItem title={'照片裁剪（矩形）'} onPress={handlePhotoPress} />
			<ListItem title={'照片裁剪（矩形 + 图像主体检测）'} onPress={handlePhotoDetectPress} />
		</View>
	);
}

export default withNavigationItem({ titleItem: { title: 'ImageCrop' } })(ImageCropDemo);

const styles = StyleSheet.create({
	container: {
		flex: 1,
		justifyContent: 'flex-start',
		alignItems: 'stretch',
	},
	item: {
		height: 60,
		justifyContent: 'space-between',
		alignItems: 'center',
		flexDirection: 'row',
		borderBottomWidth: 1,
		borderBottomColor: '#EEEEEE',
		paddingLeft: 16,
		paddingRight: 16,
	},
	text: {
		color: '#222222',
		fontSize: 17,
	},
});

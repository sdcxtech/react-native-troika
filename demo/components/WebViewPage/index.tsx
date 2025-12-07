import React, { useRef, useState } from 'react';
import { WebView } from 'react-native-webview';
import Lottie from 'lottie-react-native';
import { StyleSheet, View } from 'react-native';

export function WebViewPage({ url }: { url: string }) {
	const lottieRef = useRef<Lottie>(null);
	const [loading, setLoading] = useState(false);

	const onLoadStart = () => {
		setLoading(true);
	};

	const onLoadEnd = () => {
		setLoading(false);
	};

	return (
		<View collapsable={false} style={styles.fill}>
			<WebView
				style={{ flex: 1 }}
				containerStyle={{ flex: 1, overflow: 'visible' }}
				originWhitelist={['*']}
				source={{ uri: url }}
				onLoadProgress={({ nativeEvent: { progress } }) => {
					console.log('progress', progress);
				}}
				onLoadStart={onLoadStart}
				onLoadEnd={onLoadEnd}
				cacheEnabled={false}
			/>
			{loading ? (
				<Lottie
					ref={lottieRef}
					style={styles.loading}
					source={require('./loading.json')}
					autoPlay
					loop
					speed={1}
				/>
			) : null}
		</View>
	);
}

const styles = StyleSheet.create({
	fill: {
		flex: 1,
		justifyContent: 'center',
	},
	loading: {
		height: 80,
		width: 100,
		position: 'absolute',
		top: '20%',
		alignSelf: 'center',
	},
});

import React from 'react'
import { WebView } from 'react-native-webview'

export function DemoWebView({ url, onLoadFinish }: { url: string; onLoadFinish?: () => void }) {
  return (
    <WebView
      style={{ height: '100%', flex: 1 }}
      containerStyle={{ flex: 1, overflow: 'visible' }}
      originWhitelist={['*']}
      source={{ uri: url }}
      onLoadProgress={({ nativeEvent: { progress } }) => {
        console.log('progress', progress)
      }}
      onLoadEnd={onLoadFinish}
      cacheEnabled={false}
    />
  )
}

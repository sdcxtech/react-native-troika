# ActivityIndicator

`ActivityIndicator` 是一个 React Native 原生 UI 组件，是一个菊花转圈的动画。

<img src="https://raw.githubusercontent.com/sdcxtech/react-native-troika/master/packages/activity-indicator/docs/assets/activity.png" width="320">

## 版本兼容

| 版本 | RN 版本 | RN 架构 |
| ---- | ------- | ------- |
| 0.x  | < 0.82  | 旧架构  |
| 1.x  | >= 0.82 | 新架构  |

## Installation

```bash
yarn add @sdcx/activity-indicator
```

## Usage

```tsx
import ActivityIndicator from '@sdcx/activity-indicator';

function App() {
	return (
		<View style={styles.container}>
			<ActivityIndicator />
		</View>
	);
}
```

## Props

基本与 RN 的 [ActivityIndicator](https://reactnative.dev/docs/activityindicator) 一致。

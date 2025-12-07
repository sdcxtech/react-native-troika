import { Animated, LayoutChangeEvent } from 'react-native';
import { Driver, DriverState } from './Driver';

export class ViewDriver implements Driver {
	constructor(public name: string) {}

	// 输入框距屏幕底部的距离
	private senderBottom = 0;

	private y = 0;
	private animation = new Animated.Value(0);

	shown = false;
	height = 0;

	show = (state: DriverState) => {
		const { bottom, driver, setDriver, setTranslateY } = state;

		if (driver && driver !== this) {
			// 记录主界面当前位置
			this.y = driver.shown ? driver.height : 0;
			// 隐藏前一个 driver
			driver.hide({ bottom, driver: this, setDriver, setTranslateY });
		}

		this.shown = true;
		this.senderBottom = bottom;
		setDriver(this);
		setTranslateY(this.translateY);

		Animated.timing(this.animation, {
			toValue: 0,
			duration: 200,
			useNativeDriver: true,
		}).start();
	};

	hide = (state: DriverState) => {
		const { bottom, driver, setDriver, setTranslateY } = state;

		this.shown = false;
		this.y = 0;
		this.senderBottom = bottom;

		if (driver === this) {
			setDriver(undefined);
			setTranslateY(this.translateY);
			Animated.timing(this.animation, {
				toValue: this.height,
				duration: 200,
				useNativeDriver: true,
			}).start();
		} else {
			this.animation.setValue(this.height);
		}
	};

	toggle = (state: DriverState) => {
		this.shown ? this.hide(state) : this.show(state);
	};

	style = {
		transform: [
			{
				translateY: this.animation,
			},
		],
	};

	private get position() {
		return this.animation.interpolate({
			inputRange: [0, this.height],
			outputRange: [this.height, 0],
		});
	}

	private get translateY() {
		const extraHeight = this.senderBottom;
		console.log(this.name, 'height', this.height, 'y', this.y, 'extraHeight', extraHeight);
		if (!this.shown || this.y === 0) {
			return this.position.interpolate({
				inputRange: [extraHeight, this.height],
				outputRange: [0, extraHeight - this.height],
				extrapolate: 'clamp',
			}) as Animated.Value;
		} else {
			return this.position.interpolate({
				inputRange: [0, this.height],
				outputRange: [extraHeight - this.y, extraHeight - this.height],
				extrapolate: 'clamp',
			}) as Animated.Value;
		}
	}

	onLayout = (event: LayoutChangeEvent) => {
		this.animation.setValue(event.nativeEvent.layout.height);
		this.height = event.nativeEvent.layout.height;
	};
}

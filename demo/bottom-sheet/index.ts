import Navigation from 'hybrid-navigation';

import Home from './Home';
import BottomSheetWithoutScrollView from './BottomSheetWithoutScrollView';
import BottomSheetFlashList from './BottomSheetFlashList';
import BottomSheetPagerView from './BottomSheetPagerView';
import BottomSheetBackdropShadow from './BottomSheetBackdropShadow';

export function registerBottomSheetComponent() {
	Navigation.registerComponent('BottomSheet', () => Home);
	Navigation.registerComponent(
		'BottomSheetWithoutScrollView',
		() => BottomSheetWithoutScrollView,
	);
	Navigation.registerComponent('BottomSheetFlashList', () => BottomSheetFlashList);
	Navigation.registerComponent('BottomSheetPagerView', () => BottomSheetPagerView);
	Navigation.registerComponent('BottomSheetBackdropShadow', () => BottomSheetBackdropShadow);
}

import Navigation from 'hybrid-navigation';

import Home from './Home';
import NestedScrollFlatList from './NestedScrollFlatList';
import NestedScrollParallaxHeader from './NestedScrollParallaxHeader';
import NestedScrollTabView from './NestedScrollTabView';
import NestedScrollPagerViewStickyHeader from './NestedScrollPagerViewStickyHeader';

export function registerNestedScrollComponent() {
	Navigation.registerComponent('NestedScroll', () => Home);
	Navigation.registerComponent('NestedScrollFlatList', () => NestedScrollFlatList);
	Navigation.registerComponent('NestedScrollParallaxHeader', () => NestedScrollParallaxHeader);
	Navigation.registerComponent('NestedScrollTabView', () => NestedScrollTabView);
	Navigation.registerComponent(
		'NestedScrollPagerViewStickyHeader',
		() => NestedScrollPagerViewStickyHeader,
	);
}

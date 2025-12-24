export interface Anchor {
	x: number;
	y: number;
	size: number;
}

export interface BallProps {
	anchor: Anchor;
	onOffsetChanged?: (x: number, y: number) => void;
}

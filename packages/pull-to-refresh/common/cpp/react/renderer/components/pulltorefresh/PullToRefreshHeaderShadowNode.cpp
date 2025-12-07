#include "PullToRefreshHeaderShadowNode.h"

#include <react/renderer/components/view/conversions.h>

namespace facebook {
namespace react {

using namespace yoga;

extern const char PullToRefreshHeaderComponentName[] = "PullToRefreshHeader";

void PullToRefreshHeaderShadowNode::adjustLayoutWithState() {
	ensureUnsealed();
	
	auto height = getLayoutMetrics().frame.size.height;
	if (height == 0) {
		height = 1000;
	}
	
	yoga::Style adjustedStyle = getConcreteProps().yogaStyle;
	adjustedStyle.setPositionType(yoga::PositionType::Absolute);
	adjustedStyle.setPosition(Edge::Top, Style::Length::points(-height));
	adjustedStyle.setPosition(Edge::Bottom, Style::Length::undefined());
	adjustedStyle.setPosition(Edge::Left, Style::Length::points(0));
	adjustedStyle.setPosition(Edge::Right, Style::Length::points(0));
	
	auto currentStyle = yogaNode_.style();
	
	if (adjustedStyle.positionType() != currentStyle.positionType() ||
		adjustedStyle.position(Edge::Top) != currentStyle.position(Edge::Top) ||
		adjustedStyle.position(Edge::Bottom) != currentStyle.position(Edge::Bottom) ||
		adjustedStyle.position(Edge::Left) != currentStyle.position(Edge::Left) ||
		adjustedStyle.position(Edge::Right) != currentStyle.position(Edge::Right)) {
		yogaNode_.setStyle(adjustedStyle);
		yogaNode_.setDirty(true);
	}
}


} // namespace react
} // namespace facebook

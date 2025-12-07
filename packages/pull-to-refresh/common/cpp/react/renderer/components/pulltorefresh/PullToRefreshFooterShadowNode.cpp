#include "PullToRefreshFooterShadowNode.h"

#include <react/renderer/components/view/conversions.h>

namespace facebook {
namespace react {

using namespace yoga;

extern const char PullToRefreshFooterComponentName[] = "PullToRefreshFooter";


void PullToRefreshFooterShadowNode::adjustLayoutWithState() {
	ensureUnsealed();
	
	auto state =
		  std::static_pointer_cast<const PullToRefreshFooterShadowNode::ConcreteState>(
			  getState());
	auto stateData = state->getData();
	auto height = stateData.contentHeight;
	
	yoga::Style adjustedStyle = getConcreteProps().yogaStyle;
	adjustedStyle.setPositionType(yoga::PositionType::Absolute);
	adjustedStyle.setPosition(Edge::Top, Style::Length::points(height));
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

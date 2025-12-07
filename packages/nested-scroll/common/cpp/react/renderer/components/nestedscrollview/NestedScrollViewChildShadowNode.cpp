#include "NestedScrollViewChildShadowNode.h"

#include <react/renderer/components/view/conversions.h>

namespace facebook {
namespace react {

using namespace yoga;

extern const char NestedScrollViewChildComponentName[] = "NestedScrollViewChild";

void NestedScrollViewChildShadowNode::adjustLayoutWithState(float contentHeight) {
	// ensureUnsealed();

	yoga::Style adjustedStyle = getConcreteProps().yogaStyle;
	adjustedStyle.setMaxDimension(
		yoga::Dimension::Height, yoga::StyleSizeLength::points(contentHeight));
	adjustedStyle.setMinDimension(
		yoga::Dimension::Height, yoga::StyleSizeLength::points(contentHeight));

	auto currentStyle = yogaNode_.style();
	if (adjustedStyle.maxDimension(yoga::Dimension::Height) != currentStyle.maxDimension(yoga::Dimension::Height) ||
		adjustedStyle.minDimension(yoga::Dimension::Height) != currentStyle.minDimension(yoga::Dimension::Height)) {
		yogaNode_.setStyle(adjustedStyle);
		yogaNode_.setDirty(true);
	}
}

} // namespace react
} // namespace facebook

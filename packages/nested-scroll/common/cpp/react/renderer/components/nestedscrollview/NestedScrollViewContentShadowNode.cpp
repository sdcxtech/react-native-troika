#include "NestedScrollViewContentShadowNode.h"

#include <react/renderer/components/view/conversions.h>

namespace facebook {
namespace react {

using namespace yoga;

extern const char NestedScrollViewContentComponentName[] = "NestedScrollViewContent";

bool NestedScrollViewContentShadowNode::adjustLayoutWithState(float contentHeight) {
	//ensureUnsealed();

	yoga::Style adjustedStyle = getConcreteProps().yogaStyle;
	adjustedStyle.setMaxDimension(
		yoga::Dimension::Height, yoga::StyleSizeLength::points(contentHeight));
	adjustedStyle.setMinDimension(
		yoga::Dimension::Height, yoga::StyleSizeLength::points(contentHeight));

	auto currentStyle = yogaNode_.style();
	if (currentStyle.maxDimension(yoga::Dimension::Height) != currentStyle.maxDimension(yoga::Dimension::Height) ||
		adjustedStyle.minDimension(yoga::Dimension::Height) != currentStyle.minDimension(yoga::Dimension::Height)) {
		yogaNode_.setStyle(adjustedStyle);
		yogaNode_.setDirty(true);
		return true;
	}
	return false;
}

} // namespace react
} // namespace facebook

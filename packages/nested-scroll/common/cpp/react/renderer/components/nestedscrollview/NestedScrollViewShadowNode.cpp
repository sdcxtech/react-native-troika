#include "NestedScrollViewShadowNode.h"

#include <react/renderer/components/view/conversions.h>

#include <react/renderer/components/nestedscrollview/NestedScrollViewContentShadowNode.h>
#include <react/renderer/components/nestedscrollview/NestedScrollViewChildShadowNode.h>

namespace facebook {
namespace react {

using namespace yoga;

extern const char NestedScrollViewComponentName[] = "NestedScrollView";

void NestedScrollViewShadowNode::adjustLayoutWithState() {
	ensureUnsealed();

	auto state =
      std::static_pointer_cast<const NestedScrollViewShadowNode::ConcreteState>(
          getState());

	// Check if state is valid
	if (!state) {
		return;
	}

  	auto stateData = state->getData();
	auto contentHeight = stateData.contentHeight;
	auto headerHeight = stateData.headerHeight;
	auto parentNodeHeight = headerHeight + contentHeight;

	// Validate height values
	if (parentNodeHeight <= 0) {
		return;
	}

	auto nodes = getLayoutableChildNodes();

#ifdef ANDROID
	if (nodes.empty()) {
		return;
	}
	
	auto contentShadowNode = static_cast<NestedScrollViewContentShadowNode *>(nodes.at(0));
	auto adjusted = contentShadowNode->adjustLayoutWithState(parentNodeHeight);
	if (adjusted) {
		yogaNode_.setDirty(true);
	}

	auto childNodes = contentShadowNode->getLayoutableChildNodes();
	if (childNodes.size() != 2) {
		return;
	}

	auto childShadowNode = static_cast<NestedScrollViewChildShadowNode *>(childNodes.at(1));
	childShadowNode->adjustLayoutWithState(contentHeight);
	
#else
	if (nodes.size() != 2) {
		return;
	}

	auto childShadowNode = static_cast<NestedScrollViewChildShadowNode *>(nodes.at(1));
	childShadowNode->adjustLayoutWithState(contentHeight);
#endif
	
}

Point NestedScrollViewShadowNode::getContentOriginOffset(bool includeTransform) const {
    auto state =
    std::static_pointer_cast<const NestedScrollViewShadowNode::ConcreteState>(
                                                                              getState());
    auto stateData = state->getData();
    auto contentOffsetY = stateData.contentOffsetY;

    return {
        .x  = 0,
        .y = static_cast<Float>(-contentOffsetY)
    };
}

} // namespace react
} // namespace facebook

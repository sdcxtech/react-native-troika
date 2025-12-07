#pragma once

#include <react/renderer/components/nestedscrollview/NestedScrollViewShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>

namespace facebook {
namespace react {

/*
 * Descriptor for <NestedScrollView> component.
 */
class NestedScrollViewComponentDescriptor final
    : public ConcreteComponentDescriptor<NestedScrollViewShadowNode> {
  	using ConcreteComponentDescriptor::ConcreteComponentDescriptor;
  	void adopt(ShadowNode &shadowNode) const override {
    auto &concreteShadowNode =
        static_cast<NestedScrollViewShadowNode &>(shadowNode);

		concreteShadowNode.adjustLayoutWithState();

    	ConcreteComponentDescriptor::adopt(shadowNode);
  	}
};

} // namespace react
} // namespace facebook

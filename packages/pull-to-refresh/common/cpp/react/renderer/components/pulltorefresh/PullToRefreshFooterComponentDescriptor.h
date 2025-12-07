#pragma once

#include <react/renderer/components/pulltorefresh/PullToRefreshFooterShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>

namespace facebook {
namespace react {

/*
 * Descriptor for <PullToRefreshFooter> component.
 */
class PullToRefreshFooterComponentDescriptor final
    : public ConcreteComponentDescriptor<PullToRefreshFooterShadowNode> {
  	using ConcreteComponentDescriptor::ConcreteComponentDescriptor;
  	void adopt(ShadowNode &shadowNode) const override {
		auto &concreteShadowNode = static_cast<PullToRefreshFooterShadowNode &>(shadowNode);
		concreteShadowNode.adjustLayoutWithState();

		ConcreteComponentDescriptor::adopt(shadowNode);
	}
};

} // namespace react
} // namespace facebook

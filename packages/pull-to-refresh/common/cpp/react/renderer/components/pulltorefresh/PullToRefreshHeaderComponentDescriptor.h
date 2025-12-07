#pragma once

#include <react/renderer/components/pulltorefresh/PullToRefreshHeaderShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>

namespace facebook {
namespace react {

/*
 * Descriptor for <PullToRefreshHeader> component.
 */
class PullToRefreshHeaderComponentDescriptor final
    : public ConcreteComponentDescriptor<PullToRefreshHeaderShadowNode> {
  	using ConcreteComponentDescriptor::ConcreteComponentDescriptor;
		
  	void adopt(ShadowNode &shadowNode) const override {
		auto &concreteShadowNode = static_cast<PullToRefreshHeaderShadowNode &>(shadowNode);
		concreteShadowNode.adjustLayoutWithState();
		
		ConcreteComponentDescriptor::adopt(shadowNode);
	}
};

} // namespace react
} // namespace facebook

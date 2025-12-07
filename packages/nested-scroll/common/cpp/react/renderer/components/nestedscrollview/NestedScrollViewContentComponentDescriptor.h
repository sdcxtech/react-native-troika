#pragma once

#include <react/renderer/components/nestedscrollview/NestedScrollViewContentShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>

namespace facebook {
namespace react {

/*
 * Descriptor for <NestedScrollViewChild> component.
 */
class NestedScrollViewContentComponentDescriptor final
    : public ConcreteComponentDescriptor<NestedScrollViewContentShadowNode> {
  	using ConcreteComponentDescriptor::ConcreteComponentDescriptor;
};

} // namespace react
} // namespace facebook

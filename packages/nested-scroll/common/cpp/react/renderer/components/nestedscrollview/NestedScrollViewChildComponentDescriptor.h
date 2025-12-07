#pragma once

#include <react/renderer/components/nestedscrollview/NestedScrollViewChildShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>

namespace facebook {
namespace react {

/*
 * Descriptor for <NestedScrollViewChild> component.
 */
class NestedScrollViewChildComponentDescriptor final
    : public ConcreteComponentDescriptor<NestedScrollViewChildShadowNode> {
  	using ConcreteComponentDescriptor::ConcreteComponentDescriptor;
};

} // namespace react
} // namespace facebook

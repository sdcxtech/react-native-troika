#pragma once

#include <jsi/jsi.h>
#include <react/renderer/components/nestedscrollview/EventEmitters.h>
#include <react/renderer/components/nestedscrollview/Props.h>
#include <react/renderer/components/view/ConcreteViewShadowNode.h>
#include <react/renderer/core/StateData.h>

namespace facebook {
namespace react {

JSI_EXPORT extern const char NestedScrollViewContentComponentName[];

using NestedScrollViewContentState = StateData;

/*
 * `ShadowNode` for <NestedScrollViewContent> component.
 */
class JSI_EXPORT NestedScrollViewContentShadowNode final
    : public ConcreteViewShadowNode<
          NestedScrollViewContentComponentName,
          NestedScrollViewContentProps,
          ViewEventEmitter,
          NestedScrollViewContentState> {
  	using ConcreteViewShadowNode::ConcreteViewShadowNode;

public:
  	bool adjustLayoutWithState(float contentHeight);
};

} // namespace react
} // namespace facebook

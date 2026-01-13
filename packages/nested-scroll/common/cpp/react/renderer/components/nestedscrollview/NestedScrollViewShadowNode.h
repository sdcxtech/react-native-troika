#pragma once

#include <jsi/jsi.h>
#include <react/renderer/components/nestedscrollview/EventEmitters.h>
#include <react/renderer/components/nestedscrollview/Props.h>
#include <react/renderer/components/nestedscrollview/NestedScrollViewState.h>
#include <react/renderer/components/view/ConcreteViewShadowNode.h>

namespace facebook {
namespace react {

JSI_EXPORT extern const char NestedScrollViewComponentName[];

/*
 * `ShadowNode` for <NestedScrollView> component.
 */
class JSI_EXPORT NestedScrollViewShadowNode final
    : public ConcreteViewShadowNode<
          NestedScrollViewComponentName,
          NestedScrollViewProps,
          ViewEventEmitter,
          NestedScrollViewState> {
  	using ConcreteViewShadowNode::ConcreteViewShadowNode;

public:
	void adjustLayoutWithState();
	Point getContentOriginOffset(bool includeTransform) const;
};

} // namespace react
} // namespace facebook

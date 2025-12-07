#pragma once

#include <jsi/jsi.h>
#include <react/renderer/components/pulltorefresh/EventEmitters.h>
#include <react/renderer/components/pulltorefresh/Props.h>
#include <react/renderer/components/pulltorefresh/PullToRefreshFooterState.h>
#include <react/renderer/components/view/ConcreteViewShadowNode.h>

namespace facebook {
namespace react {

JSI_EXPORT extern const char PullToRefreshFooterComponentName[];

/*
 * `ShadowNode` for <PullToRefreshFooter> component.
 */
class JSI_EXPORT PullToRefreshFooterShadowNode final
    : public ConcreteViewShadowNode<
          PullToRefreshFooterComponentName,
          PullToRefreshFooterProps,
          ViewEventEmitter,
		  PullToRefreshFooterState> {
  	using ConcreteViewShadowNode::ConcreteViewShadowNode;

	public:
		void adjustLayoutWithState();

};

} // namespace react
} // namespace facebook

#pragma once

#include <jsi/jsi.h>
#include <react/renderer/components/pulltorefresh/EventEmitters.h>
#include <react/renderer/components/pulltorefresh/Props.h>
#include <react/renderer/components/pulltorefresh/States.h>
#include <react/renderer/components/view/ConcreteViewShadowNode.h>

namespace facebook {
namespace react {

JSI_EXPORT extern const char PullToRefreshHeaderComponentName[];


using PullToRefreshHeaderState = StateData;


/*
 * `ShadowNode` for <PullToRefreshHeader> component.
 */
class JSI_EXPORT PullToRefreshHeaderShadowNode final
    : public ConcreteViewShadowNode<
          PullToRefreshHeaderComponentName,
          PullToRefreshHeaderProps,
          ViewEventEmitter,
          PullToRefreshHeaderState> {
  	using ConcreteViewShadowNode::ConcreteViewShadowNode;
			  
	public:
	  void adjustLayoutWithState();

};

} // namespace react
} // namespace facebook

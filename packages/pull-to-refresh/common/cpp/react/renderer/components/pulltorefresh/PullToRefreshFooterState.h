#pragma once

#include <react/renderer/components/pulltorefresh/Props.h>

#ifdef ANDROID
#include <folly/dynamic.h>
#include <react/renderer/mapbuffer/MapBuffer.h>
#include <react/renderer/mapbuffer/MapBufferBuilder.h>
#endif

namespace facebook {
namespace react {

/*
 * State for <PullToRefreshFooter> component.
 */
class JSI_EXPORT PullToRefreshFooterState final {
public:
  	using Shared = std::shared_ptr<const PullToRefreshFooterState>;

  	PullToRefreshFooterState(){};

#ifdef ANDROID
  	PullToRefreshFooterState(
      	PullToRefreshFooterState const &previousState,
      	folly::dynamic data):
			contentHeight(data["contentHeight"].getDouble()){};
#endif

  	double contentHeight{};

#ifdef ANDROID
  	folly::dynamic getDynamic() const;
  	MapBuffer getMapBuffer() const {
    	return MapBufferBuilder::EMPTY();
  	}

#endif
};

} // namespace react
} // namespace facebook

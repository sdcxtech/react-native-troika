#pragma once

#include <react/renderer/components/nestedscrollview/Props.h>

#ifdef ANDROID
#include <folly/dynamic.h>
#include <react/renderer/mapbuffer/MapBuffer.h>
#include <react/renderer/mapbuffer/MapBufferBuilder.h>
#endif

namespace facebook {
namespace react {

/*
 * State for <NestedScrollView> component.
 */
class JSI_EXPORT NestedScrollViewState final {
public:
  	using Shared = std::shared_ptr<const NestedScrollViewState>;

  	NestedScrollViewState(){};

#ifdef ANDROID
  	NestedScrollViewState(
      	NestedScrollViewState const &previousState,
      	folly::dynamic data):
			contentHeight(data["contentHeight"].getDouble()),
        	headerHeight(data["headerHeight"].getDouble()){};
#endif

  	double contentHeight{};
  	double headerHeight{};

#ifdef ANDROID
  	folly::dynamic getDynamic() const;
  	MapBuffer getMapBuffer() const {
    	return MapBufferBuilder::EMPTY();
  	}

#endif
};

} // namespace react
} // namespace facebook

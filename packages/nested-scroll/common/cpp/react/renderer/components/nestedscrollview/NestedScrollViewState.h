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
      	folly::dynamic data) {
        // 使用安全的方式读取，如果 data 中没有某个字段，则保留上一次状态的值
        contentHeight = data.getDefault("contentHeight", previousState.contentHeight).asDouble();
        headerHeight = data.getDefault("headerHeight", previousState.headerHeight).asDouble();
        contentOffsetY = data.getDefault("contentOffsetY", previousState.contentOffsetY).asDouble();
    };
#endif

  	double contentHeight{};
  	double headerHeight{};
    double contentOffsetY{};
#ifdef ANDROID
  	folly::dynamic getDynamic() const;
  	MapBuffer getMapBuffer() const {
    	return MapBufferBuilder::EMPTY();
  	}

#endif
};

} // namespace react
} // namespace facebook

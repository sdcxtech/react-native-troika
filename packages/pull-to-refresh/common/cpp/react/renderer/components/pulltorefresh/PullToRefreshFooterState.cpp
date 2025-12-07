#include "PullToRefreshFooterState.h"

namespace facebook {
namespace react {

#ifdef ANDROID

folly::dynamic PullToRefreshFooterState::getDynamic() const {
  	folly::dynamic data = folly::dynamic::object();
  	data["contentHeight"] = contentHeight;
  	return data;
}
#endif

} // namespace react
} // namespace facebook

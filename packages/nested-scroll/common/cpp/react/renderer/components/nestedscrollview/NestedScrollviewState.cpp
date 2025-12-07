#include "NestedScrollViewState.h"

namespace facebook {
namespace react {

#ifdef ANDROID

folly::dynamic NestedScrollViewState::getDynamic() const {
  	folly::dynamic data = folly::dynamic::object();
  	data["contentHeight"] = contentHeight;
  	data["headerHeight"] = headerHeight;

  	return data;
}
#endif

} // namespace react
} // namespace facebook

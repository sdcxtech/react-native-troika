#include "ActivityIndicatorShadowNode.h"
#include "ActivityIndicatorMeasurementsManager.h"

namespace facebook {
    namespace react {

        extern const char ActivityIndicatorComponentName[] = "ActivityIndicator";

#ifdef ANDROID
        void ActivityIndicatorShadowNode::setActivityIndicatorMeasurementsManager(
                const std::shared_ptr<ActivityIndicatorMeasurementsManager> &
                measurementsManager) {
            ensureUnsealed();
            measurementsManager_ = measurementsManager;
        }

#pragma mark - LayoutableShadowNode

        Size ActivityIndicatorShadowNode::measureContent(
                const LayoutContext & /*layoutContext*/,
                const LayoutConstraints &layoutConstraints) const {
            return measurementsManager_->measure(getSurfaceId(), layoutConstraints, getConcreteProps());
        }
#endif

    }
}

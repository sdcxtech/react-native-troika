#pragma once

#include <jsi/jsi.h>
#include <react/renderer/components/activityindicator/ActivityIndicatorState.h>
#include <react/renderer/components/activityindicator/Props.h>
#include <react/renderer/components/activityindicator/EventEmitters.h>
#include <react/renderer/components/view/ConcreteViewShadowNode.h>

#include "ActivityIndicatorMeasurementsManager.h"

namespace facebook {
    namespace react {

        JSI_EXPORT extern const char ActivityIndicatorComponentName[];

/*
 * `ShadowNode` for <ActivityIndicator> component.
 */
        class JSI_EXPORT ActivityIndicatorShadowNode final
                : public ConcreteViewShadowNode<
                        ActivityIndicatorComponentName,
                        ActivityIndicatorProps,
                        ActivityIndicatorEventEmitter,
                        ActivityIndicatorState> {
        public:
            using ConcreteViewShadowNode::ConcreteViewShadowNode;

#ifdef ANDROID
            void setActivityIndicatorMeasurementsManager(
                    const std::shared_ptr<ActivityIndicatorMeasurementsManager> &measurementsManager);

#pragma mark - LayoutableShadowNode

            Size measureContent(
                    const LayoutContext &layoutContext,
                    const LayoutConstraints &layoutConstraints) const override;

        private:
            std::shared_ptr<ActivityIndicatorMeasurementsManager> measurementsManager_;
#endif

        };

    }
}

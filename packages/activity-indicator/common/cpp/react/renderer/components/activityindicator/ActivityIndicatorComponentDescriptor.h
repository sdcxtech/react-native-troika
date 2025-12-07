#pragma once

#include <react/renderer/components/activityindicator/ActivityIndicatorShadowNode.h>
#include <react/renderer/core/ConcreteComponentDescriptor.h>
#include "ActivityIndicatorMeasurementsManager.h"

namespace facebook {
    namespace react {

        class ActivityIndicatorComponentDescriptor final
                : public ConcreteComponentDescriptor<ActivityIndicatorShadowNode> {
#ifdef ANDROID
        public:
            ActivityIndicatorComponentDescriptor(const ComponentDescriptorParameters &parameters)
                    : ConcreteComponentDescriptor(parameters), measurementsManager_(
                    std::make_shared<ActivityIndicatorMeasurementsManager>(contextContainer_)) {}

            void adopt(ShadowNode &shadowNode) const override {
                ConcreteComponentDescriptor::adopt(shadowNode);


                auto &activityIndicatorShadowNode =
                        static_cast<ActivityIndicatorShadowNode &>(shadowNode);

                // `ActivityIndicatorShadowNode` uses `ActivityIndicatorMeasurementsManager` to
                // provide measurements to Yoga.
                activityIndicatorShadowNode.setActivityIndicatorMeasurementsManager(
                        measurementsManager_);

                // All `ActivityIndicatorShadowNode`s must have leaf Yoga nodes with properly
                // setup measure function.
                activityIndicatorShadowNode.enableMeasurement();
            }
        private:
            const std::shared_ptr<ActivityIndicatorMeasurementsManager> measurementsManager_;
#else
        public:
            ActivityIndicatorComponentDescriptor(const ComponentDescriptorParameters &parameters)
            : ConcreteComponentDescriptor(parameters) {}
#endif
        };

    }
}

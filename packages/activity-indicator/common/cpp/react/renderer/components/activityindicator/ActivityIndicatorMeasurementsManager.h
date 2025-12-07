#ifdef ANDROID
#pragma once

#include <react/renderer/core/ConcreteComponentDescriptor.h>
#include <react/renderer/core/LayoutConstraints.h>
#include <react/utils/ContextContainer.h>
#include <react/renderer/components/activityindicator/Props.h>
#include <react/renderer/core/propsConversions.h>

namespace facebook::react {

#ifdef RN_SERIALIZABLE_STATE
inline folly::dynamic toDynamic(const ActivityIndicatorProps &props) {
	folly::dynamic serializedProps = folly::dynamic::object();
	serializedProps["size"] = toDynamic(props.size);
	return serializedProps;
}
#endif

    class ActivityIndicatorMeasurementsManager {
    public:
        ActivityIndicatorMeasurementsManager(
                const std::shared_ptr<const ContextContainer> &contextContainer)
                : contextContainer_(contextContainer) {}

        Size measure(SurfaceId surfaceId, LayoutConstraints layoutConstraints, const ActivityIndicatorProps& props) const;

    private:
        const std::shared_ptr<const ContextContainer> contextContainer_;
    };
}
#endif

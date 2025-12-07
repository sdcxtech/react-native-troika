#ifdef ANDROID
#include "ActivityIndicatorMeasurementsManager.h"

#include <fbjni/fbjni.h>
#include <folly/dynamic.h>
#include <react/jni/ReadableNativeMap.h>
#include <react/renderer/core/conversions.h>

using namespace facebook::jni;

namespace facebook::react {

Size ActivityIndicatorMeasurementsManager::measure(
    SurfaceId surfaceId,
    LayoutConstraints layoutConstraints,
    const ActivityIndicatorProps& props) const {

  const jni::global_ref<jobject>& fabricUIManager =
      contextContainer_->at<jni::global_ref<jobject>>("FabricUIManager");

  static auto measure =
      jni::findClassStatic("com/facebook/react/fabric/FabricUIManager")
          ->getMethod<jlong(
              jint,
              jstring,
              ReadableMap::javaobject,
              ReadableMap::javaobject,
              ReadableMap::javaobject,
              jfloat,
              jfloat,
              jfloat,
              jfloat)>("measure");

  auto minimumSize = layoutConstraints.minimumSize;
  auto maximumSize = layoutConstraints.maximumSize;

  local_ref<JString> componentName = make_jstring("ActivityIndicator");

  // Convert props to ReadableMap
	auto serializedProps = toDynamic(props);
	local_ref<ReadableNativeMap::javaobject> propsRNM =
    ReadableNativeMap::newObjectCxxArgs(serializedProps);
    local_ref<ReadableMap::javaobject> propsRM =
          make_local(reinterpret_cast<ReadableMap::javaobject>(propsRNM.get()));

  auto measurement = yogaMeassureToSize(measure(
      fabricUIManager,
      surfaceId,
      componentName.get(),
	  nullptr,
      propsRM.get(),
      nullptr,
      minimumSize.width,
      maximumSize.width,
      minimumSize.height,
      maximumSize.height));

  return measurement;
}

}
#endif

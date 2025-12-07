#pragma once

#include <ReactCommon/JavaTurboModule.h>
#include <ReactCommon/TurboModule.h>
#include <jsi/jsi.h>
#include <react/renderer/components/nestedscrollview/NestedScrollViewComponentDescriptor.h>
#include <react/renderer/components/nestedscrollview/NestedScrollViewContentComponentDescriptor.h>
#include <react/renderer/components/nestedscrollview/NestedScrollViewChildComponentDescriptor.h>

namespace facebook {
namespace react {

JSI_EXPORT
std::shared_ptr<TurboModule> nestedscrollview_ModuleProvider(
    const std::string &moduleName,
    const JavaTurboModule::InitParams &params);

} // namespace react
} // namespace facebook

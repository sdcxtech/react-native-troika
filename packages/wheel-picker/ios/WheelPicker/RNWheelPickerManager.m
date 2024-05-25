/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

#import "RNWheelPickerManager.h"
#import "RNWheelPicker.h"

#import <React/RCTBridge.h>
#import <React/RCTFont.h>

@implementation RNWheelPickerManager

RCT_EXPORT_MODULE(WheelPicker)

- (UIView *)view {
  return [RNWheelPicker new];
}

RCT_EXPORT_VIEW_PROPERTY(items, NSArray<NSString *>)
RCT_EXPORT_VIEW_PROPERTY(selectedIndex, NSInteger)
RCT_EXPORT_VIEW_PROPERTY(onItemSelected, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(color, UIColor)
RCT_EXPORT_VIEW_PROPERTY(textAlign, NSTextAlignment)
RCT_CUSTOM_VIEW_PROPERTY(fontSize, NSNumber, RNWheelPicker) {
    view.font = [RCTFont updateFont:view.font withSize:json ?: @(defaultView.font.pointSize)];
}
RCT_CUSTOM_VIEW_PROPERTY(fontWeight, NSString, __unused RNWheelPicker) {
    view.font = [RCTFont updateFont:view.font withWeight:json]; // defaults to normal
}
RCT_CUSTOM_VIEW_PROPERTY(fontStyle, NSString, __unused RNWheelPicker) {
    view.font = [RCTFont updateFont:view.font withStyle:json]; // defaults to normal
}
RCT_CUSTOM_VIEW_PROPERTY(fontFamily, NSString, RNWheelPicker) {
    view.font = [RCTFont updateFont:view.font withFamily:json ?: defaultView.font.familyName];
}

@end

#import "RNKeyboardInsetsViewManager.h"
#import "RNKeyboardInsetsView.h"

@implementation RNKeyboardInsetsViewManager

RCT_EXPORT_MODULE(KeyboardInsetsView)

- (UIView *)view {
    return [[RNKeyboardInsetsView alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(mode, NSString)
RCT_EXPORT_VIEW_PROPERTY(extraHeight, CGFloat)

RCT_EXPORT_VIEW_PROPERTY(onStatusChanged, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onPositionChanged, RCTDirectEventBlock)

@end

#import "RNBottomSheetManager.h"
#import "RNBottomSheet.h"
#import "RNBottomSheetState.h"

@implementation RNBottomSheetManager

RCT_EXPORT_MODULE(BottomSheet)

- (UIView *)view {
    return [RNBottomSheet new];
}

RCT_EXPORT_VIEW_PROPERTY(onSlide, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onStateChanged, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(peekHeight, CGFloat)

RCT_CUSTOM_VIEW_PROPERTY(state, NSString, RNBottomSheet) {
    view.state = RNBottomSheetStateFromString([RCTConvert NSString:json]);
}


@end

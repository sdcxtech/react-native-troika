#import "RNBottomSheetManager.h"
#import "RNBottomSheet.h"
#import "RNBottomSheetState.h"

@implementation RNBottomSheetManager

RCT_EXPORT_MODULE(BottomSheet)

- (UIView *)view {
    RNBottomSheet *bottomSheet = [[RNBottomSheet alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
    bottomSheet.pointerEvents = RCTPointerEventsBoxNone;
    return bottomSheet;
}

RCT_EXPORT_VIEW_PROPERTY(onSlide, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onStateChanged, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(peekHeight, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(draggable, BOOL)

RCT_CUSTOM_VIEW_PROPERTY(state, NSString, RNBottomSheet) {
    view.state = RNBottomSheetStateFromString([RCTConvert NSString:json]);
}

@end

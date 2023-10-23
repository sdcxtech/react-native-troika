#import <React/RCTUtils.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, RNBottomSheetState) {
    RNBottomSheetStateCollapsed = 0,
    RNBottomSheetStateExpanded,
    RNBottomSheetStateHidden,
    RNBottomSheetStateDragging,
    RNBottomSheetStateSettling,
};

RCT_EXTERN RNBottomSheetState RNBottomSheetStateFromString(NSString *state);

RCT_EXTERN NSString* RNBottomSheetStateToString(RNBottomSheetState state);
    
NS_ASSUME_NONNULL_END

#import "RNBottomSheetManager.h"
#import "RNBottomSheet.h"

@implementation RNBottomSheetManager

RCT_EXPORT_MODULE(BottomSheet)

- (UIView *)view {
    return [RNBottomSheet new];
}

@end

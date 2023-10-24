#import "RNNestedScrollViewHeaderManager.h"
#import "RNNestedScrollViewHeader.h"

#import <React/RCTConvert.h>

@implementation RNNestedScrollViewHeaderManager

RCT_EXPORT_MODULE(NestedScrollViewHeader)

- (UIView *)view {
    return [[RNNestedScrollViewHeader alloc] init];
}

RCT_CUSTOM_VIEW_PROPERTY(stickyHeight, CGFloat, RNNestedScrollViewHeader) {
    if (json) {
        [view setStickyHeight:[RCTConvert CGFloat:json]];
    } else {
        [view setStickyHeight:-1];
    }
}

RCT_EXPORT_VIEW_PROPERTY(stickyHeaderBeginIndex, NSUInteger)
RCT_EXPORT_VIEW_PROPERTY(onScroll, RCTDirectEventBlock)

@end

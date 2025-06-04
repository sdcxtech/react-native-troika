#import "RNNestedScrollViewHeaderManager.h"
#import "RNNestedScrollViewHeader.h"

#import <React/RCTConvert.h>

@implementation RNNestedScrollViewHeaderManager

RCT_EXPORT_MODULE(NestedScrollViewHeader)

- (UIView *)view {
    return [[RNNestedScrollViewHeader alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
}

RCT_CUSTOM_VIEW_PROPERTY(stickyHeight, CGFloat, RNNestedScrollViewHeader) {
    if (json) {
        [view setStickyHeight:[RCTConvert CGFloat:json]];
    } else {
        [view setStickyHeight:-1];
    }
}

RCT_CUSTOM_VIEW_PROPERTY(stickyHeaderBeginIndex, NSUInteger, RNNestedScrollViewHeader) {
    if (json) {
        [view setStickyHeaderBeginIndex:[RCTConvert NSUInteger:json]];
    } else {
        [view setStickyHeaderBeginIndex:NSUIntegerMax];
    }
}


RCT_EXPORT_VIEW_PROPERTY(onScroll, RCTDirectEventBlock)

@end

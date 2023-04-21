#import "RNNestedScrollViewHeaderManager.h"
#import "RNNestedScrollViewHeader.h"

@implementation RNNestedScrollViewHeaderManager

RCT_EXPORT_MODULE(NestedScrollViewHeader)

- (UIView *)view {
    return [[RNNestedScrollViewHeader alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(stickyHeight, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(stickyHeaderBeginIndex, NSUInteger)
RCT_EXPORT_VIEW_PROPERTY(onScroll, RCTDirectEventBlock)


@end

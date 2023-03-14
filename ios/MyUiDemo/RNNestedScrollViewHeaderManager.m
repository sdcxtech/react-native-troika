#import "RNNestedScrollViewHeaderManager.h"
#import "RNNestedScrollViewHeader.h"

@implementation RNNestedScrollViewHeaderManager

RCT_EXPORT_MODULE(NestedScrollViewHeader)

- (UIView *)view {
    return [[RNNestedScrollViewHeader alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(fixedHeight, CGFloat)

RCT_EXPORT_VIEW_PROPERTY(stickyHeaderBeginIndex, NSUInteger)


@end

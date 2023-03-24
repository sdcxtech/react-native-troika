#import "RNNestedScrollViewManager.h"
#import "RNNestedScrollView.h"
#import "RNNestedScrollShadowView.h"

@implementation RNNestedScrollViewManager

RCT_EXPORT_MODULE(NestedScrollView)

- (UIView *)view {
    return [[RNNestedScrollView alloc] initWithBridge:self.bridge];
}

- (RCTShadowView *)shadowView {
    return [RNNestedScrollShadowView new];
}

RCT_EXPORT_VIEW_PROPERTY(bounces, BOOL)

@end

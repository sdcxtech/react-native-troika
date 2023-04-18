#import "RNRefreshHeaderShadowView.h"

#import <Yoga/YGValue.h>

@implementation RNRefreshHeaderShadowView

- (void)layoutWithMetrics:(RCTLayoutMetrics)layoutMetrics layoutContext:(RCTLayoutContext)layoutContext {
    if (!RCTLayoutMetricsEqualToLayoutMetrics(self.layoutMetrics, layoutMetrics)) {
        self.layoutMetrics = layoutMetrics;
        [layoutContext.affectedShadowViews addObject:self];
        self.top = (YGValue){-layoutMetrics.frame.size.height, YGUnitPoint};
        self.left = YGValueZero;
        self.right = YGValueZero;
        self.position = YGPositionTypeAbsolute;
    }
}

@end

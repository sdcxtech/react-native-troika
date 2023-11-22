#import "RNRefreshFooterShadowView.h"
#import "RNRefreshFooterLocalData.h"

@implementation RNRefreshFooterShadowView

- (void)setLocalData:(RNRefreshFooterLocalData *)localData {
    CGSize size = localData.scrollViewContentSize;
    self.top = (YGValue){size.height, YGUnitPoint};
    self.bottom = YGValueUndefined;
    self.left = YGValueZero;
    self.right = YGValueZero;
    self.position = YGPositionTypeAbsolute;
}

@end

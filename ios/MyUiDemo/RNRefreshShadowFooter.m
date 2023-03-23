#import "RNRefreshShadowFooter.h"
#import "RNRefreshFooterLocalData.h"

@implementation RNRefreshShadowFooter

- (void)setLocalData:(RNRefreshFooterLocalData *)localData {
    CGSize size = localData.scrollViewContentSize;
    self.top = (YGValue){size.height, YGUnitPoint};
    self.left = YGValueZero;
    self.right = YGValueZero;
    self.position = YGPositionTypeAbsolute;
}

@end

#import "RNRefreshFooterShadowView.h"
#import "RNRefreshFooterLocalData.h"

@implementation RNRefreshFooterShadowView

- (instancetype)init {
    if (self = [super init]) {
        self.top = (YGValue){1000, YGUnitPoint};
        self.bottom = YGValueUndefined;
        self.left = YGValueZero;
        self.right = YGValueZero;
        self.position = YGPositionTypeAbsolute;
    }
    return self;
}

- (void)setLocalData:(RNRefreshFooterLocalData *)localData {
    CGSize size = localData.scrollViewContentSize;
    self.top = (YGValue){size.height, YGUnitPoint};
    self.bottom = YGValueUndefined;
    self.left = YGValueZero;
    self.right = YGValueZero;
    self.position = YGPositionTypeAbsolute;
}

@end

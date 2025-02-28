#import "RNRefreshHeaderShadowView.h"
#import "RNRefreshHeaderLocalData.h"

#import <Yoga/YGValue.h>

@implementation RNRefreshHeaderShadowView

- (instancetype)init {
    if (self = [super init]) {
        self.top = (YGValue){-1000, YGUnitPoint};
        self.bottom = YGValueUndefined;
        self.left = YGValueZero;
        self.right = YGValueZero;
        self.position = YGPositionTypeAbsolute;
    }
    return self;
}

- (void)setLocalData:(RNRefreshHeaderLocalData *)localData {
    self.top = (YGValue){-localData.height, YGUnitPoint};
    self.bottom = YGValueUndefined;
    self.left = YGValueZero;
    self.right = YGValueZero;
    self.position = YGPositionTypeAbsolute;
}

@end

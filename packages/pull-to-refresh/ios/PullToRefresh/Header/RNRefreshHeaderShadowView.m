#import "RNRefreshHeaderShadowView.h"
#import "RNRefreshHeaderLocalData.h"

#import <Yoga/YGValue.h>

@implementation RNRefreshHeaderShadowView

- (void)setLocalData:(RNRefreshHeaderLocalData *)localData {
    self.top = (YGValue){-localData.height, YGUnitPoint};
    self.bottom = YGValueUndefined;
    self.left = YGValueZero;
    self.right = YGValueZero;
    self.position = YGPositionTypeAbsolute;
}

@end

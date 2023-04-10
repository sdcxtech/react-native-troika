#import "RNNestedScrollShadowView.h"
#import "RNNestedScrollViewLocalData.h"

#import <React/RCTAssert.h>

@implementation RNNestedScrollShadowView

- (void)setLocalData:(RNNestedScrollViewLocalData *)localData {
    CGSize size = localData.scrollingChildSize;
    NSUInteger count = self.reactSubviews.count;

    RCTAssert(count <= 2, @"`NestedScrollView` can have at most two child component.");

    for (NSUInteger i = 0; i < count; i ++) {
        if (i == 1) {
            RCTShadowView *shadow = self.reactSubviews[i];
            [shadow setSize:size];
            [shadow setMinHeight:(YGValue){size.height, YGUnitPoint}];
        }
    }
}

@end

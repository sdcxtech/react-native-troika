#import "RNNestedScrollViewHeader.h"
#import "RNNestedScrollView.h"

#import <React/UIView+React.h>
#import <React/RCTAssert.h>

@implementation RNNestedScrollViewHeader

- (CGFloat)maxScrollRange {
    if (self.fixedHeight > 0) {
        return fmax(self.frame.size.height - self.fixedHeight, 0);
    }
    
    if (self.stickyHeaderBeginIndex > 0 && self.stickyHeaderBeginIndex < self.subviews.count) {
        CGFloat range = 0.0;
        for (NSUInteger i = 0; i < self.subviews.count; i++) {
            if (i == self.stickyHeaderBeginIndex) {
                break;
            }
            UIView *child = self.subviews[i];
            range += child.frame.size.height;
        }
        return range;
    }
    
    return self.frame.size.height;
}

-(void)didSetProps:(NSArray<NSString *> *)props {
    if ([props containsObject:@"fixedHeight"] || [props containsObject:@"stickyHeaderBeginIndex"]) {
        [self notityContentSizeChanged];
    }
}

- (void)reactSetFrame:(CGRect)frame {
    [super reactSetFrame:frame];
}

- (void)layoutSubviews {
    [super layoutSubviews];
}

- (void)notityContentSizeChanged {
    RNNestedScrollView *scrollView = (RNNestedScrollView *)self.superview.superview;
    if (!scrollView) {
      return;
    }

    RCTAssert([scrollView isKindOfClass:[RNNestedScrollView class]], @"Unexpected view hierarchy of NestedScrollView component.");
    [scrollView updateContentSizeIfNeeded];
}

@end

#import "RNNestedScrollShadowView.h"
#import "RNNestedScrollViewLocalData.h"

#import <React/RCTAssert.h>

@interface RNNestedScrollShadowView ()

@property(nonatomic, assign) CGSize scrollingChildSize;

@end

@implementation RNNestedScrollShadowView

- (void)setLocalData:(RNNestedScrollViewLocalData *)localData {
    CGSize size = localData.scrollingChildSize;
    NSUInteger count = self.reactSubviews.count;

    RCTAssert(count <= 2, @"`NNestedScrollView` can have at most two child component.");

    for (NSUInteger i = 0; i < count; i ++) {
        if (i == 1) {
            RCTShadowView *shadow = self.reactSubviews[i];
            [shadow setSize:size];
        }
    }
    
    self.scrollingChildSize = localData.scrollingChildSize;
}

- (void)layoutSubviewsWithContext:(RCTLayoutContext)layoutContext {
    if (self.reactSubviews.count < 2) {
        [super layoutSubviewsWithContext:layoutContext];
        return;
    }
    
    RCTAssert(self.reactSubviews.count == 2, @"`NNestedScrollView` can have at most two child component.");
    
    RCTLayoutMetrics layoutMetrics = self.layoutMetrics;
    if (layoutMetrics.displayType == RCTDisplayTypeNone) {
      return;
    }
    
    RCTShadowView *header = self.reactSubviews[0];
    RCTShadowView *scrolling = self.reactSubviews[1];
    YGNodeRef scrollingYogaNode = scrolling.yogaNode;

    RCTLayoutMetrics scrollingLayoutMetrics = RCTLayoutMetricsFromYogaNode(scrollingYogaNode);
    
    if (CGSizeEqualToSize(CGSizeZero, _scrollingChildSize) || CGSizeEqualToSize(scrollingLayoutMetrics.frame.size, _scrollingChildSize)) {
        [super layoutSubviewsWithContext:layoutContext];
        return;
    }
    
    [self layoutChild:header withContext:layoutContext];
    
    [scrolling layoutWithMinimumSize:_scrollingChildSize maximumSize:_scrollingChildSize layoutDirection:layoutMetrics.layoutDirection layoutContext:layoutContext];
}

- (void)layoutChild:(RCTShadowView *)child withContext:(RCTLayoutContext)layoutContext {
    YGNodeRef childYogaNode = child.yogaNode;

    RCTAssert(!YGNodeIsDirty(childYogaNode), @"Attempt to get layout metrics from dirtied Yoga node.");

    if (!YGNodeGetHasNewLayout(childYogaNode)) {
        return;
    }

    YGNodeSetHasNewLayout(childYogaNode, false);
      
    RCTLayoutMetrics childLayoutMetrics = RCTLayoutMetricsFromYogaNode(childYogaNode);

    layoutContext.absolutePosition.x += childLayoutMetrics.frame.origin.x;
    layoutContext.absolutePosition.y += childLayoutMetrics.frame.origin.y;

    [child layoutWithMetrics:childLayoutMetrics layoutContext:layoutContext];

    // Recursive call.
    [child layoutSubviewsWithContext:layoutContext];
}


@end

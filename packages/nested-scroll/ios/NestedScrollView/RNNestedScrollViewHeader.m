#import "RNNestedScrollViewHeader.h"
#import "RNNestedScrollView.h"
#import "RNNestedScrollEvent.h"

#import <React/UIView+React.h>
#import <React/RCTAssert.h>

static const CGFloat InvalidStickyHeight = -1;
static const NSUInteger InvalidStickyBeginIndex = NSUIntegerMax;

@implementation RNNestedScrollViewHeader {
    BOOL _hasObserver;
}

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher {
    if (self = [super init]) {
        _stickyHeight = InvalidStickyHeight;
        _stickyHeaderBeginIndex = InvalidStickyBeginIndex;
        _eventDispatcher = eventDispatcher;
    }
    return self;
}

- (CGFloat)maxScrollRange {
    if (self.stickyHeight >= 0) {
        return fmax(self.frame.size.height - self.stickyHeight, 0);
    }
    
    if (self.stickyHeaderBeginIndex != InvalidStickyBeginIndex) {
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
    if ([props containsObject:@"stickyHeight"] || [props containsObject:@"stickyHeaderBeginIndex"]) {
        [self notityContentSizeChanged];
    }
}

- (void)reactSetFrame:(CGRect)frame {
    [super reactSetFrame:frame];
    [self notityContentSizeChanged];
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

- (void)willMoveToWindow:(UIWindow *)newWindow {
    [super willMoveToWindow:newWindow];
    if (newWindow) {
        [self addObserver];
    } else {
        [self removeObserver];
    }
}

- (void)addObserver {
    if (!_hasObserver && self.superview) {
        NSKeyValueObservingOptions options = NSKeyValueObservingOptionNew | NSKeyValueObservingOptionOld;
        [self.superview addObserver:self forKeyPath:@"contentOffset" options:options context:nil];
        _hasObserver = YES;
    }
}

- (void)removeObserver {
    if (_hasObserver && self.superview) {
        [self.superview removeObserver:self forKeyPath:@"contentOffset" context:nil];
        _hasObserver = NO;
    }
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context {
    if ([keyPath isEqualToString:@"contentOffset"]) {
        CGPoint offset = [change[@"new"] CGPointValue];
        [self.eventDispatcher sendEvent:[[RNNestedScrollEvent alloc] initWithReactTag:self.reactTag offset:offset]];
    }
}

@end

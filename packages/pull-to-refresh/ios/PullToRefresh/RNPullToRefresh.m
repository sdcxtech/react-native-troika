#import "RNPullToRefresh.h"
#import "RNRefreshHeader.h"
#import "RNRefreshFooter.h"

#import <React/UIView+React.h>
#import <React/RCTLog.h>

@interface RNPullToRefresh ()

@property(nonatomic, weak) RNRefreshHeader *header;
@property(nonatomic, weak) RNRefreshFooter *footer;
@property(nonatomic, weak) UIScrollView *scrollView;

@end

@implementation RNPullToRefresh

- (void)insertReactSubview:(UIView *)subview atIndex:(NSInteger)atIndex {
    [super insertReactSubview:subview atIndex:atIndex];
    if ([subview isKindOfClass:[RNRefreshHeader class]]) {
        self.header = (RNRefreshHeader *)subview;
        [self assembleIfNeeded];
    } else if ([subview isKindOfClass:[RNRefreshFooter class]]) {
        self.footer = (RNRefreshFooter *)subview;
        [self assembleIfNeeded];
    } else {
        RCTAssert(self.subviews.count == 0, @"PullToRefresh may only contain a single subview.");
        [self addSubview:subview];
        self.scrollView = [self findScrollView:subview];
        [self assembleIfNeeded];
    }
}

- (void)assembleIfNeeded {
    if (self.scrollView) {
        self.scrollView.bounces = YES;
        if (self.header) {
            [self.header removeFromSuperview];
            [self.scrollView addSubview:self.header];
            self.header.scrollView = self.scrollView;
        }
        if (self.footer) {
            [self.footer removeFromSuperview];
            [self.scrollView addSubview:self.footer];
            self.footer.scrollView = self.scrollView;
        }
    }
}

- (void)removeReactSubview:(UIView *)subview {
    [super removeReactSubview:subview];
}

- (void)didUpdateReactSubviews {
    // Do nothing, as subviews are managed by `insertReactSubview:atIndex:`
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if (self.scrollView == nil) {
        self.scrollView = [self findScrollView:self];
        [self assembleIfNeeded];
    }
}

- (UIScrollView *)findScrollView:(UIView *)superview {
    for (UIView *subview in superview.subviews) {
        if ([subview isKindOfClass:[UIScrollView class]]) {
            return (UIScrollView *)subview;
        }
    }
    
    for (UIView *subview in superview.subviews) {
        UIScrollView *target = [self findScrollView:subview];
        if (target != nil) {
            return target;
        }
    }
    
    return nil;
}

@end

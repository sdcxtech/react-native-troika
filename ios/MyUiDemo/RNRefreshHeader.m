#import "RNRefreshHeader.h"
#import "RNRefreshState.h"

#import <React/RCTRefreshableProtocol.h>
#import <React/RCTLog.h>

@interface RNRefreshHeader () <RCTRefreshableProtocol>

@property(nonatomic, assign) RNRefreshState state;
@property(nonatomic, assign) CGFloat topInset;

@end

@implementation RNRefreshHeader {
    BOOL _isInitialRender;
    BOOL _hasObserver;
}

- (instancetype)init {
    if (self = [super init]) {
        _isInitialRender = YES;
        _hasObserver = NO;
        _state = RNRefreshStateIdle;
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    if (self.backgroundColor == nil) {
        self.backgroundColor = [UIColor clearColor];
    }
    
    if (self.state == RNRefreshStateRefreshing && _isInitialRender) {
        [self animateToRefreshingState];
    }
    _isInitialRender = false;
}

- (void)setScrollView:(UIScrollView *)scrollView {
    _scrollView = scrollView;
    [self addObserver];
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
    if (!_hasObserver && self.scrollView) {
        NSKeyValueObservingOptions options = NSKeyValueObservingOptionNew | NSKeyValueObservingOptionOld;
        [self.scrollView addObserver:self forKeyPath:@"contentOffset" options:options context:nil];
        _hasObserver = YES;
    }
}

- (void)removeObserver {
    if (_hasObserver && self.scrollView) {
        [self.scrollView removeObserver:self forKeyPath:@"contentOffset" context:nil];
        _hasObserver = NO;
    }
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context {
    if ([keyPath isEqualToString:@"contentOffset"]) {
        
        CGFloat offsetY = self.scrollView.contentOffset.y;
        CGFloat insetT = -self.scrollView.contentInset.top;
        
        if (self.onOffsetChanged && offsetY <= 0) {
            self.onOffsetChanged(@{
                @"offset": @(fabs(offsetY))
            });
        }
        
        if (self.state == RNRefreshStateRefreshing) {
            return;
        }
        
        if (offsetY > insetT) {
            return;
        }
        
        CGFloat range = self.scrollView.contentInset.top + self.bounds.size.height;
        
        if (self.scrollView.isDragging) {
            if (self.state == RNRefreshStateIdle && fabs(offsetY) >= range) {
                self.state = RNRefreshStateComing;
                
            } else
            if (self.state == RNRefreshStateComing && fabs(offsetY) <= range) {
                self.state = RNRefreshStateIdle;
            }
            return;
        }
        
        if (self.state == RNRefreshStateComing) {
            // 松开手
            [self beginRefreshing];
            return;
        }
    }
}

@dynamic refreshing;

- (BOOL)isRefreshing {
    return self.state == RNRefreshStateRefreshing;
}

- (void)setRefreshing:(BOOL)refreshing {
    if (refreshing) {
        [self beginRefreshing];
    } else {
        [self endRefreshing];
    }
}

- (void)beginRefreshing {
    [self setState:RNRefreshStateRefreshing];
}

- (void)endRefreshing {
    [self setState:RNRefreshStateIdle];
}

- (void)setState:(RNRefreshState)state {
    if (_state == state || !self.scrollView) {
        return;
    }
    
    RNRefreshState old = _state;
    _state = state;
    
    if (_isInitialRender) {
        return;
    }
    
    if (self.onStateChanged) {
        self.onStateChanged(@{
            @"state": @(state)
        });
    }
    
    if (state == RNRefreshStateIdle && old == RNRefreshStateRefreshing) {
        [self animateToIdleState];
        return;
    }
    
    if (state == RNRefreshStateRefreshing) {
        [self animateToRefreshingState];
        
        if (self.onRefresh) {
            self.onRefresh(nil);
        }
        return;
    }
}

- (void)animateToIdleState {
    [UIView animateWithDuration:0.2 animations:^{
        UIScrollView *scrollView = self.scrollView;
        UIEdgeInsets insets = scrollView.contentInset;
        scrollView.contentInset = UIEdgeInsetsMake(self.topInset, insets.left, insets.bottom, insets.right);
    } completion:NULL];
}

- (void)animateToRefreshingState {
    [UIView animateWithDuration:0.2 animations:^{
        UIScrollView *scrollView = self.scrollView;
        CGFloat range = scrollView.contentInset.top + self.bounds.size.height;
        UIEdgeInsets insets = scrollView.contentInset;
        self.topInset = insets.top;
        [scrollView setContentInset:UIEdgeInsetsMake(range, insets.left, insets.bottom, insets.right)];
        CGPoint offset = {scrollView.contentOffset.x, -range};
        [scrollView setContentOffset:offset animated:NO];
    } completion:NULL];
}

@end

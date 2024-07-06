#import "RNRefreshHeader.h"
#import "RNRefreshState.h"
#import "RNRefreshingEvent.h"
#import "RNRefreshOffsetChangedEvent.h"
#import "RNRefreshStateChangedEvent.h"
#import "RNRefreshHeaderLocalData.h"

#import <React/RCTRefreshableProtocol.h>
#import <React/UIView+React.h>
#import <React/RCTRootContentView.h>
#import <React/RCTTouchHandler.h>
#import <React/RCTUIManager.h>
#import <React/RCTLog.h>

@interface RNRefreshHeader () <RCTRefreshableProtocol>

@property(nonatomic, assign) RNRefreshState state;
@property(nonatomic, assign) CGFloat topInset;
@property(nonatomic, weak) RCTBridge *bridge;

@end

@implementation RNRefreshHeader {
    BOOL _isInitialRender;
    BOOL _hasObserver;
    __weak RCTRootContentView *_rootView;
}

- (instancetype)initWithBridge:(RCTBridge *)bridge {
    if (self = [super init]) {
        _isInitialRender = YES;
        _hasObserver = NO;
        _state = RNRefreshStateIdle;
        _bridge = bridge;
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    [self setLocalData];
    
    if (self.backgroundColor == nil) {
        self.backgroundColor = [UIColor clearColor];
    }
    
    dispatch_async(dispatch_get_main_queue(), ^{
        if (self.state == RNRefreshStateRefreshing && self->_isInitialRender) {
            [self settleToRefreshing];
        }
        self->_isInitialRender = NO;
    });
}

- (void)reactSetFrame:(CGRect)frame {
    [super reactSetFrame:frame];
    [self setLocalData];
}

- (void)setFrame:(CGRect)frame {
    if (frame.origin.y != frame.size.height) {
        [self setLocalData];
        return;
    }
    [super setFrame:frame];
}

- (void)setLocalData {
    if (self.scrollView && self.frame.size.height != 0) {
        if (self.frame.origin.y != -self.frame.size.height) {
            RNRefreshHeaderLocalData *localData = [[RNRefreshHeaderLocalData alloc] initWithHeaderHeight:self.frame.size.height];
            [self.bridge.uiManager setLocalData:localData forView:self];
        }
    }
}

- (void)setScrollView:(UIScrollView *)scrollView {
    [self removeObserver];
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

- (void)didMoveToWindow {
    [super didMoveToWindow];
    if (self.window) {
        [self cacheRootView];
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
        
        if (offsetY <= 0) {
            [self.bridge.eventDispatcher sendEvent:[[RNRefreshOffsetChangedEvent alloc] initWithViewTag:self.reactTag offset:fabs(offsetY)]];
        }
        
        if (self.state == RNRefreshStateRefreshing) {
            return;
        }
        
        if (offsetY > insetT) {
            return;
        }
        
        CGFloat range = self.scrollView.contentInset.top + self.bounds.size.height;
        
        if (self.scrollView.isDragging) {
            [self cancelRootViewTouches];
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
    if (_isInitialRender) {
        _state = state;
        return;
    }
    
    if (_state == state || !self.scrollView) {
        return;
    }
    
    RNRefreshState old = _state;
    _state = state;

    [self.bridge.eventDispatcher sendEvent:[[RNRefreshStateChangedEvent alloc] initWithViewTag:self.reactTag refreshState:state]];
    
    if (state == RNRefreshStateIdle && old == RNRefreshStateRefreshing) {
        [self animateToIdleState];
        return;
    }
    
    if (state == RNRefreshStateRefreshing) {
        [self animateToRefreshingState];
        [self.bridge.eventDispatcher sendEvent:[[RNRefreshingEvent alloc] initWithViewTag:self.reactTag]];
        return;
    }
}

- (void)settleToRefreshing {
    [self.bridge.eventDispatcher sendEvent:[[RNRefreshStateChangedEvent alloc] initWithViewTag:self.reactTag refreshState:RNRefreshStateRefreshing]];
    
    [self animateToRefreshingState];
    
    [self.bridge.eventDispatcher sendEvent:[[RNRefreshingEvent alloc] initWithViewTag:self.reactTag]];
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

- (void)cacheRootView {
  UIView *rootView = self;
  while (rootView.superview && ![rootView isReactRootView]) {
    rootView = rootView.superview;
  }
  _rootView = (RCTRootContentView *)rootView;
}

- (void)cancelRootViewTouches {
    RCTRootContentView *rootView = (RCTRootContentView *)_rootView;
    [rootView.touchHandler cancel];
}

@end

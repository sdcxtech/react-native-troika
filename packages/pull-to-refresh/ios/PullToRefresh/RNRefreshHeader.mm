#import "RNRefreshHeader.h"
#import "RNRefreshState.h"
#import "RNRefreshingEvent.h"
#import "RNRefreshOffsetChangedEvent.h"
#import "RNRefreshStateChangedEvent.h"

#import <react/renderer/components/pulltorefresh/PullToRefreshHeaderComponentDescriptor.h>
#import <react/renderer/components/pulltorefresh/EventEmitters.h>
#import <react/renderer/components/pulltorefresh/Props.h>
#import <react/renderer/components/pulltorefresh/RCTComponentViewHelpers.h>

#import <React/RCTConversions.h>
#import <React/RCTLog.h>
#import <React/RCTRefreshableProtocol.h>
#import <React/UIView+React.h>

using namespace facebook::react;

@interface RNRefreshHeader () <RCTRefreshableProtocol>

@property(nonatomic, assign) RNRefreshState state;
@property(nonatomic, assign) CGFloat topInset;

@end

@implementation RNRefreshHeader {
    BOOL _isInitialRender;
    BOOL _hasObserver;
}

// Needed because of this: https://github.com/facebook/react-native/pull/37274
+ (void)load {
	[super load];
}

+ (ComponentDescriptorProvider)componentDescriptorProvider {
	return concreteComponentDescriptorProvider<PullToRefreshHeaderComponentDescriptor>();
}

+ (BOOL)shouldBeRecycled {
	return NO;
}

- (instancetype)initWithFrame:(CGRect)frame {
	if (self = [super initWithFrame:frame]) {
		static const auto defaultProps = std::make_shared<const PullToRefreshHeaderProps>();
		_props = defaultProps;
        _isInitialRender = YES;
        _hasObserver = NO;
        _state = RNRefreshStateIdle;
    }
    return self;
}

- (void)prepareForRecycle {
	[super prepareForRecycle];
	_isInitialRender = YES;
	_hasObserver = NO;
	_state = RNRefreshStateIdle;
	self.refreshing = NO;
}

- (void)updateProps:(const facebook::react::Props::Shared &)props oldProps:(const facebook::react::Props::Shared &)oldProps {
	const auto &oldViewProps = static_cast<const PullToRefreshHeaderProps &>(*_props);
	const auto &newViewProps = static_cast<const PullToRefreshHeaderProps &>(*props);
	
	// `refreshing`
	if (newViewProps.refreshing != oldViewProps.refreshing) {
		self.refreshing = newViewProps.refreshing;
	}
	
	[super updateProps:props oldProps:oldProps];
}

- (const PullToRefreshHeaderEventEmitter &)eventEmitter {
	return static_cast<const PullToRefreshHeaderEventEmitter &>(*_eventEmitter);
}

- (void)layoutSubviews {
    [super layoutSubviews];

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
			[self eventEmitter].onOffsetChanged({
				.offset = offsetY
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
    if (_isInitialRender) {
        _state = state;
        return;
    }
    
    if (_state == state || !self.scrollView) {
        return;
    }
    
    RNRefreshState old = _state;
    _state = state;

    if (state == RNRefreshStateIdle && old == RNRefreshStateRefreshing) {
        [self settleToIdle];
        return;
    }
    
    if (state == RNRefreshStateRefreshing) {
        [self settleToRefreshing];
        return;
    }
    
    RCTLogInfo(@"[pull-to-refresh] publish comming event");
	[self eventEmitter].onStateChanged({
		.state = static_cast<int>(state)
	});
}

- (void)settleToRefreshing {
    RCTLogInfo(@"[pull-to-refresh] settleToRefreshing");
    [self animateToRefreshingState:^(BOOL finished) {
        if (self.state == RNRefreshStateRefreshing) {
            RCTLogInfo(@"[pull-to-refresh] publish refresh event");
			[self eventEmitter].onStateChanged({
				.state = static_cast<int>(RNRefreshStateRefreshing)
			});
            
			[self eventEmitter].onRefresh({});
        }
    }];
}

- (void)settleToIdle {
    RCTLogInfo(@"[pull-to-refresh] settleToIdle");
    [self animateToIdleState:^(BOOL finished) {
        if (self.state == RNRefreshStateIdle) {
            RCTLogInfo(@"[pull-to-refresh] publish idle event");
			[self eventEmitter].onStateChanged({
				.state = static_cast<int>(RNRefreshStateIdle)
			});
        }
    }];
}

- (void)animateToIdleState:(void (^ __nullable)(BOOL finished))completion {
    [UIView animateWithDuration:0.2 delay:0 options:UIViewAnimationOptionBeginFromCurrentState animations:^{
        UIScrollView *scrollView = self.scrollView;
        UIEdgeInsets insets = scrollView.contentInset;
        scrollView.contentInset = UIEdgeInsetsMake(self.topInset, insets.left, insets.bottom, insets.right);
    } completion:completion];
}

- (void)animateToRefreshingState:(void (^ __nullable)(BOOL finished))completion {
    [UIView animateWithDuration:0.2 animations:^{
        UIScrollView *scrollView = self.scrollView;
        CGFloat range = scrollView.contentInset.top + self.bounds.size.height;
        UIEdgeInsets insets = scrollView.contentInset;
        self.topInset = insets.top;
        [scrollView setContentInset:UIEdgeInsetsMake(range, insets.left, insets.bottom, insets.right)];
        CGPoint offset = {scrollView.contentOffset.x, -range};
        [scrollView setContentOffset:offset animated:NO];
    } completion:completion];
}

@end

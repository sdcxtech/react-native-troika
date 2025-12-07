#import "RNRefreshFooter.h"
#import "RNRefreshState.h"
#import "RNRefreshingEvent.h"
#import "RNRefreshOffsetChangedEvent.h"
#import "RNRefreshStateChangedEvent.h"

#import <react/renderer/components/pulltorefresh/PullToRefreshFooterComponentDescriptor.h>
#import <react/renderer/components/pulltorefresh/EventEmitters.h>
#import <react/renderer/components/pulltorefresh/Props.h>
#import <react/renderer/components/pulltorefresh/RCTComponentViewHelpers.h>

#import <React/RCTConversions.h>
#import <React/RCTLog.h>
#import <React/RCTRefreshableProtocol.h>

using namespace facebook::react;

@interface RNRefreshFooter () <RCTRefreshableProtocol>

@property(nonatomic, assign) RNRefreshState state;
@property(nonatomic, assign) CGFloat bottomInset;

@end

@implementation RNRefreshFooter {
	PullToRefreshFooterShadowNode::ConcreteState::Shared _shadowState;
    BOOL _hasObserver;
}

// Needed because of this: https://github.com/facebook/react-native/pull/37274
+ (void)load {
	[super load];
}

+ (ComponentDescriptorProvider)componentDescriptorProvider {
	return concreteComponentDescriptorProvider<PullToRefreshFooterComponentDescriptor>();
}

+ (BOOL)shouldBeRecycled {
	return NO;
}

- (instancetype)initWithFrame:(CGRect)frame {
	if (self = [super initWithFrame:frame]) {
		static const auto defaultProps = std::make_shared<const PullToRefreshFooterProps>();
		_props = defaultProps;
        _hasObserver = NO;
        _state = RNRefreshStateIdle;
        _noMoreData = NO;
        _manual = NO;
    }
    return self;
}

- (void)prepareForRecycle {
	[super prepareForRecycle];
	_hasObserver = NO;
	_state = RNRefreshStateIdle;
	_noMoreData = NO;
	_manual = NO;
}

- (void)updateState:(const facebook::react::State::Shared &)state oldState:(const facebook::react::State::Shared &)oldState {
	_shadowState = std::static_pointer_cast<PullToRefreshFooterShadowNode::ConcreteState const>(state);
}

- (void)updateLayoutMetrics:(const facebook::react::LayoutMetrics &)layoutMetrics oldLayoutMetrics:(const facebook::react::LayoutMetrics &)oldLayoutMetrics {
	[super updateLayoutMetrics:layoutMetrics oldLayoutMetrics:oldLayoutMetrics];

	[self adjustContentInset];
}

- (void)updateProps:(const facebook::react::Props::Shared &)props oldProps:(const facebook::react::Props::Shared &)oldProps {
	const auto &oldViewProps = static_cast<const PullToRefreshFooterProps &>(*_props);
	const auto &newViewProps = static_cast<const PullToRefreshFooterProps &>(*props);
	
	// `refreshing`
	if (newViewProps.refreshing != oldViewProps.refreshing) {
		self.refreshing = newViewProps.refreshing;
	}
	
	// `noMoreData`
	if (newViewProps.noMoreData != oldViewProps.noMoreData) {
		self.noMoreData = newViewProps.noMoreData;
	}
	
	// `manual`
	if (newViewProps.manual != oldViewProps.manual) {
		self.manual = newViewProps.manual;
	}
	
	[super updateProps:props oldProps:oldProps];
}

- (const PullToRefreshFooterEventEmitter &)eventEmitter {
	return static_cast<const PullToRefreshFooterEventEmitter &>(*_eventEmitter);
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if (self.backgroundColor == nil) {
        self.backgroundColor = [UIColor clearColor];
    }
}

- (void)adjustContentInset {
	self.hidden = ![self isFullScrollView];
	if (!self.manual) {
		// 和下拉刷新有冲突
		[self setScrollViewContentInset];
	}
}

- (void)setScrollViewContentInset {
    UIEdgeInsets insets = self.scrollView.contentInset;
    if (!self.hidden) {
        self.scrollView.contentInset = UIEdgeInsetsMake(insets.top, insets.left, self.frame.size.height, insets.right);
    } else {
        self.scrollView.contentInset = UIEdgeInsetsMake(insets.top, insets.left, 0, insets.right);
    }
}

- (void)updateState {
    if (self.scrollView && self.frame.size.height != 0) {
        CGSize contentSize = self.scrollView.contentSize;
        if (self.frame.origin.y != contentSize.height) {
			_shadowState->updateState(
									  [=](PullToRefreshFooterShadowNode::ConcreteState::Data const &oldData)
					  -> PullToRefreshFooterShadowNode::ConcreteState::SharedData {
						  auto newData = oldData;
						  newData.contentHeight = contentSize.height;
						  return std::make_shared<PullToRefreshFooterShadowNode::ConcreteState::Data const>(newData);
				  });
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

- (void)addObserver {
    if (!_hasObserver && self.scrollView) {
        NSKeyValueObservingOptions options = NSKeyValueObservingOptionNew | NSKeyValueObservingOptionOld;
        [self.scrollView addObserver:self forKeyPath:@"contentOffset" options:options context:nil];
        [self.scrollView addObserver:self forKeyPath:@"contentSize" options:options context:nil];
        _hasObserver = YES;
    }
}

- (void)removeObserver {
    if (_hasObserver && self.scrollView) {
        [self.scrollView removeObserver:self forKeyPath:@"contentOffset" context:nil];
        [self.scrollView removeObserver:self forKeyPath:@"contentSize" context:nil];
        _hasObserver = NO;
    }
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context {
    if ([keyPath isEqualToString:@"contentSize"]) {
		[self adjustContentInset];
        [self updateState];
    }
    
    if ([keyPath isEqualToString:@"contentOffset"]) {
        
        // 马上可看见 footer
        CGFloat minRange = self.scrollView.contentSize.height - self.scrollView.frame.size.height;
        
        if (self.scrollView.contentOffset.y >= minRange) {
            CGFloat offset = self.scrollView.contentOffset.y - minRange;
			[self eventEmitter].onOffsetChanged({
				.offset = offset
			});
        }
        
        if (self.hidden || self.noMoreData) {
            return;
        }
        
        if (self.state == RNRefreshStateRefreshing) {
            return;
        }
        
        if (![self isFullScrollView]) { // 内容不满一屏
            return;
        }
        
        CGFloat offset = self.scrollView.contentOffset.y;
        
        if (self.scrollView.isDragging) {
            // TODO： 取消点击事件
        }
        
        if (self.manual) {
            if (offset < minRange) {
                // 未到临界点，返回
                return;
            }
            
            // 完全可看见 footer
            CGFloat maxRange = minRange + self.bounds.size.height;
            
            if (self.scrollView.isDragging) {
                if (self.state == RNRefreshStateIdle && offset >= maxRange) {
                    self.state = RNRefreshStateComing;
                    
                } else
                if (self.state == RNRefreshStateComing && offset <= maxRange) {
                    self.state = RNRefreshStateIdle;
                }
                return;
            }
            
            if (self.state == RNRefreshStateComing) {
                // 松开手
                [self beginRefreshing];
                return;
            }
        } else {
			CGFloat newY = [change[@"new"] CGPointValue].y;
			CGFloat oldY = [change[@"old"] CGPointValue].y;
            
            CGFloat range = self.scrollView.contentSize.height - self.scrollView.frame.size.height + self.frame.size.height * 0.3;
            if (newY > oldY && offset >= range) {
                if (self.state == RNRefreshStateIdle) {
                    [self beginRefreshing];
                    return;
                }
            }
        }
    }
}

-(BOOL)isFullScrollView { // 内容是否能撑满 scrollView
    CGFloat range = self.scrollView.contentInset.top + self.scrollView.contentSize.height;
    CGFloat height = self.scrollView.frame.size.height;
    return range >= height;
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

	[self eventEmitter].onStateChanged({
		.state = static_cast<int>(state)
	});
    
    if (state == RNRefreshStateIdle && old == RNRefreshStateRefreshing) {
        if (self.manual) {
            [self animateToIdleState];
        }
        return;
    }

    if (state == RNRefreshStateRefreshing) {
        if (self.manual) {
            [self animateToRefreshingState];
        }
		[self eventEmitter].onRefresh({});
        return;
    }
}

- (void)animateToIdleState {
    [UIView animateWithDuration:0.2 animations:^{
        UIScrollView *scrollView = self.scrollView;
        UIEdgeInsets insets = scrollView.contentInset;
        scrollView.contentInset = UIEdgeInsetsMake(insets.top, insets.left, self.bottomInset, insets.right);
    } completion:NULL];
}

- (void)animateToRefreshingState {
    [UIView animateWithDuration:0.2 animations:^{
        UIScrollView *scrollView = self.scrollView;
        CGFloat range = scrollView.contentSize.height - scrollView.frame.size.height + self.bounds.size.height;
        UIEdgeInsets insets = scrollView.contentInset;
        self.bottomInset = insets.bottom;
        [scrollView setContentInset:UIEdgeInsetsMake(insets.top, insets.left, self.frame.size.height, insets.right)];
        CGPoint offset = {scrollView.contentOffset.x, range};
        [scrollView setContentOffset:offset animated:NO];
    } completion:NULL];
}

@end

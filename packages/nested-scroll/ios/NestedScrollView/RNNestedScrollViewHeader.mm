#import "RNNestedScrollViewHeader.h"
#import "RNNestedScrollView.h"
#import "RNNestedScrollEvent.h"

#import <react/renderer/components/nestedscrollview/ComponentDescriptors.h>
#import <react/renderer/components/nestedscrollview/EventEmitters.h>
#import <react/renderer/components/nestedscrollview/Props.h>
#import <react/renderer/components/nestedscrollview/RCTComponentViewHelpers.h>

#import <React/RCTConversions.h>
#import <React/RCTLog.h>
#import <React/RCTAssert.h>


static void
RCTSendScrollEventForNativeAnimations_DEPRECATED(NSInteger tag, CGPoint offset) {
	RNNestedScrollEvent *scrollEvent =	[[RNNestedScrollEvent alloc] initWithReactTag:@(tag) offset:offset];
	NSDictionary *userInfo = [NSDictionary dictionaryWithObjectsAndKeys:scrollEvent, @"event", nil];
	[[NSNotificationCenter defaultCenter] postNotificationName:RCTNotifyEventDispatcherObserversOfEvent_DEPRECATED
													  object:nil
													userInfo:userInfo];
}


using namespace facebook::react;

@implementation RNNestedScrollViewHeader {
    BOOL _hasObserver;
}

// Needed because of this: https://github.com/facebook/react-native/pull/37274
+ (void)load {
	[super load];
}

+ (ComponentDescriptorProvider)componentDescriptorProvider {
	return concreteComponentDescriptorProvider<NestedScrollViewHeaderComponentDescriptor>();
}

+ (BOOL)shouldBeRecycled {
	return NO;
}

- (instancetype)initWithFrame:(CGRect)frame {
	if (self = [super initWithFrame:frame]) {
		static const auto defaultProps = std::make_shared<const NestedScrollViewHeaderProps>();
		_props = defaultProps;
		_stickyHeaderHeight = -1;
        _stickyHeaderBeginIndex = -1;
    }
    return self;
}


- (void)updateProps:(const facebook::react::Props::Shared &)props oldProps:(const facebook::react::Props::Shared &)oldProps {
	const auto &oldViewProps = static_cast<const NestedScrollViewHeaderProps &>(*_props);
	const auto &newViewProps = static_cast<const NestedScrollViewHeaderProps &>(*props);
	
	// `stickyHeaderHeight`
	if (newViewProps.stickyHeaderHeight != oldViewProps.stickyHeaderHeight) {
		self.stickyHeaderHeight = (CGFloat)newViewProps.stickyHeaderHeight;
	}
	
	// `stickyHeaderBeginIndex`
	if (newViewProps.stickyHeaderBeginIndex != oldViewProps.stickyHeaderBeginIndex) {
		self.stickyHeaderBeginIndex = newViewProps.stickyHeaderBeginIndex;
	}
	
	[super updateProps:props oldProps:oldProps];
}

- (const NestedScrollViewHeaderEventEmitter &)eventEmitter {
	return static_cast<const NestedScrollViewHeaderEventEmitter &>(*_eventEmitter);
}

- (void)setStickyHeaderHeight:(CGFloat)stickyHeaderHeight {
	_stickyHeaderHeight = stickyHeaderHeight;
	[self notityContentSizeChanged];
}

- (void)setStickyHeaderBeginIndex:(NSInteger)stickyHeaderBeginIndex {
	_stickyHeaderBeginIndex = stickyHeaderBeginIndex;
	[self notityContentSizeChanged];
}

- (CGFloat)maxScrollRange {
	if (self.stickyHeaderHeight >= 0) {
		return fmax(self.frame.size.height - self.stickyHeaderHeight, 0);
    }
    
    if (self.stickyHeaderBeginIndex != -1) {
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

- (void)notityContentSizeChanged {
	RNNestedScrollView *scrollView = (RNNestedScrollView *)self.superview.superview;
    if (!scrollView) {
      return;
    }

    RCTAssert([scrollView isKindOfClass:[RNNestedScrollView class]], @"Unexpected view hierarchy of NestedScrollView component.");
    [scrollView updateContentSizeIfNeeded];
}

- (void)layoutSubviews {
	[super layoutSubviews];
	[self notityContentSizeChanged];
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
		RCTSendScrollEventForNativeAnimations_DEPRECATED(self.tag, offset);
		[self eventEmitter].onScroll({
			.contentOffset = {
				.x = static_cast<Float>(offset.x),
				.y = static_cast<Float>(offset.y)
			}
		});
    }
}

@end

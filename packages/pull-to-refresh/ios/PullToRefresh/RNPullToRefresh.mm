#import "RNPullToRefresh.h"
#import "RNRefreshHeader.h"
#import "RNRefreshFooter.h"

#import <react/renderer/components/pulltorefresh/ComponentDescriptors.h>
#import <react/renderer/components/pulltorefresh/EventEmitters.h>
#import <react/renderer/components/pulltorefresh/Props.h>
#import <react/renderer/components/pulltorefresh/RCTComponentViewHelpers.h>

#import <React/RCTConversions.h>
#import <React/RCTLog.h>

using namespace facebook::react;

@interface RNPullToRefresh ()

@property(nonatomic, weak) RNRefreshHeader *header;
@property(nonatomic, weak) RNRefreshFooter *footer;
@property(nonatomic, weak) UIScrollView *scrollView;

@end

@implementation RNPullToRefresh

// Needed because of this: https://github.com/facebook/react-native/pull/37274
+ (void)load {
	[super load];
}

+ (ComponentDescriptorProvider)componentDescriptorProvider {
	return concreteComponentDescriptorProvider<PullToRefreshComponentDescriptor>();
}

+ (BOOL)shouldBeRecycled {
	return NO;
}

- (instancetype)initWithFrame:(CGRect)frame {
	if (self = [super initWithFrame:frame]) {
		static const auto defaultProps = std::make_shared<const PullToRefreshProps>();
		_props = defaultProps;
	}
	return self;
}

- (void)updateProps:(const facebook::react::Props::Shared &)props oldProps:(const facebook::react::Props::Shared &)oldProps {
	[super updateProps:props oldProps:oldProps];
}

- (void)mountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
	if ([childComponentView isKindOfClass:[RNRefreshHeader class]]) {
		self.header = (RNRefreshHeader *)childComponentView;
		[self assembleIfNeeded];
	} else if ([childComponentView isKindOfClass:[RNRefreshFooter class]]) {
		self.footer = (RNRefreshFooter *)childComponentView;
		[self assembleIfNeeded];
	} else {
		RCTAssert(self.subviews.count == 0, @"PullToRefresh may only contain a single subview.");
		[self addSubview:childComponentView];
		self.scrollView = [self findScrollView:childComponentView];
		[self assembleIfNeeded];
	}
}

- (void)unmountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
	[childComponentView removeFromSuperview];
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

- (void)layoutSubviews {
    [super layoutSubviews];
    if (self.scrollView == nil) {
		self.scrollView = [self findScrollView:(UIView *)self];
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

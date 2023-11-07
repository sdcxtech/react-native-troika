#import <React/RCTView.h>
#import <React/RCTEventDispatcher.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollViewHeader : RCTView

@property(nonatomic, assign) CGFloat stickyHeight;
@property(nonatomic, assign) NSUInteger stickyHeaderBeginIndex;
@property(nonatomic, copy) RCTDirectEventBlock onScroll;
@property(nonatomic, strong) RCTEventDispatcher *eventDispatcher;

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher;

- (CGFloat)maxScrollRange;

@end

NS_ASSUME_NONNULL_END

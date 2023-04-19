#import <React/RCTView.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollViewHeader : RCTView

@property(nonatomic, assign) CGFloat stickyHeight;
@property(nonatomic, assign) NSUInteger stickyHeaderBeginIndex;
@property(nonatomic, copy) RCTDirectEventBlock onScroll;

- (CGFloat)maxScrollRange;

@end

NS_ASSUME_NONNULL_END

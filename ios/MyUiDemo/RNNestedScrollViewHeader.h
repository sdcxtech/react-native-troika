#import <React/RCTView.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollViewHeader : RCTView

@property(nonatomic, assign) CGFloat fixedHeight;
@property(nonatomic, assign) NSUInteger stickyHeaderBeginIndex;

- (CGFloat)maxScrollRange;

@end

NS_ASSUME_NONNULL_END

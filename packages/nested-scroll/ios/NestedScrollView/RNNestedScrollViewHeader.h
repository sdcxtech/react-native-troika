#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollViewHeader : RCTViewComponentView

@property(nonatomic, assign) CGFloat stickyHeaderHeight;
@property(nonatomic, assign) NSInteger stickyHeaderBeginIndex;

- (CGFloat)maxScrollRange;

@end

NS_ASSUME_NONNULL_END

#import <React/RCTView.h>
#import <React/RCTBridge.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollView : RCTView

- (instancetype)initWithBridge:(RCTBridge *)bridge;

- (void)updateContentSizeIfNeeded;

@end

NS_ASSUME_NONNULL_END

#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollView : UIView

- (instancetype)initWithBridge:(RCTBridge *)bridge;

- (void)updateContentSizeIfNeeded;

@end

NS_ASSUME_NONNULL_END

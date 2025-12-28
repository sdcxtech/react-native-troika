#import "RNKeyboardInsetsView.h"

NS_ASSUME_NONNULL_BEGIN

@interface RNKeyboardManualHandler : NSObject <RNKeyboardHandler>

- (instancetype)initWithKeyboardInsetsView:(RNKeyboardInsetsView *)view;

@end

NS_ASSUME_NONNULL_END

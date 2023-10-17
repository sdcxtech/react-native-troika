#import <React/RCTViewManager.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNKeyboardStatusChangedEvent : NSObject <RCTEvent>

- (instancetype) initWithReactTag:(NSNumber *)reactTag
                           height:(CGFloat)height
                            shown:(BOOL)shown
                    transitioning:(BOOL)transitioning;

@end

NS_ASSUME_NONNULL_END

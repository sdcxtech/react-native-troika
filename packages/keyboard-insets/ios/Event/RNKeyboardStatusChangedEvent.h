#import <React/RCTEventDispatcherProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNKeyboardStatusChangedEvent : NSObject <RCTEvent>

- (instancetype) initWithReactTag:(NSNumber *)reactTag
                            shown:(BOOL)shown
                    transitioning:(BOOL)transitioning
                           height:(CGFloat)height;

@end

NS_ASSUME_NONNULL_END

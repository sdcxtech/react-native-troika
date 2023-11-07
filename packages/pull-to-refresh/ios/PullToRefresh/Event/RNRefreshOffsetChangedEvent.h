#import <React/RCTEventDispatcherProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNRefreshOffsetChangedEvent : NSObject <RCTEvent>

- (instancetype)initWithViewTag:(NSNumber *)viewTag offset:(CGFloat)offset;

@end

NS_ASSUME_NONNULL_END

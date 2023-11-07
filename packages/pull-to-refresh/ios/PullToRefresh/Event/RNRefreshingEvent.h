#import <React/RCTEventDispatcherProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNRefreshingEvent : NSObject <RCTEvent>

- (instancetype)initWithViewTag:(NSNumber *)viewTag;

@end

NS_ASSUME_NONNULL_END

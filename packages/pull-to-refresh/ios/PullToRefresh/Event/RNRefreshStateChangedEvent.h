#import "RNRefreshState.h"

#import <React/RCTEventDispatcherProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNRefreshStateChangedEvent : NSObject <RCTEvent>

- (instancetype)initWithViewTag:(NSNumber *)viewTag refreshState:(RNRefreshState)refreshState;

@end

NS_ASSUME_NONNULL_END

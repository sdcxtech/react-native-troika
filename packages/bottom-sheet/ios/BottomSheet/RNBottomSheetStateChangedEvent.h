#import <React/RCTEventDispatcherProtocol.h>

#import "RNBottomSheetState.h"

NS_ASSUME_NONNULL_BEGIN

@interface RNBottomSheetStateChangedEvent : NSObject <RCTEvent>

- (instancetype)initWithViewTag:(NSNumber *)viewTag state:(RNBottomSheetState)state;

@end

NS_ASSUME_NONNULL_END

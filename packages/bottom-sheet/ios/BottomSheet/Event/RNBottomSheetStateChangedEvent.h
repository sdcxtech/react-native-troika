#import <React/RCTEventDispatcherProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNBottomSheetStateChangedEvent : NSObject <RCTEvent>

- (instancetype)initWithViewTag:(NSNumber *)viewTag state:(NSString *)state;

@end

NS_ASSUME_NONNULL_END

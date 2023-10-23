#import <React/RCTEventDispatcherProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNBottomSheetOffsetChangedEvent : NSObject <RCTEvent>

- (instancetype)initWithViewTag:(NSNumber *)viewTag progress:(CGFloat)progress offset:(CGFloat)offset minY:(CGFloat)minY maxY:(CGFloat)maxY;

@end

NS_ASSUME_NONNULL_END

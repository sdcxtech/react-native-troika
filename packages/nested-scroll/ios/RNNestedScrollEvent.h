#import <React/RCTViewManager.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollEvent : NSObject <RCTEvent>

- (instancetype) initWithReactTag:(NSNumber *)reactTag
                         offset:(CGPoint)offset;

@end

NS_ASSUME_NONNULL_END

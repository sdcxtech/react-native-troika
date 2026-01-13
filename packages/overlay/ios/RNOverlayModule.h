#import <Foundation/Foundation.h>
#import <overlay/overlay.h>
#import <React/RCTInitializing.h>

NS_ASSUME_NONNULL_BEGIN

@class RCTHost;

@interface RNOverlayModule : NSObject <NativeOverlaySpec, RCTInvalidating>

- (instancetype)initWithHost:(RCTHost *)host;

@end

NS_ASSUME_NONNULL_END

#import <UIKit/UIKit.h>

#import <React/RCTSurfaceDelegate.h>
#import <React/RCTSurfaceProtocol.h>
#import <React/RCTSurfaceSizeMeasureMode.h>
#import <React/RCTSurfaceStage.h>

@class RCTSurface;

NS_ASSUME_NONNULL_BEGIN

@interface RNOverlayHostingView : UIView <RCTSurfaceDelegate>

- (instancetype)initWithSurface:(id<RCTSurfaceProtocol>)surface;

@property (nonatomic, strong, readonly) id<RCTSurfaceProtocol> surface;

@property (nonatomic, assign) BOOL passThroughTouches;

@property (nonatomic, assign) RCTSurfaceSizeMeasureMode sizeMeasureMode;

@end

NS_ASSUME_NONNULL_END

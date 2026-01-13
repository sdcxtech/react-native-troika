#import <UIKit/UIKit.h>

@class RCTHost;

@interface RNOverlay : NSObject

- (instancetype)initWithModuleName:(NSString *)moduleName host:(RCTHost *)rctHost;

- (void)show:(NSDictionary *)props options:(NSDictionary *)options;

- (void)hide;

- (void)update;

@end

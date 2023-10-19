#import "RNKeyboardInsetsModule.h"
#import <React/RCTLog.h>
#import <React/RCTUIManager.h>

@implementation RNKeyboardInsetsModule

RCT_EXPORT_MODULE(KeyboardInsetsModule)

@synthesize bridge;

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(getEdgeInsetsForView:(nonnull NSNumber *)reactTag callback:(RCTResponseSenderBlock)callback) {
    RCTUIManager* uiManager = [self.bridge moduleForClass:[RCTUIManager class]];
    UIView *target = [uiManager viewForReactTag:reactTag];
    CGRect window = target.window.bounds;
    CGRect view = target.bounds;
    CGPoint center = [target.window convertPoint:target.center fromView:target.superview];
    
    callback(@[
        @{
            @"left":   @(MAX(0, window.origin.x - (center.x - view.size.width / 2))),
            @"top":    @(MAX(0, window.origin.y + (center.y - view.size.height / 2))),
            @"right":  @(MAX(0, window.size.width - (center.x + view.size.width / 2))),
            @"bottom": @(MAX(0, window.size.height - (center.y + view.size.height/ 2))),
        }
    ]);
}

@end

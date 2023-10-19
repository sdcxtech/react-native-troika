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
    UIView* view = [uiManager viewForReactTag:reactTag];
    CGRect windowFrame = [view.window convertRect:view.frame fromView:view.superview];
    
    CGAffineTransform t = [self maybeTransform:view];
    
    callback(@[
        @{
            @"left":   @(MAX(0, windowFrame.origin.x + t.tx)),
            @"top":    @(MAX(0, windowFrame.origin.y + t.ty)),
            @"right":  @(MAX(0, CGRectGetMaxX(view.window.bounds) - CGRectGetMaxX(windowFrame) + t.tx)),
            @"bottom": @(MAX(0, CGRectGetMaxY(view.window.bounds) - CGRectGetMaxY(windowFrame) + t.ty)),
        }
    ]);
}


- (CGAffineTransform)maybeTransform:(UIView *)view {
    if (view.transform.ty != 0) {
        return view.transform;
    }
    
    if (!view.superview) {
        return CGAffineTransformIdentity;
    }
    
    return [self maybeTransform:view.superview];
}


@end

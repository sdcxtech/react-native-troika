#import "RNKeyboardStatusChangedEvent.h"

#import <React/UIView+React.h>

static uint16_t _coalescingKey = 0;

@implementation RNKeyboardStatusChangedEvent {
    CGFloat _height;
    BOOL _shown;
    BOOL _transitioning;
}

@synthesize viewTag = _viewTag;


- (NSString *)eventName {
    return @"onStatusChanged";
}

- (instancetype)initWithReactTag:(NSNumber *)reactTag shown:(BOOL)shown transitioning:(BOOL)transitioning height:(CGFloat)height {
    RCTAssertParam(reactTag);
    if ((self = [super init])) {
        _viewTag = reactTag;
        _shown = shown;
        _transitioning = transitioning;
        _height = height;
    }
    return self;
}

RCT_NOT_IMPLEMENTED(- (instancetype)init)
- (uint16_t)coalescingKey {
    return _coalescingKey++;
}

- (BOOL)canCoalesce {
    return NO;
}

+ (NSString *)moduleDotMethod {
    return @"RCTEventEmitter.receiveEvent";
}

- (NSArray *)arguments {
    return @[self.viewTag, RCTNormalizeInputEventName(self.eventName), @{
                @"shown": @(_shown),
                @"transitioning": @(_transitioning),
                @"height": @(_height)
            }];
}

- (id<RCTEvent>)coalesceWithEvent:(id<RCTEvent>)newEvent {
    return newEvent;
}

@end


#import "RNKeyboardPositionChangedEvent.h"

#import <React/UIView+React.h>

@implementation RNKeyboardPositionChangedEvent {
    NSNumber* _position;
}

@synthesize viewTag = _viewTag;

- (NSString *)eventName {
    return @"onPositionChanged";
}

- (instancetype) initWithReactTag:(NSNumber *)reactTag
                         position:(NSNumber *)position {
    RCTAssertParam(reactTag);
    if ((self = [super init])) {
        _viewTag = reactTag;
        _position = position;
    }
    return self;
}

RCT_NOT_IMPLEMENTED(- (instancetype)init)
- (uint16_t)coalescingKey {
    return 0;
}

- (BOOL)canCoalesce {
    return YES;
}

+ (NSString *)moduleDotMethod {
    return @"RCTEventEmitter.receiveEvent";
}

- (NSArray *)arguments {
    return @[self.viewTag, RCTNormalizeInputEventName(self.eventName), @{
                 @"position": _position,
           }];
}

- (id<RCTEvent>)coalesceWithEvent:(id<RCTEvent>)newEvent {
    return newEvent;
}

@end

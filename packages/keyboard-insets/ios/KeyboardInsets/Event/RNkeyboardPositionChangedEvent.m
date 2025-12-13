#import "RNKeyboardPositionChangedEvent.h"

@implementation RNKeyboardPositionChangedEvent {
    NSNumber* _position;
}

@synthesize viewTag = _viewTag;

+ (NSString *)moduleDotMethod {
    return @"RCTEventEmitter.receiveEvent";
}

RCT_NOT_IMPLEMENTED(- (instancetype)init)
- (instancetype) initWithReactTag:(NSNumber *)reactTag
                         position:(NSNumber *)position {
    if ((self = [super init])) {
        _viewTag = reactTag;
        _position = position;
    }
    return self;
}

- (NSString *)eventName {
    return @"onPositionChanged";
}

- (BOOL)canCoalesce {
    return YES;
}

- (uint16_t)coalescingKey {
    return 0;
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

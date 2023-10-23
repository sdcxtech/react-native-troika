#import "RNBottomSheetStateChangedEvent.h"

static uint16_t _coalescingKey = 0;

@interface RNBottomSheetStateChangedEvent ()

@property (nonatomic, assign) RNBottomSheetState state;

@end

@implementation RNBottomSheetStateChangedEvent

@synthesize viewTag = _viewTag;

+ (NSString *)moduleDotMethod {
    return @"RCTEventEmitter.receiveEvent";
}

RCT_NOT_IMPLEMENTED(- (instancetype)init)
- (instancetype)initWithViewTag:(NSNumber *)viewTag state:(RNBottomSheetState)state {
    if (self = [super init]) {
        _viewTag = viewTag;
        _state = state;
    }
    return self;
}

- (NSString *)eventName {
    return @"onStateChanged";
}

- (BOOL)canCoalesce {
    return NO;
}

- (uint16_t)coalescingKey {
    return _coalescingKey++;
}

- (NSArray *)arguments {
    return @[self.viewTag, RCTNormalizeInputEventName(self.eventName), @{
                @"state": RNBottomSheetStateToString(self.state)
            }];
}

- (id<RCTEvent>)coalesceWithEvent:(id<RCTEvent>)newEvent {
    return newEvent;
}

@end

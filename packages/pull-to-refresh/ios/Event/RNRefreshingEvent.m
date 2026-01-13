#import "RNRefreshingEvent.h"

static uint16_t _coalescingKey = 0;

@implementation RNRefreshingEvent

@synthesize viewTag = _viewTag;

+ (NSString *)moduleDotMethod {
    return @"RCTEventEmitter.receiveEvent";
}

RCT_NOT_IMPLEMENTED(- (instancetype)init)
- (instancetype)initWithViewTag:(NSNumber *)viewTag {
    if (self = [super init]) {
        _viewTag = viewTag;
    }
    return self;
}

- (NSString *)eventName {
    return @"onRefresh";
}

- (BOOL)canCoalesce {
    return NO;
}

- (uint16_t)coalescingKey {
    return _coalescingKey++;
}

- (NSArray *)arguments {
    return @[self.viewTag, RCTNormalizeInputEventName(self.eventName), @{}];
}

- (id<RCTEvent>)coalesceWithEvent:(id<RCTEvent>)newEvent {
    return newEvent;
}

@end

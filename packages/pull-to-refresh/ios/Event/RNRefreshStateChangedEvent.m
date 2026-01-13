#import "RNRefreshStateChangedEvent.h"

static uint16_t _coalescingKey = 0;

@interface RNRefreshStateChangedEvent ()

@property (nonatomic, assign) RNRefreshState refreshState;

@end

@implementation RNRefreshStateChangedEvent

@synthesize viewTag = _viewTag;

+ (NSString *)moduleDotMethod {
    return @"RCTEventEmitter.receiveEvent";
}

RCT_NOT_IMPLEMENTED(- (instancetype)init)

- (instancetype)initWithViewTag:(NSNumber *)viewTag refreshState:(RNRefreshState)refreshState {
    if (self = [super init]) {
        _viewTag = viewTag;
        _refreshState = refreshState;
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
                @"state": @(self.refreshState)
            }];
}

- (id<RCTEvent>)coalesceWithEvent:(id<RCTEvent>)newEvent {
    return newEvent;
}

@end

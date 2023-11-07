#import "RNRefreshOffsetChangedEvent.h"


@interface RNRefreshOffsetChangedEvent ()

@property(nonatomic, assign) CGFloat offset;

@end

@implementation RNRefreshOffsetChangedEvent

@synthesize viewTag = _viewTag;

+ (NSString *)moduleDotMethod {
    return @"RCTEventEmitter.receiveEvent";
}

RCT_NOT_IMPLEMENTED(- (instancetype)init)
- (instancetype)initWithViewTag:(NSNumber *)viewTag offset:(CGFloat)offset {
    if (self = [super init]) {
        _viewTag = viewTag;
        _offset = offset;
    }
    return self;
}

- (NSString *)eventName {
    return @"onOffsetChanged";
}

- (BOOL)canCoalesce {
    return YES;
}

- (uint16_t)coalescingKey {
    return 0;
}

- (NSArray *)arguments {
    return @[self.viewTag, RCTNormalizeInputEventName(self.eventName), @{
        @"offset": @(self.offset),
    }];
}

- (id<RCTEvent>)coalesceWithEvent:(id<RCTEvent>)newEvent {
    return newEvent;
}

@end

#import "RNNestedScrollEvent.h"

#import <React/UIView+React.h>

@implementation RNNestedScrollEvent {
    CGPoint _offset;
}

@synthesize viewTag = _viewTag;

- (NSString *)eventName {
    return @"onScroll";
}

- (instancetype) initWithReactTag:(NSNumber *)reactTag
                         offset:(CGPoint)offset {
    RCTAssertParam(reactTag);
    if ((self = [super init])) {
        _viewTag = reactTag;
        _offset = offset;
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
        @"contentOffset": @{
            @"y": @(_offset.y),
            @"x": @(_offset.x)
        }
    }];
}

- (id<RCTEvent>)coalesceWithEvent:(id<RCTEvent>)newEvent {
    return newEvent;
}

@end

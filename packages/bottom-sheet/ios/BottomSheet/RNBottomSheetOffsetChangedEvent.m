#import "RNBottomSheetOffsetChangedEvent.h"

@interface RNBottomSheetOffsetChangedEvent ()

@property (nonatomic, assign) CGFloat progress;
@property (nonatomic, assign) CGFloat offest;
@property (nonatomic, assign) CGFloat minY;
@property (nonatomic, assign) CGFloat maxY;

@end

@implementation RNBottomSheetOffsetChangedEvent

@synthesize viewTag = _viewTag;

+ (NSString *)moduleDotMethod {
    return @"RCTEventEmitter.receiveEvent";
}

RCT_NOT_IMPLEMENTED(- (instancetype)init)
- (instancetype)initWithViewTag:(NSNumber *)viewTag progress:(CGFloat)progress offset:(CGFloat)offset minY:(CGFloat)minY maxY:(CGFloat)maxY {
    if (self = [super init]) {
        _viewTag = viewTag;
        _progress = progress;
        _offest = offset;
        _minY = minY;
        _maxY = maxY;
    }
    return self;
}

- (NSString *)eventName {
    return @"onSlide";
}

- (BOOL)canCoalesce {
    return YES;
}

- (uint16_t)coalescingKey {
    return 0;
}

- (NSArray *)arguments {
    return @[self.viewTag, RCTNormalizeInputEventName(self.eventName), @{
                @"progress": @(self.progress),
                @"offset": @(self.offest),
                @"expandedOffset": @(self.minY),
                @"collapsedOffset": @(self.maxY)
            }];
}

- (id<RCTEvent>)coalesceWithEvent:(id<RCTEvent>)newEvent {
    return newEvent;
}


@end

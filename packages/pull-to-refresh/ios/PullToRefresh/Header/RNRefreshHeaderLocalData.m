#import "RNRefreshHeaderLocalData.h"

@implementation RNRefreshHeaderLocalData

- (instancetype)initWithHeaderHeight:(CGFloat)height {
    if (self = [super init]) {
        _height = height;
    }
    return self;
}

@end

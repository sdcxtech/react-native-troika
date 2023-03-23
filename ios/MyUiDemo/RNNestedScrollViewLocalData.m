#import "RNNestedScrollViewLocalData.h"

@implementation RNNestedScrollViewLocalData

- (instancetype)initWithSize:(CGSize)size {
    if (self = [super init]) {
        _scrollingChildSize = size;
    }
    return self;
}

@end

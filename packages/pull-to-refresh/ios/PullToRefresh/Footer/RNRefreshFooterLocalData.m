#import "RNRefreshFooterLocalData.h"

@implementation RNRefreshFooterLocalData

- (instancetype)initWithScrollViewContentSize:(CGSize)size {
    if (self = [super init]) {
        _scrollViewContentSize = size;
    }
    return self;
}

@end

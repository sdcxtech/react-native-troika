#import "RNPullToRefreshManager.h"
#import "RNPullToRefresh.h"

@implementation RNPullToRefreshManager

RCT_EXPORT_MODULE(PullToRefresh)

- (UIView *)view {
    return [RNPullToRefresh new];
}

@end

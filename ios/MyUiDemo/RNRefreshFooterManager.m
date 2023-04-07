#import "RNRefreshFooterManager.h"
#import "RNRefreshFooter.h"
#import "RNRefreshShadowFooter.h"

#import <React/RCTRefreshableProtocol.h>
#import <React/RCTUIManager.h>

@implementation RNRefreshFooterManager

RCT_EXPORT_MODULE(RefreshFooter)

- (UIView *)view {
    return [[RNRefreshFooter alloc] initWithBridge:self.bridge];
}

- (RCTShadowView *)shadowView {
    return [RNRefreshShadowFooter new];
}

RCT_EXPORT_VIEW_PROPERTY(onRefresh, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onStateChanged, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onOffsetChanged, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(refreshing, BOOL)
RCT_EXPORT_VIEW_PROPERTY(noMoreData, BOOL)
RCT_EXPORT_VIEW_PROPERTY(manual, BOOL)

RCT_EXPORT_METHOD(setNativeRefreshing : (nonnull NSNumber *)viewTag toRefreshing : (BOOL)refreshing) {
  [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry) {
    UIView *view = viewRegistry[viewTag];

    if ([view conformsToProtocol:@protocol(RCTRefreshableProtocol)]) {
        [(id<RCTRefreshableProtocol>)view setRefreshing:refreshing];
    } else {
        RCTLogError(@"view must conform to protocol RCTRefreshableProtocol");
    }
  }];
}

@end

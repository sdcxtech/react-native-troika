#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>

#import <React/RCTScrollableProtocol.h>
#import <React/RCTBridge.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNRefreshHeader : RCTViewComponentView <RCTCustomRefreshControlProtocol>

@property (nonatomic, copy) RCTDirectEventBlock onRefresh;
@property (nonatomic, copy) RCTDirectEventBlock onStateChanged;
@property (nonatomic, copy) RCTDirectEventBlock onOffsetChanged;
@property (nonatomic, readonly, getter=isRefreshing) BOOL refreshing;

@property (nonatomic, weak) UIScrollView *scrollView;

@end

NS_ASSUME_NONNULL_END

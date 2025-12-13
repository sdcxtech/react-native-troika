#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>

#import <React/RCTCustomPullToRefreshViewProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNRefreshHeader : RCTViewComponentView <RCTCustomPullToRefreshViewProtocol>

@property (nonatomic, readonly, getter=isRefreshing) BOOL refreshing;

@property (nonatomic, weak) UIScrollView *scrollView;

@end

NS_ASSUME_NONNULL_END

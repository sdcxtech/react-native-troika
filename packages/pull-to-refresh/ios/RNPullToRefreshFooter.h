#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>

#import <React/RCTCustomPullToRefreshViewProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNPullToRefreshFooter : RCTViewComponentView <RCTCustomPullToRefreshViewProtocol>

@property (nonatomic, readonly, getter=isRefreshing) BOOL refreshing;

@property (nonatomic, assign) BOOL noMoreData;
// 手动触发加载更多，设置为 NO，将自动触发加载更多，默认为 NO
@property (nonatomic, assign) BOOL manual;

@property (nonatomic, weak) UIScrollView *scrollView;

@end

NS_ASSUME_NONNULL_END

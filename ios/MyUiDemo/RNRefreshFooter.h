#import <UIKit/UIKit.h>
#import <React/RCTScrollableProtocol.h>
#import <React/RCTBridge.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNRefreshFooter : UIView <RCTCustomRefreshContolProtocol>

- (instancetype)initWithBridge:(RCTBridge *)bridge;

@property (nonatomic, copy) RCTDirectEventBlock onRefresh;
@property (nonatomic, copy) RCTDirectEventBlock onStateChanged;
@property (nonatomic, readonly, getter=isRefreshing) BOOL refreshing;

@property (nonatomic, assign) BOOL noMoreData;
// 手动触发加载更多，设置为 NO，将自动触发加载更多，默认为 NO
@property (nonatomic, assign) BOOL manual;

@property (nonatomic, weak) UIScrollView *scrollView;

@end

NS_ASSUME_NONNULL_END

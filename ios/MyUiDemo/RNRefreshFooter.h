#import <UIKit/UIKit.h>
#import <React/RCTScrollableProtocol.h>
#import <React/RCTBridge.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNRefreshFooter : UIView <RCTCustomRefreshContolProtocol>

- (instancetype)initWithBridge:(RCTBridge *)bridge;

@property (nonatomic, copy) RCTDirectEventBlock onRefresh;
@property (nonatomic, copy) RCTDirectEventBlock onStateChanged;
@property (nonatomic, readonly, getter=isRefreshing) BOOL refreshing;

// 是否响应上拉加载更多，当没有更多数据时，设置为 NO，默认为 YES
@property (nonatomic, assign) BOOL enabled;
// 手动触发加载更多，设置为 NO，将自动触发加载更多，默认为 NO
@property (nonatomic, assign) BOOL manual;

@property (nonatomic, weak) UIScrollView *scrollView;

@end

NS_ASSUME_NONNULL_END

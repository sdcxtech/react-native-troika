#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, RNRefreshState) {
    /** 普通闲置状态 */
    RNRefreshStateIdle,
    /** 松开就可以进行刷新的状态 */
    RNRefreshStateComing,
    /** 正在刷新中的状态 */
    RNRefreshStateRefreshing,
};

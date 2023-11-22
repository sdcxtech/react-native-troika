#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNRefreshHeaderLocalData : NSObject

- (instancetype)initWithHeaderHeight:(CGFloat)height;

@property(nonatomic, assign)CGFloat height;

@end

NS_ASSUME_NONNULL_END

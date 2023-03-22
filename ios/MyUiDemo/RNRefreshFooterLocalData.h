#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNRefreshFooterLocalData : NSObject

- (instancetype)initWithScrollViewContentSize:(CGSize)size;

@property(nonatomic, assign)CGSize scrollViewContentSize;

@end

NS_ASSUME_NONNULL_END

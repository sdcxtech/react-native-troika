#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollViewLocalData : NSObject

- (instancetype)initWithSize:(CGSize)size;

@property(nonatomic, assign) CGSize scrollingChildSize;

@end

NS_ASSUME_NONNULL_END

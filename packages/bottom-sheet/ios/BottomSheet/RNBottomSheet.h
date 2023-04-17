#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNBottomSheet : UIView

@property(nonatomic, copy) RCTDirectEventBlock onSlide;
@property(nonatomic, copy) RCTDirectEventBlock onStateChanged;
@property(nonatomic, assign) CGFloat peekHeight;
@property(nonatomic, copy) NSString *state;

@end

NS_ASSUME_NONNULL_END

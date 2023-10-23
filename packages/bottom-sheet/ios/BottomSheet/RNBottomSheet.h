#import <UIKit/UIKit.h>
#import <React/RCTView.h>

#import "RNBottomSheetState.h"

NS_ASSUME_NONNULL_BEGIN

@interface RNBottomSheet : RCTView

@property(nonatomic, copy) RCTDirectEventBlock onSlide;
@property(nonatomic, copy) RCTDirectEventBlock onStateChanged;
@property(nonatomic, assign) CGFloat peekHeight;
@property(nonatomic, assign) RNBottomSheetState state;

@end

NS_ASSUME_NONNULL_END

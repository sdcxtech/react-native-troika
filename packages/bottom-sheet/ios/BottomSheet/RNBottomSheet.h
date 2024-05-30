#import <UIKit/UIKit.h>
#import <React/RCTView.h>
#import <React/RCTEventDispatcher.h>

#import "RNBottomSheetState.h"

NS_ASSUME_NONNULL_BEGIN

@interface RNBottomSheet : RCTView

@property(nonatomic, copy) RCTDirectEventBlock onSlide;
@property(nonatomic, copy) RCTDirectEventBlock onStateChanged;
@property(nonatomic, assign) CGFloat peekHeight;
@property(nonatomic, assign) RNBottomSheetState state;
@property(nonatomic, assign) BOOL draggable;

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher;

@end

NS_ASSUME_NONNULL_END

#import <React/RCTView.h>
#import <React/RCTEventDispatcher.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNKeyboardInsetsView : RCTView

@property(nonatomic, copy) RCTDirectEventBlock onStatusChanged;
@property(nonatomic, copy) RCTDirectEventBlock onPositionChanged;

@property(nonatomic, copy) NSString *mode;
@property(nonatomic, assign) CGFloat extraHeight;
@property(nonatomic, assign) BOOL explicitly;
@property(nonatomic, strong) RCTEventDispatcher *eventDispatcher;

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher;

@end

@protocol RNKeyboardHandler <NSObject>

- (void)keyboardWillShow:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight;

- (void)keyboardDidShow:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight;

- (void)keyboardWillHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight;

- (void)keyboardDidHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight;

- (void)handleKeyboardTransition:(CGFloat)position;

@end

NS_ASSUME_NONNULL_END

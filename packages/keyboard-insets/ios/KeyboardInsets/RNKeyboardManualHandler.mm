#import "RNKeyboardManualHandler.h"

#import <React/RCTLog.h>

@interface RNKeyboardManualHandler ()

@property (nonatomic, weak) RNKeyboardInsetsViewComponentView *view;

@end

@implementation RNKeyboardManualHandler

- (instancetype)initWithKeyboardInsetsView:(RNKeyboardInsetsViewComponentView *)view {
    if (self = [super init]) {
        _view = view;
    }
    return self;
}

- (void)keyboardWillShow:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self handleKeyboardShown:YES transitioning:YES height:keyboardHeight];
}

- (void)keyboardDidShow:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self handleKeyboardTransition:keyboardHeight];
    [self handleKeyboardShown:YES transitioning:NO height:keyboardHeight];
}

- (void)keyboardWillHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self handleKeyboardShown:NO transitioning:YES height:keyboardHeight];
}

- (void)keyboardDidHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self handleKeyboardTransition:0];
    [self handleKeyboardShown:NO transitioning:NO height:keyboardHeight];
}

- (void)handleKeyboardShown:(BOOL)shown transitioning:(BOOL)transitioning height:(CGFloat)height {
	[self.view dispatchKeyboardStatus:{
		height, shown, transitioning
	}];
}

- (void)handleKeyboardTransition:(CGFloat)position {
    RCTLogInfo(@"[KeyboardInsetsView] keyboard position: %f", position);
	[self.view dispatchKeyboardPosition:position];
}

@end

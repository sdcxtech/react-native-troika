#import "RNKeyboardManualHandler.h"
#import "RNKeyboardPositionChangedEvent.h"
#import "RNKeyboardStatusChangedEvent.h"

#import <React/RCTLog.h>
#import <React/RCTUIManager.h>
#import <React/RCTScrollView.h>

@interface RNKeyboardManualHandler ()

@property (nonatomic, weak) RNKeyboardInsetsView *view;

@end

@implementation RNKeyboardManualHandler

- (instancetype)initWithKeyboardInsetsView:(RNKeyboardInsetsView *)view {
    if (self = [super init]) {
        _view = view;
    }
    return self;
}

- (void)keyboardWillShow:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self handleKeyboardStatus:YES transitioning:YES height:keyboardHeight];
}

- (void)keyboardDidShow:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self handleKeyboardTransition:keyboardHeight];
    [self handleKeyboardStatus:YES transitioning:NO height:keyboardHeight];
}

- (void)keyboardWillHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self handleKeyboardStatus:NO transitioning:YES height:keyboardHeight];
}

- (void)keyboardDidHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self handleKeyboardTransition:0];
    [self handleKeyboardStatus:NO transitioning:NO height:keyboardHeight];
}

- (void)handleKeyboardStatus:(BOOL)shown transitioning:(BOOL)transitioning height:(CGFloat)height {
    RNKeyboardInsetsView *view = self.view;
    [view.eventDispatcher sendEvent:[[RNKeyboardStatusChangedEvent alloc] initWithReactTag:view.reactTag height:height shown:shown transitioning:transitioning]];
}

- (void)handleKeyboardTransition:(CGFloat)position {
    RCTLogInfo(@"[KeyboardInsetsView] keyboard position: %f", position);
    RNKeyboardInsetsView *view = self.view;
    [view.eventDispatcher sendEvent:[[RNKeyboardPositionChangedEvent alloc] initWithReactTag:view.reactTag position:@(position)]];
}

@end

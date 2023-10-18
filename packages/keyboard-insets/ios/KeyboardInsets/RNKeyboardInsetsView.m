#import "RNKeyboardInsetsView.h"
#import "RNKeyboardAutoHandler.h"
#import "RNKeyboardManualHandler.h"

#import <React/RCTLog.h>
#import <React/RCTUIManager.h>
#import <React/RCTScrollView.h>

@implementation RNKeyboardInsetsView {
    UIView *_focusView;
 
    CADisplayLink *_displayLink;
    UIView *_keyboardView;
    CGFloat _keyboardHeight;
    
    RNKeyboardAutoHandler *_autoHandler;
    RNKeyboardManualHandler *_manualHandler;
}

- (instancetype)init {
    if (self = [super init]) {
        _mode = @"auto";
    }
    return self;
}

- (RNKeyboardAutoHandler *)autoHandler {
    if (!_autoHandler) {
        _autoHandler = [[RNKeyboardAutoHandler alloc] initWithKeyboardInsetsView:self];
    }
    return _autoHandler;
}

- (RNKeyboardManualHandler *)manualHandler {
    if (!_manualHandler) {
        _manualHandler = [[RNKeyboardManualHandler alloc] initWithKeyboardInsetsView:self];
    }
    return _manualHandler;
}

- (void)willMoveToWindow:(UIWindow *)newWindow {
    if (!newWindow) {
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardWillHideNotification object:nil];
        [[NSNotificationCenter defaultCenter] removeObserver:self name:UIKeyboardDidHideNotification object:nil];
        [self stopWatchKeyboardTransition];
    }
}

- (void)didMoveToWindow {
    if (self.window) {
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                     selector:@selector(keyboardWillShow:)
                                                         name:UIKeyboardWillShowNotification
                                                       object:nil];
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                     selector:@selector(keyboardDidShow:)
                                                         name:UIKeyboardDidShowNotification
                                                       object:nil];
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                     selector:@selector(keyboardWillHide:)
                                                         name:UIKeyboardWillHideNotification
                                                       object:nil];
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                     selector:@selector(keyboardDidHide:)
                                                         name:UIKeyboardDidHideNotification
                                                       object:nil];
    }
}

- (void)keyboardWillShow:(NSNotification *)notification {
    UIView *focusView = [RNKeyboardInsetsView findFocusView:self];
    
    if (![self shouldHandleKeyboardTransition:focusView]) {
        return;
    }
    
    _focusView = focusView;
    _keyboardView = [RNKeyboardInsetsView findKeyboardView];
    
    NSDictionary *userInfo = [notification userInfo];
    CGRect keyboardRect = [[userInfo objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue];
    CGFloat keyboardHeight = keyboardRect.size.height;
    
    _keyboardHeight = keyboardHeight;
    
    if ([self isAutoMode]) {
        [[self autoHandler] keyboardWillShow:focusView keyboardHeight:keyboardHeight];
    } else {
        [[self manualHandler] keyboardWillShow:focusView keyboardHeight:keyboardHeight];
    }

    RCTLogInfo(@"[KeyboardInsetsView] keyboardWillShow startWatchKeyboardTransition");
    [self startWatchKeyboardTransition];
}

- (void)keyboardDidShow:(NSNotification *)notification {
    if (![self shouldHandleKeyboardTransition:_focusView]) {
        return;
    }
    
    RCTLogInfo(@"[KeyboardInsetsView] keyboardDidShow stopWatchKeyboardTransition");
    [self stopWatchKeyboardTransition];

    if ([self isAutoMode]) {
        UIView *focusView = [RNKeyboardInsetsView findFocusView:self];
        if (focusView && focusView != _focusView) {
            RNKeyboardInsetsView *insetsView = [RNKeyboardInsetsView findClosetKeyboardInsetsView:focusView];
            if (insetsView != self) {
                focusView = nil;
            }
        }
        _focusView = focusView;
        [[self autoHandler] keyboardDidShow:focusView keyboardHeight:_keyboardHeight];
    } else {
        [[self manualHandler] keyboardDidShow:_focusView keyboardHeight:_keyboardHeight];
    }
}

- (void)keyboardWillHide:(NSNotification *)notification {
    if (![self shouldHandleKeyboardTransition:_focusView]) {
        return;
    }
    
    _keyboardView = [RNKeyboardInsetsView findKeyboardView];
    
    if ([self isAutoMode]) {
        [[self autoHandler] keyboardWillHide:_focusView keyboardHeight:_keyboardHeight];
    } else {
        [[self manualHandler] keyboardWillHide:_focusView keyboardHeight:_keyboardHeight];
    }
    
    RCTLogInfo(@"[KeyboardInsetsView] keyboardWillHide startWatchKeyboardTransition");
    [self startWatchKeyboardTransition];
}


- (void)keyboardDidHide:(NSNotification *)notification {
    UIView *focusView = _focusView;
    _focusView = nil;
    
    if (![self shouldHandleKeyboardTransition:focusView]) {
        return;
    }
    
    RCTLogInfo(@"[KeyboardInsetsView] keyboardDidHide stopWatchKeyboardTransition");
    [self stopWatchKeyboardTransition];
    
    if ([self isAutoMode]) {
        [[self autoHandler] keyboardDidHide:focusView keyboardHeight:_keyboardHeight];
    } else {
        [[self manualHandler] keyboardDidHide:focusView keyboardHeight:_keyboardHeight];
    }
}

- (BOOL)shouldHandleKeyboardTransition:(UIView *)focusView {
    if (focusView) {
        RNKeyboardInsetsView *closet = [RNKeyboardInsetsView findClosetKeyboardInsetsView:focusView];
            return closet == self;
    }
    return NO;
}


- (void)startWatchKeyboardTransition {
    [self stopWatchKeyboardTransition];
    _displayLink = [CADisplayLink displayLinkWithTarget:self selector:@selector(watchKeyboardTransition)];
    _displayLink.preferredFramesPerSecond = 120;
    [_displayLink addToRunLoop:[NSRunLoop mainRunLoop] forMode:NSRunLoopCommonModes];
}

- (void)stopWatchKeyboardTransition {
    if(_displayLink){
        [_displayLink invalidate];
        _displayLink = nil;
    }
}

- (void)watchKeyboardTransition {
    if (_keyboardView == nil) {
        return;
    }
    
    CGFloat keyboardFrameY = [_keyboardView.layer presentationLayer].frame.origin.y;
    CGFloat keyboardWindowH = _keyboardView.window.bounds.size.height;
    [self handleKeyboardTransition:(keyboardWindowH - keyboardFrameY)];
}

- (void)handleKeyboardTransition:(CGFloat)position {
    if ([self isAutoMode]) {
        if (_focusView) {
            [[self autoHandler] handleKeyboardTransition:position];
        }
    } else {
        [[self manualHandler] handleKeyboardTransition:position];
    }
}

- (BOOL)isAutoMode {
    return [self.mode isEqualToString:@"auto"];
}

+ (UIView *)findKeyboardView {
    NSArray<UIWindow *> *windows = UIApplication.sharedApplication.windows;
    for (UIWindow *window in windows) {
        if ([window.description hasPrefix:@"<UITextEffectsWindow"]) {
            for (UIView *subview in window.subviews) {
                if ([subview.description hasPrefix:@"<UIInputSetContainerView"]) {
                    for (UIView *hostView in subview.subviews) {
                        if ([hostView.description hasPrefix:@"<UIInputSetHostView"]) {
                            return hostView;
                        }
                    }
                    break;
                }
            }
            break;
        }
    }
    return nil;
}

+ (UIView *)findFocusView:(UIView *)view {
    if ([view isFirstResponder]) {
        return view;
    }
    
    for (UIView *child in view.subviews) {
        UIView *focus = [self findFocusView:child];
        if (focus) {
            return focus;
        }
    }
    
    return nil;
}

+ (RNKeyboardInsetsView *)findClosetKeyboardInsetsView:(UIView *)view {
    if ([view isKindOfClass:[RNKeyboardInsetsView class]]) {
        return (RNKeyboardInsetsView *)view;
    }
    
    if (view.superview) {
        return [self findClosetKeyboardInsetsView:view.superview];
    }
    
    return nil;
}

@end

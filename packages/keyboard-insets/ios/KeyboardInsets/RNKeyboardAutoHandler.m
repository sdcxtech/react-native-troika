#import "RNKeyboardAutoHandler.h"

#import <React/RCTLog.h>
#import <React/RCTUIManager.h>
#import <React/RCTScrollView.h>

@interface RNKeyboardAutoHandler ()

@property (nonatomic, weak) RNKeyboardInsetsView *view;
@property (nonatomic, assign) CGFloat keyboardHeight;
@property (nonatomic, assign) BOOL forceUpdated;
@property (nonatomic, assign) CGFloat edgeBottom;
@property (nonatomic, assign) BOOL shown;

@end

@implementation RNKeyboardAutoHandler

- (instancetype)initWithKeyboardInsetsView:(RNKeyboardInsetsView *)view {
    if (self = [super init]) {
        _view = view;
    }
    return self;
}

- (void)keyboardWillShow:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    self.shown = YES;
    if (self.keyboardHeight != keyboardHeight) {
        self.forceUpdated = YES;
    }
    self.keyboardHeight = keyboardHeight;
    
    [self adjustScrollViewOffsetIfNeeded:focusView];
    [self refreshEdgeBottom:focusView];
}

- (void)keyboardDidShow:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    if (focusView) {
        [self handleKeyboardTransition:keyboardHeight];
    } else {
        self.forceUpdated = YES;
        [self handleKeyboardTransition:0];
    }
}

- (void)keyboardWillHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    self.shown = NO;
}

- (void)keyboardDidHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self handleKeyboardTransition:0];
    self.edgeBottom = 0;
}

- (void)handleKeyboardTransition:(CGFloat)position {
    RNKeyboardInsetsView *view = self.view;
    
    CGFloat translationY = 0;
    if (position > 0) {
        CGFloat edgeBottom = MAX(self.edgeBottom - view.extraHeight, 0);
        translationY = -MAX(position - edgeBottom, 0);
    }
    
    if (self.forceUpdated) {
        self.forceUpdated = NO;
        view.transform = CGAffineTransformMakeTranslation(0, translationY);
    }
    
    if (self.shown && view.transform.ty < translationY) {
        return;
    }
    
    view.transform = CGAffineTransformMakeTranslation(0, translationY);
}

- (void)refreshEdgeBottom:(UIView *)focusView {
    UIWindow *window = focusView.window;
    CGPoint center = [window convertPoint:focusView.center fromView:focusView.superview];
    self.edgeBottom = window.bounds.size.height - (center.y + focusView.bounds.size.height / 2);
}

- (void)adjustScrollViewOffsetIfNeeded:(UIView *)focusView {
    RCTScrollView *rct = [RNKeyboardAutoHandler findClosetScrollView:focusView];
    if (rct) {
        UIScrollView *scrollView = rct.scrollView;
        CGRect frame = [rct.contentView convertRect:focusView.frame fromView:focusView.superview];
        CGFloat dy =  CGRectGetHeight(rct.frame) + scrollView.contentOffset.y -  CGRectGetMaxY(frame) - self.view.extraHeight;
        if (dy < 0) {
            CGFloat range = scrollView.contentSize.height - scrollView.frame.size.height;
            CGPoint offset = scrollView.contentOffset;
            offset.y = MIN(range, offset.y - dy);
            [rct scrollToOffset:offset animated:NO];
        }
    }
}

+ (RCTScrollView *)findClosetScrollView:(UIView *)view {
    if ([view isKindOfClass:[RCTScrollView class]]) {
        return (RCTScrollView *)view;
    }
    
    if (view.superview) {
        return [self findClosetScrollView:view.superview];
    }
    
    return nil;
}

@end

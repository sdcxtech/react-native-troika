#import "RNKeyboardAutoHandler.h"

#import <React/RCTLog.h>
#import <React/RCTScrollViewComponentView.h>

@interface RNKeyboardAutoHandler ()

@property (nonatomic, weak) RNKeyboardInsetsViewComponentView *view;
@property (nonatomic, assign) CGFloat keyboardHeight;
@property (nonatomic, assign) BOOL forceUpdated;
@property (nonatomic, assign) CGFloat edgeBottom;
@property (nonatomic, assign) BOOL shown;

@end

@implementation RNKeyboardAutoHandler

- (instancetype)initWithKeyboardInsetsView:(RNKeyboardInsetsViewComponentView *)view {
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
    if (!self.view.explicitly) {
        [self doKeyboardTransition:keyboardHeight];
    }
}

- (void)keyboardDidShow:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    if (focusView) {
        [self doKeyboardTransition:keyboardHeight];
    } else {
        self.forceUpdated = YES;
        [self doKeyboardTransition:0];
    }
}

- (void)keyboardWillHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    self.shown = NO;
    if (!self.view.explicitly) {
        [self doKeyboardTransition:0];
    }
}

- (void)keyboardDidHide:(UIView *)focusView keyboardHeight:(CGFloat)keyboardHeight {
    [self doKeyboardTransition:0];
    self.edgeBottom = 0;
}

- (void)doKeyboardTransition:(CGFloat)position {
    RNKeyboardInsetsViewComponentView *view = self.view;

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

- (void)handleKeyboardTransition:(CGFloat)position {
    if (self.view.explicitly) {
        [self doKeyboardTransition:position];
    }
}

- (void)refreshEdgeBottom:(UIView *)focusView {
    UIView *view = self.view;
    CGFloat translateY = view.transform.ty;
    CGRect windowFrame = [view.window convertRect:focusView.frame fromView:focusView.superview];
    CGFloat dy = CGRectGetMaxY(view.window.bounds) - CGRectGetMaxY(windowFrame);
    CGFloat newEdgeBottom = MAX(dy + translateY, 0);
    if (self.edgeBottom == 0 || self.edgeBottom != newEdgeBottom){
        self.edgeBottom = newEdgeBottom;
    }
}

- (void)adjustScrollViewOffsetIfNeeded:(UIView *)focusView {
	RCTScrollViewComponentView *rct = [RNKeyboardAutoHandler findClosetScrollView:focusView];
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

+ (RCTScrollViewComponentView *)findClosetScrollView:(UIView *)view {
    if ([view isKindOfClass:[RCTScrollViewComponentView class]]) {
        return (RCTScrollViewComponentView *)view;
    }

    if (view.superview) {
        return [self findClosetScrollView:view.superview];
    }

    return nil;
}

@end

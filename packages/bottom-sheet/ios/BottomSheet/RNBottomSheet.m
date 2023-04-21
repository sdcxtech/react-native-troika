#import "RNBottomSheet.h"

#import <React/UIView+React.h>
#import <React/RCTLog.h>

@interface RNBottomSheet () <UIGestureRecognizerDelegate>

@property(nonatomic, strong) UIScrollView *target;
@property(nonatomic, strong) UIPanGestureRecognizer *panGestureRecognizer;

@property(nonatomic, assign) CGFloat minY;
@property(nonatomic, assign) CGFloat maxY;

@property(nonatomic, assign) BOOL nextReturn;
@property(nonatomic, assign) CGFloat lastDragDistance;

@property(nonatomic, strong) CADisplayLink *displayLink;

@end

@implementation RNBottomSheet

- (instancetype)init {
    if (self = [super init]) {
        _panGestureRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(handlePan:)];
        _panGestureRecognizer.delegate = self;
        _state = @"collapsed";
        [self addGestureRecognizer:_panGestureRecognizer];
    }
    return self;
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {
    if (gestureRecognizer == self.panGestureRecognizer && otherGestureRecognizer == self.target.panGestureRecognizer) {
        return YES;
    }
    return NO;
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch {
    if (gestureRecognizer != self.panGestureRecognizer) {
        return YES;
    }
    
    if (self.target) {
        [self.target removeObserver:self forKeyPath:@"contentOffset"];
    }
    self.target = nil;
    
    UIView *touchView = touch.view;
   
    while (touchView != nil && [touchView isDescendantOfView:self]) {
        if ([touchView isKindOfClass:[UIScrollView class]]) {
            UIScrollView *scrollView = (UIScrollView *)touchView;
            if (![self isHorizontal:scrollView]) {
                self.target = scrollView;
                [self.target addObserver:self forKeyPath:@"contentOffset" options:NSKeyValueObservingOptionNew | NSKeyValueObservingOptionOld context:nil];
                break;
            }
        }
        touchView = touchView.superview;
    }

    return YES;
}

- (void)willMoveToSuperview:(UIView *)superview {
    [super willMoveToSuperview:superview];
    if (superview == nil && self.target) {
        [self.target removeObserver:self forKeyPath:@"contentOffset"];
        self.target = nil;
    }
    if (superview == nil) {
        [self stopWatchBottomSheetTransition];
    }
}

- (void)willMoveToWindow:(UIWindow *)newWindow {
    [super willMoveToWindow:newWindow];
    if (newWindow == nil) {
        [self stopWatchBottomSheetTransition];
    }
}

- (BOOL)isHorizontal:(UIScrollView *)scrollView {
    return scrollView.contentSize.width > self.frame.size.width;
}


- (void)reactSetFrame:(CGRect)frame {
    [super reactSetFrame:frame];
    if (!CGRectEqualToRect(self.frame, CGRectZero)) {
        [self calculateOffset];
        if ([self.state isEqualToString:@"collapsed"]) {
            self.frame = CGRectOffset(self.frame, 0, self.frame.size.height - self.peekHeight);
            [self dispatchOnSlide:self.frame.origin.y];
        } else if ([self.state isEqualToString:@"hidden"]) {
            self.frame = CGRectOffset(self.frame, 0, self.frame.size.height);
            [self dispatchOnSlide:self.frame.origin.y];
        }
    }
}

- (void)layoutSubviews {
    [super layoutSubviews];
    [self calculateOffset];
    [self dispatchOnSlide:self.frame.origin.y];
}

- (void)calculateOffset {
    CGFloat parentHeight = self.superview.frame.size.height;
    self.minY = fmax(0, parentHeight - self.frame.size.height);
    self.maxY = fmax(self.minY, parentHeight - self.peekHeight);
}

- (void)handlePan:(UIPanGestureRecognizer *)pan {
    
    CGFloat translationY = [pan translationInView:self].y;
    [pan setTranslation:CGPointZero inView:self];
    
    CGFloat top = self.frame.origin.y;
    
    if (pan.state == UIGestureRecognizerStateChanged) {
        [self setStateInternal:@"dragging"];
    }
    
    // 如果有嵌套滚动
    if (self.target) {
        if(translationY > 0 && top < self.maxY && self.target.contentOffset.y <= 0) {
            //向下拖
            CGFloat y = fmin(top + translationY, self.maxY);
            self.frame = CGRectOffset(self.frame, 0, y - top);
            [self dispatchOnSlide:self.frame.origin.y];
        }
        
        if (translationY < 0 && top > self.minY) {
            //向上拖
            CGFloat y = fmax(top + translationY, self.minY);
            self.frame = CGRectOffset(self.frame, 0, y - top);
            [self dispatchOnSlide:self.frame.origin.y];
        }
    }
    
    // 没有嵌套滚动
    if (!self.target) {
        if(translationY > 0 && top < self.maxY) {
            //向下拖
            CGFloat y = fmin(top + translationY, self.maxY);
            self.frame = CGRectOffset(self.frame, 0, y - top);
            [self dispatchOnSlide:self.frame.origin.y];
        }
        
        if (translationY < 0 && top > self.minY) {
            //向上拖
            CGFloat y = fmax(top + translationY, self.minY);
            self.frame = CGRectOffset(self.frame, 0, y - top);
            [self dispatchOnSlide:self.frame.origin.y];
        }
    }
    
    if (pan.state == UIGestureRecognizerStateEnded) {
        if (self.lastDragDistance > 10) {
            if (self.target && self.target.contentOffset.y <= 0) {
                //如果是类似轻扫的那种
                [self settleToState:@"collapsed"];
            }
            
            if (!self.target) {
                //如果是类似轻扫的那种
                [self settleToState:@"collapsed"];
            }
        } else if (self.lastDragDistance < -10) {
            //如果是类似轻扫的那种
            [self settleToState:@"expanded"];
        } else {
            //如果是普通拖拽
            if(fabs(self.frame.origin.y - self.minY) > fabs(self.frame.origin.y - self.maxY)) {
                [self settleToState:@"collapsed"];
            } else {
                [self settleToState:@"expanded"];
            }
        }
    }
    
    self.lastDragDistance = translationY;
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(UIScrollView *)target change:(NSDictionary<NSKeyValueChangeKey,id> *)change context:(void *)context{
    if (_nextReturn) {
        _nextReturn = false;
        return;
    }
    
    if (![keyPath isEqualToString:@"contentOffset"]) {
        return;
    }
    
    CGFloat new = [change[@"new"] CGPointValue].y;
    CGFloat old = [change[@"old"] CGPointValue].y;

    if (new == old) {
        return;
    }
    
    CGFloat dy = old - new;

    if (dy > 0) {
        //向下
        if(target.contentOffset.y < 0){
            _nextReturn = true;
            target.contentOffset = CGPointMake(0, 0);
        }
    }
    
    if (dy < 0) {
        //向上
        if (self.frame.origin.y > self.minY) {
            _nextReturn = true;
            target.contentOffset = CGPointMake(0, old);
        }
    }
}

- (void)setPeekHeight:(CGFloat)peekHeight {
    _peekHeight = peekHeight;
    if (!CGRectEqualToRect(self.frame, CGRectZero)) {
        [self calculateOffset];
        if ([self.state isEqualToString:@"collapsed"]) {
            [self settleToState:@"collapsed"];
        }
    }
}

- (void)setState:(NSString *)state {
    if ([_state isEqualToString:state]) {
        return;
    }

    if (CGRectEqualToRect(self.frame, CGRectZero)) {
        [self setStateInternal:state];
        return;
    }
    
    [self settleToState:state];
}

- (void)settleToState:(NSString *)state {
    if ([state isEqualToString:@"collapsed"]) {
        [self startSettlingToState:state top:self.maxY];
    } else if ([state isEqualToString:@"expanded"]) {
        [self startSettlingToState:state top:self.minY];
    } else if ([state isEqualToString:@"hidden"]) {
        [self startSettlingToState:@"hidden" top:self.superview.frame.size.height];
    }
}

- (void)startSettlingToState:(NSString *)state top:(CGFloat)top {
    self.target.pagingEnabled = YES;
    [self setStateInternal:@"settling"];
    [self startWatchBottomSheetTransition];
    [self.layer removeAllAnimations];
    CGFloat duration = fmin(fabs(self.frame.origin.y - top) / (self.maxY - self.minY) * 0.3, 0.3);
    [UIView animateWithDuration:duration delay:0 options:UIViewAnimationOptionCurveEaseOut|UIViewAnimationOptionBeginFromCurrentState animations:^{
        self.frame = CGRectOffset(self.frame, 0, top - self.frame.origin.y);
    } completion:^(BOOL finished) {
        self.target.pagingEnabled = NO;
        [self stopWatchBottomSheetTransition];
        [self setStateInternal:state];
    }];
}

- (void)setStateInternal:(NSString *)state {
    if ([_state isEqualToString:state]) {
        return;
    }
    _state = state;
    
    if (self.onStateChanged) {
        if ([state isEqualToString:@"collapsed"] || [state isEqualToString:@"expanded"] || [state isEqualToString:@"hidden"]) {
            self.onStateChanged(@{
                @"state": state,
            });
        }
    }
}

- (void)dispatchOnSlide:(CGFloat)top {
    if (top < 0 || self.maxY == 0) {
        return;
    }
    if (self.onSlide) {
        CGFloat progress = fmin((top - self.minY) * 1.0f / (self.maxY - self.minY), 1);
        self.onSlide(@{
            @"progress": @(progress),
            @"offset": @(top),
            @"collapsedOffset": @(self.maxY),
            @"expandedOffset": @(self.minY)
        });
    }
}

- (void)startWatchBottomSheetTransition {
    [self stopWatchBottomSheetTransition];
    _displayLink = [CADisplayLink displayLinkWithTarget:self selector:@selector(watchBottomSheetTransition)];
    _displayLink.preferredFramesPerSecond = 120;
    [_displayLink addToRunLoop:[NSRunLoop mainRunLoop] forMode:NSRunLoopCommonModes];
}

- (void)stopWatchBottomSheetTransition {
    if(_displayLink){
        [_displayLink invalidate];
        _displayLink = nil;
    }
}

- (void)watchBottomSheetTransition {
    CGFloat top = [self.layer presentationLayer].frame.origin.y;
    [self dispatchOnSlide:top];
}
    
@end

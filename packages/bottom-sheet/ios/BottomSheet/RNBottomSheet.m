#import "RNBottomSheet.h"
#import "RNBottomSheetStateChangedEvent.h"
#import "RNBottomSheetOffsetChangedEvent.h"

#import <React/UIView+React.h>
#import <React/RCTRootContentView.h>
#import <React/RCTTouchHandler.h>
#import <React/RCTLog.h>

@interface RNBottomSheet () <UIGestureRecognizerDelegate>

@property(nonatomic, strong) UIView *contentView;

@property(nonatomic, strong) UIScrollView *target;
@property(nonatomic, strong) UIPanGestureRecognizer *panGestureRecognizer;

@property(nonatomic, assign) CGFloat minY;
@property(nonatomic, assign) CGFloat maxY;

@property(nonatomic, assign) BOOL nextReturn;

@property(nonatomic, strong) CADisplayLink *displayLink;
@property(nonatomic, strong) RCTEventDispatcher *eventDispatcher;

@end

@implementation RNBottomSheet {
    __weak RCTRootContentView *_rootView;
}

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher {
    if (self = [super init]) {
        _panGestureRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(handlePan:)];
        _panGestureRecognizer.delegate = self;
        _state = RNBottomSheetStateCollapsed;
        _eventDispatcher = eventDispatcher;
    }
    return self;
}

- (void)insertReactSubview:(UIView *)subview atIndex:(NSInteger)atIndex {
    [super insertReactSubview:subview atIndex:atIndex];
    if (atIndex == 0) {
        self.contentView = subview;
        [subview addGestureRecognizer:_panGestureRecognizer];
    }
}

- (void)removeReactSubview:(UIView *)subview {
    [super removeReactSubview:subview];
    if (self.contentView == subview) {
        [subview removeGestureRecognizer:_panGestureRecognizer];
    }
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {
    if (gestureRecognizer == self.panGestureRecognizer && otherGestureRecognizer == self.target.panGestureRecognizer) {
        return YES;
    }
    return NO;
}

- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {
    if (gestureRecognizer == self.panGestureRecognizer) {
        if ([super gestureRecognizerShouldBegin:gestureRecognizer]) {
            [self cancelRootViewTouches];
            return YES;
        }
        return NO;
    }
    return [super gestureRecognizerShouldBegin:gestureRecognizer];
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

- (void)didMoveToWindow {
    [super didMoveToWindow];
    if (self.window) {
        [self cacheRootView];
    }
}

- (void)cacheRootView {
  UIView *rootView = self;
  while (rootView.superview && ![rootView isReactRootView]) {
    rootView = rootView.superview;
  }
  _rootView = rootView;
}

- (void)cancelRootViewTouches {
    RCTRootContentView *rootView = (RCTRootContentView *)_rootView;
    [rootView.touchHandler cancel];
}

- (BOOL)isHorizontal:(UIScrollView *)scrollView {
    return scrollView.contentSize.width > self.frame.size.width;
}


- (void)reactSetFrame:(CGRect)frame {
    [super reactSetFrame:frame];
    
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if (!self.contentView) {
        return;
    }
    
    if (!CGRectEqualToRect(self.contentView.frame, CGRectZero)) {
        [self calculateOffset];
        if (self.state == RNBottomSheetStateCollapsed) {
            self.contentView.frame = CGRectOffset(self.contentView.frame, 0, self.maxY - self.contentView.frame.origin.y);
        } else if (self.state == RNBottomSheetStateExpanded) {
            self.contentView.frame = CGRectOffset(self.contentView.frame, 0, self.minY - self.contentView.frame.origin.y);
        } else if (self.state == RNBottomSheetStateHidden) {
            self.contentView.frame = CGRectOffset(self.contentView.frame, 0, self.frame.size.height - self.contentView.frame.origin.y);
        }
        [self dispatchOnSlide:self.contentView.frame.origin.y];
    }
}

- (void)calculateOffset {
    CGFloat parentHeight = self.frame.size.height;
    self.minY = fmax(0, parentHeight - self.contentView.frame.size.height);
    self.maxY = fmax(self.minY, parentHeight - self.peekHeight);
}

- (void)handlePan:(UIPanGestureRecognizer *)pan {
    CGFloat translationY = [pan translationInView:self.contentView].y;
    [pan setTranslation:CGPointZero inView:self.contentView];
    
    CGFloat top = self.contentView.frame.origin.y;
    
    if (pan.state == UIGestureRecognizerStateChanged) {
        [self setStateInternal:RNBottomSheetStateDragging];
    }
    
    // 如果有嵌套滚动
    if (self.target) {
        if(translationY > 0 && top < self.maxY && self.target.contentOffset.y <= 0) {
            //向下拖
            CGFloat y = fmin(top + translationY, self.maxY);
            self.contentView.frame = CGRectOffset(self.contentView.frame, 0, y - top);
            [self dispatchOnSlide:self.contentView.frame.origin.y];
        }
        
        if (translationY < 0 && top > self.minY) {
            //向上拖
            CGFloat y = fmax(top + translationY, self.minY);
            self.contentView.frame = CGRectOffset(self.contentView.frame, 0, y - top);
            [self dispatchOnSlide:self.contentView.frame.origin.y];
        }
    }
    
    // 没有嵌套滚动
    if (!self.target) {
        if(translationY > 0 && top < self.maxY) {
            //向下拖
            CGFloat y = fmin(top + translationY, self.maxY);
            self.contentView.frame = CGRectOffset(self.contentView.frame, 0, y - top);
            [self dispatchOnSlide:self.contentView.frame.origin.y];
        }
        
        if (translationY < 0 && top > self.minY) {
            //向上拖
            CGFloat y = fmax(top + translationY, self.minY);
            self.contentView.frame = CGRectOffset(self.contentView.frame, 0, y - top);
            [self dispatchOnSlide:self.contentView.frame.origin.y];
        }
    }
    
    if (pan.state == UIGestureRecognizerStateEnded || pan.state == UIGestureRecognizerStateCancelled) {
        // RCTLogInfo(@"velocity:%f", [pan velocityInView:self.contentView].y);
        CGFloat velocity = [pan velocityInView:self.contentView].y;
        if (velocity > 400) {
            if (self.target && self.target.contentOffset.y <= 0) {
                //如果是类似轻扫的那种
                [self settleToState:RNBottomSheetStateCollapsed];
            }
            
            if (!self.target) {
                //如果是类似轻扫的那种
                [self settleToState:RNBottomSheetStateCollapsed];
            }
        } else if (velocity < -400) {
            //如果是类似轻扫的那种
            [self settleToState:RNBottomSheetStateExpanded];
        } else {
            //如果是普通拖拽
            if(fabs(self.contentView.frame.origin.y - self.minY) > fabs(self.contentView.frame.origin.y - self.maxY)) {
                [self settleToState:RNBottomSheetStateCollapsed];
            } else {
                [self settleToState:RNBottomSheetStateExpanded];
            }
        }
    }
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
        if (self.contentView.frame.origin.y > self.minY) {
            _nextReturn = true;
            target.contentOffset = CGPointMake(0, old);
        }
    }
}

- (void)setPeekHeight:(CGFloat)peekHeight {
    _peekHeight = peekHeight;
    if (!CGRectEqualToRect(self.contentView.frame, CGRectZero)) {
        [self calculateOffset];
        if (self.state == RNBottomSheetStateCollapsed) {
            [self settleToState:RNBottomSheetStateCollapsed];
        }
    }
}

- (void)setState:(RNBottomSheetState)state {
    if (_state == state) {
        return;
    }

    if (CGRectEqualToRect(self.contentView.frame, CGRectZero)) {
        [self setStateInternal:state];
        return;
    }
    
    [self settleToState:state];
}

- (void)settleToState:(RNBottomSheetState)state {
    if (state == RNBottomSheetStateCollapsed) {
        [self startSettlingToState:state top:self.maxY];
    } else if (state == RNBottomSheetStateExpanded) {
        [self startSettlingToState:state top:self.minY];
    } else if (state == RNBottomSheetStateHidden) {
        [self startSettlingToState:state top:self.frame.size.height];
    }
}

- (void)startSettlingToState:(RNBottomSheetState)state top:(CGFloat)top {
    self.target.pagingEnabled = YES;
    [self setStateInternal:RNBottomSheetStateSettling];
    [self startWatchBottomSheetTransition];
    [self.layer removeAllAnimations];
//    CGFloat duration = fmin(fabs(self.contentView.frame.origin.y - top) / (self.maxY - self.minY) * 0.3, 0.3);
    [UIView animateWithDuration:0.25 delay:0 usingSpringWithDamping:1 initialSpringVelocity:0 options:NULL animations:^{
        self.contentView.frame = CGRectOffset(self.contentView.frame, 0, top - self.contentView.frame.origin.y);
    } completion:^(BOOL finished) {
        self.target.pagingEnabled = NO;
        [self stopWatchBottomSheetTransition];
        [self setStateInternal:state];
    }];
}

- (void)setStateInternal:(RNBottomSheetState)state {
    if (_state == state) {
        return;
    }
    _state = state;
    
    if (state == RNBottomSheetStateCollapsed || state == RNBottomSheetStateExpanded || state == RNBottomSheetStateHidden) {
        [self.eventDispatcher sendEvent:[[RNBottomSheetStateChangedEvent alloc] initWithViewTag:self.reactTag state:state]];
    }
}

- (void)dispatchOnSlide:(CGFloat)top {
    if (top < 0 || self.maxY == 0) {
        return;
    }
    CGFloat progress = fmin((top - self.minY) * 1.0f / (self.maxY - self.minY), 1);
    [self.eventDispatcher sendEvent:[[RNBottomSheetOffsetChangedEvent alloc] initWithViewTag:self.reactTag progress:progress offset:top minY:self.minY maxY:self.maxY]];
}

- (void)startWatchBottomSheetTransition {
    [self stopWatchBottomSheetTransition];
    _displayLink = [CADisplayLink displayLinkWithTarget:self selector:@selector(watchBottomSheetTransition)];
    _displayLink.preferredFramesPerSecond = 120;
    [_displayLink addToRunLoop:[NSRunLoop mainRunLoop] forMode:NSRunLoopCommonModes];
}

- (void)stopWatchBottomSheetTransition {
    if (self.state == RNBottomSheetStateCollapsed) {
        [self dispatchOnSlide:self.maxY];
    } else if (self.state == RNBottomSheetStateExpanded) {
        [self dispatchOnSlide:self.minY];
    }
    if(_displayLink){
        [_displayLink invalidate];
        _displayLink = nil;
    }
}

- (void)watchBottomSheetTransition {
    CGFloat top = [self.contentView.layer presentationLayer].frame.origin.y;
    [self dispatchOnSlide:top];
}
    
@end

#import "RNBottomSheet.h"

#import <React/UIView+React.h>
#import <React/RCTLog.h>

@interface RNBottomSheet () <UIGestureRecognizerDelegate>

@property(nonatomic, strong) UIScrollView *target;
@property(nonatomic, strong) UIPanGestureRecognizer *panGestureRecognizer;

@property(nonatomic, assign) CGFloat lastOffsetY;
@property(nonatomic, assign) BOOL nextReturn;

@property(nonatomic, assign) CGFloat peekHeight;
@property(nonatomic, assign) CGFloat lastDragDistance;

@end

@implementation RNBottomSheet

- (instancetype)init {
    if (self = [super init]) {
        _panGestureRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(handlePan:)];
        _panGestureRecognizer.delegate = self;
        _peekHeight = 200;
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
}

- (BOOL)isHorizontal:(UIScrollView *)scrollView {
    return scrollView.contentSize.width > self.frame.size.width;
}


- (void)reactSetFrame:(CGRect)frame {
    [super reactSetFrame:frame];
    self.frame = CGRectOffset(self.frame, 0, self.frame.size.height - self.peekHeight);
}

- (void)handlePan:(UIPanGestureRecognizer *)pan {
    
    CGFloat translationY = [pan translationInView:self].y;
    [pan setTranslation:CGPointZero inView:self];
    
    CGFloat top = self.frame.origin.y;
    
    CGFloat parentHeight = self.superview.frame.size.height;
    CGFloat minY = 0;
    CGFloat maxY = parentHeight - self.peekHeight;
    
    if (self.target) { // 如果有嵌套滚动
        
    } else { // 没有嵌套滚动
        if(translationY > 0 && top < maxY) {
            //向下拖
            CGFloat y = fmin(top + translationY, maxY);
            self.frame = CGRectOffset(self.frame, 0, y - top);
        }
        
        if (translationY < 0 && top > minY) {
            //向上拖
            CGFloat y = fmax(top + translationY, minY);
            self.frame = CGRectOffset(self.frame, 0, y - top);
        }
    }
    
    if (pan.state == UIGestureRecognizerStateEnded) {
        if (self.lastDragDistance > 10) {
            if (self.target && self.target.contentOffset.y <= 0) {
                //如果是类似轻扫的那种
                [self collapse];
            }
            
            if (!self.target) {
                //如果是类似轻扫的那种
                [self collapse];
            }
        } else if (self.lastDragDistance < -10) {
            //如果是类似轻扫的那种
            [self expand];
        } else {
            //如果是普通拖拽
            if(fabs(self.frame.origin.y - minY) > fabs(self.frame.origin.y - maxY)) {
                [self collapse];
            } else {
                [self expand];
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
}


- (void)collapse {
    CGFloat parentHeight = self.superview.frame.size.height;
    CGFloat minY = 0;
    CGFloat maxY = parentHeight - self.peekHeight;
    if(self.frame.origin.y == maxY) {
        return;
    }
    
    CGFloat duration = (maxY - self.frame.origin.y) / (self.frame.size.height - self.peekHeight) * 0.3;
    [UIView animateWithDuration:duration animations:^{
        self.frame = CGRectOffset(self.frame, 0, maxY - self.frame.origin.y);
    } completion:^(BOOL finished) {
        
    }];
}

- (void)expand {
    CGFloat parentHeight = self.superview.frame.size.height;
    CGFloat minY = 0;
    CGFloat maxY = parentHeight - self.peekHeight;
    
    if(self.frame.origin.y == minY) {
        return;
    }
    CGFloat time = (self.frame.origin.y - minY) / (self.frame.size.height - self.peekHeight) * 0.3;
    
    [UIView animateWithDuration:time animations:^{
        self.frame = CGRectOffset(self.frame, 0, minY - self.frame.origin.y);
    } completion:^(BOOL finished) {
        
    }];
}

@end

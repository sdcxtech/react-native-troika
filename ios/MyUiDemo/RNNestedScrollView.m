#import "RNNestedScrollView.h"
#import "RNNestedScrollViewHeader.h"
#import "RNNestedScrollViewLocalData.h"

#import <React/UIView+React.h>
#import <React/RCTLog.h>
#import <React/RCTScrollView.h>
#import <React/RCTUIManager.h>


#pragma mark - RNMainScrollView

@interface RNMainScrollView : UIScrollView <UIGestureRecognizerDelegate>

@property(nonatomic, strong) UIScrollView *target;
@property(nonatomic, strong) UIView *scrollingChild;

@end

@implementation RNMainScrollView

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
        [self.target removeObserver:self.superview forKeyPath:@"contentOffset"];
    }
    self.target = nil;
    
    UIView *touchView = touch.view;
   
    while (touchView != nil && [touchView isDescendantOfView:self.scrollingChild]) {
        if ([touchView isKindOfClass:[UIScrollView class]]) {
            UIScrollView *scrollView = (UIScrollView *)touchView;
            if (![self isHorizontal:scrollView]) {
                self.target = scrollView;
                [self.target addObserver:self.superview forKeyPath:@"contentOffset" options:NSKeyValueObservingOptionNew | NSKeyValueObservingOptionOld context:nil];
                break;
            }
        }
        touchView = touchView.superview;
    }

    return YES;
}

- (void)willMoveToSuperview:(UIView *)newSuperview {
    [super willMoveToSuperview:newSuperview];
    if (newSuperview == nil && self.target) {
        [self.target removeObserver:self.superview forKeyPath:@"contentOffset"];
    }
}

- (BOOL)isHorizontal:(UIScrollView *)scrollView {
    return scrollView.contentSize.width > self.frame.size.width;
}

@end

#pragma mark - RNNestedScrollView

@interface RNNestedScrollView () <UIScrollViewDelegate>

@property(nonatomic, strong) RNMainScrollView *main;
@property(nonatomic, strong) RNNestedScrollViewHeader *header;

@property(nonatomic, assign) CGFloat lastOffsetY;
@property(nonatomic, assign) BOOL nextReturn;

@property(nonatomic, weak) RCTBridge *bridge;

@end

@implementation RNNestedScrollView

- (instancetype)initWithBridge:(RCTBridge *)bridge {
    if (self = [super init]) {
        _bridge = bridge;
        _main = [[RNMainScrollView alloc] initWithFrame:CGRectZero];
        _main.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        _main.delegate = self;
        _main.showsVerticalScrollIndicator = NO;
        _main.showsHorizontalScrollIndicator = NO;
        _main.scrollsToTop = NO;
 
        if (@available(iOS 11.0, *)) {
            _main.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        }

        [self addSubview:_main];
    }
    return self;
}

- (void)dealloc {
    _main.delegate = nil;
    RCTLogInfo(@"RNNestedScrollView dealloc");
}

- (CGFloat)headerScrollRange {
    return [self.header maxScrollRange];
}

// main scrollView
- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    CGFloat offsetY = scrollView.contentOffset.y;
    
    RCTLogInfo(@"scrollViewDidScroll %f", offsetY);
    
    if (offsetY >= self.headerScrollRange) {
        scrollView.contentOffset = CGPointMake(0, self.headerScrollRange);
        self.lastOffsetY = scrollView.contentOffset.y;
    } else if(offsetY <= 0) {
        scrollView.contentOffset = CGPointMake(0, 0);
        self.lastOffsetY = scrollView.contentOffset.y;
    } else {
        if (self.main.target == nil) {
            self.lastOffsetY = scrollView.contentOffset.y;
            return;
        }

        CGFloat dy = self.lastOffsetY - offsetY;

        if (dy < 0) {
            // 向上
            RCTLogInfo(@"向上拖拽 main");
        }


        if (dy > 0) {
            // 向下
            RCTLogInfo(@"向下拖拽 main");
        }


        if(self.main.target.contentOffset.y > 0 && (scrollView.contentOffset.y < self.headerScrollRange) && (scrollView.contentOffset.y - self.lastOffsetY) < 0) {
            //向下拖拽
            scrollView.contentOffset = CGPointMake(0, self.lastOffsetY);
        } else if((scrollView.contentOffset.y - self.lastOffsetY) > 0 && self.main.target.contentOffset.y < 0) {
            //向上
            scrollView.contentOffset = CGPointMake(0, self.lastOffsetY);
        }
        self.lastOffsetY = scrollView.contentOffset.y;
    }
}

// target scrollView
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSKeyValueChangeKey,id> *)change context:(void *)context{
    if ([keyPath isEqualToString:@"contentOffset"]) {
        if (_nextReturn) {
            _nextReturn = false;
            return;
        }
        CGFloat new = [change[@"new"] CGPointValue].y;
        CGFloat old = [change[@"old"] CGPointValue].y;

        CGFloat dy = old - new;

        if (dy < 0) {
            RCTLogInfo(@"向上拖拽 target");
            //向上
            if (self.main.contentOffset.y < self.headerScrollRange) {
                if (((UIScrollView *)object).contentOffset.y > 0) {
                    _nextReturn = true;
                    if(old < 0) {
                        old = 0;
                    }
                   ((UIScrollView *)object).contentOffset = CGPointMake(0, old);
                }
            }
        }

        if (dy > 0) {
            RCTLogInfo(@"向下拖拽 target");
            //向下
            if(((UIScrollView *)object).contentOffset.y < 0){
                if (self.main.contentOffset.y > 0) {
                    _nextReturn = true;
                    ((UIScrollView *)object).contentOffset = CGPointMake(0, 0);
                }
            }
        }

    } else {
        [super observeValueForKeyPath:keyPath ofObject:object change:change context:context];
    }
}

- (BOOL)canBounceVertical:(UIScrollView *)scrollView {
    return YES;
}

- (void)insertReactSubview:(UIView *)subview atIndex:(NSInteger)atIndex {
    [super insertReactSubview:subview atIndex:atIndex];

    if ([subview isKindOfClass:[RNNestedScrollViewHeader class]]) {
        self.header = (RNNestedScrollViewHeader *)subview;
    } else {
        self.main.scrollingChild = subview;
    }
    
    [self.main addSubview:subview];
}

- (void)removeReactSubview:(UIView *)subview {
    [super removeReactSubview:subview];
}

- (void)didUpdateReactSubviews {
    // Do nothing, as subviews are managed by `insertReactSubview:atIndex:`
}

- (void)didSetProps:(NSArray<NSString *> *)props {
    
}

- (void)setClipsToBounds:(BOOL)clipsToBounds {
    super.clipsToBounds = clipsToBounds;
    _main.clipsToBounds = clipsToBounds;
}

- (CGSize)contentSize {
    return CGSizeMake(self.frame.size.width, self.frame.size.height + [self headerScrollRange]);
}

- (void)layoutSubviews {
    [super layoutSubviews];
    [self updateContentSizeIfNeeded];
}

- (void)updateContentSizeIfNeeded {
    CGSize contentSize = self.contentSize;
    if (!CGSizeEqualToSize(_main.contentSize, contentSize)) {
        _main.contentSize = contentSize;
    }
    
    CGSize scrollingChildSize = CGSizeMake(self.frame.size.width, self.frame.size.height - (self.header.frame.size.height -  [self headerScrollRange]));
    
    if (scrollingChildSize.height > 0) {
        RNNestedScrollViewLocalData *localData = [[RNNestedScrollViewLocalData alloc] initWithSize:scrollingChildSize];
        [_bridge.uiManager setLocalData:localData forView:self];
    }
}

@end




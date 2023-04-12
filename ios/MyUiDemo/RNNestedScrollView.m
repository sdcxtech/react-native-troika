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
                if (self.bounces) {
                    scrollView.bounces = NO;
                }
                self.target = scrollView;
                [self.target addObserver:self.superview forKeyPath:@"contentOffset" options:NSKeyValueObservingOptionNew | NSKeyValueObservingOptionOld context:nil];
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
        [self.target removeObserver:self.superview forKeyPath:@"contentOffset"];
        self.target = nil;
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
        _main.delaysContentTouches = NO;
        _main.bounces = NO;
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

- (void)setBounces:(BOOL)bounces {
    self.main.bounces = bounces;
}

- (CGFloat)headerScrollRange {
    return [self.header maxScrollRange];
}

// main scrollView
- (void)scrollViewDidScroll:(UIScrollView *)main {
    CGFloat newOffset = main.contentOffset.y;
    CGFloat dy = _lastOffsetY - newOffset;
    
    UIScrollView *target = self.main.target;
    if (target == nil) {
        if (dy < 0 && newOffset >= self.headerScrollRange) {
            main.contentOffset = CGPointMake(0, self.headerScrollRange);
            _lastOffsetY = main.contentOffset.y;
            return;
        }
        _lastOffsetY = main.contentOffset.y;
        return;
    }
    
    // 向下，main 下拉刷新
    if (dy > 0 && target.contentOffset.y <= 0) {
        _lastOffsetY = newOffset;
        return;
    }
    
    // 向下，main 上拉加载归位
    if (dy > 0 && _lastOffsetY > self.headerScrollRange) {
        main.contentOffset = CGPointMake(0, fmax(newOffset, self.headerScrollRange));
        _lastOffsetY = main.contentOffset.y;
        return;
    }
    
    // 向下，target 可向下，main 保持不变
    if (dy > 0 && target.contentOffset.y > 0) {
        main.contentOffset = CGPointMake(0, _lastOffsetY);
        return;
    }
    
    // 向上，target 优先取消下拉刷新，main 保持不变
    if (dy < 0 && target.contentOffset.y < 0) {
        main.contentOffset = CGPointMake(0, _lastOffsetY);
        return;
    }
    
    // 向上，target 可向上，main 保持 sticky
    if (dy < 0 && newOffset > self.headerScrollRange && target.contentOffset.y + 0.01 < target.contentSize.height - target.frame.size.height) {
        main.contentOffset = CGPointMake(0, self.headerScrollRange);
        _lastOffsetY = main.contentOffset.y;
        return;
    }
    
    _lastOffsetY = newOffset;
}

// target scrollView
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(UIScrollView *)target change:(NSDictionary<NSKeyValueChangeKey,id> *)change context:(void *)context{
    if (_nextReturn) {
        _nextReturn = false;
        return;
    }
    
    if (![keyPath isEqualToString:@"contentOffset"]) {
        return;
    }
    
    UIScrollView *main = self.main;
    CGFloat new = [change[@"new"] CGPointValue].y;
    CGFloat old = [change[@"old"] CGPointValue].y;
    CGFloat dy = old - new;
    
    // 向上
    if (dy < 0) {
        // main 可向上，target 归位
        if (main.contentOffset.y < self.headerScrollRange && target.contentOffset.y > 0) {
            _nextReturn = true;
            target.contentOffset = CGPointMake(0, fmax(0, old));
            return;
        }
        
        if (main.bounces && main.contentOffset.y > self.headerScrollRange && target.contentOffset.y + 0.01 >= target.contentSize.height - target.frame.size.height) {
            _nextReturn = true;
            target.contentOffset = CGPointMake(0,  target.contentSize.height - target.frame.size.height);
            return;
        }
    }
    
    //向下
    if (dy > 0) {
        // main 可向下，target 归位
        if((main.contentOffset.y > 0 || main.bounces) && target.contentOffset.y < 0){
            _nextReturn = true;
            target.contentOffset = CGPointMake(0, 0);
            return;
        }
        
        if (main.bounces && main.contentOffset.y > self.headerScrollRange && old + 0.01 >= target.contentSize.height - target.frame.size.height) {
            _nextReturn = true;
            target.contentOffset = CGPointMake(0, old);
        }
    }
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

- (void)reactSetFrame:(CGRect)frame {
    [super reactSetFrame:frame];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    [self updateContentSizeIfNeeded];
}

- (void)updateContentSizeIfNeeded {
    CGSize contentSize = self.contentSize;
    if (_CGSizeValid(contentSize) && !CGSizeEqualToSize(_main.contentSize, contentSize)) {
        _main.contentSize = contentSize;
    }
    
    CGSize scrollingChildSize = CGSizeMake(self.frame.size.width, self.frame.size.height - (self.header.frame.size.height -  [self headerScrollRange]));
    
    if (_CGSizeValid(scrollingChildSize)) {
        RNNestedScrollViewLocalData *localData = [[RNNestedScrollViewLocalData alloc] initWithSize:scrollingChildSize];
        [_bridge.uiManager setLocalData:localData forView:self];
    }
}

static BOOL _CGSizeValid(CGSize size) {
    return size.width > 0 && size.height > 0;
}

@end




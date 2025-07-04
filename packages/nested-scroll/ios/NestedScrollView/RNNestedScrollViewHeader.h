#import <React/RCTView.h>
#import <React/RCTEventDispatcher.h>
#import <react/renderer/components/RNCNestedScrollSpec/EventEmitters.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollViewHeader : RCTView

@property(nonatomic, assign) CGFloat stickyHeight;
@property(nonatomic, assign) NSUInteger stickyHeaderBeginIndex;
@property(nonatomic, copy) RCTDirectEventBlock onScroll;
@property(nonatomic, strong) RCTEventDispatcher *eventDispatcher;
#ifdef RCT_NEW_ARCH_ENABLED
@property(nonatomic) facebook::react::SharedViewEventEmitter eventEmitter;
#endif
- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher;

- (CGFloat)maxScrollRange;

@end

NS_ASSUME_NONNULL_END

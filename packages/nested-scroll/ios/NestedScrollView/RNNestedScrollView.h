#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>
#import <react/renderer/components/RNCNestedScrollSpec/EventEmitters.h>

NS_ASSUME_NONNULL_BEGIN

@interface RNNestedScrollView : UIView

@property(nonatomic, assign) BOOL bounces;
#ifdef RCT_NEW_ARCH_ENABLED
@property(nonatomic) facebook::react::SharedViewEventEmitter eventEmitter;
#endif
- (instancetype)initWithBridge:(RCTBridge *)bridge;

- (void)updateContentSizeIfNeeded;

- (void)insertReactSubview:(UIView *)subview atIndex:(NSInteger)atIndex;

- (void)removeReactSubview:(UIView *)subview;
@end

NS_ASSUME_NONNULL_END

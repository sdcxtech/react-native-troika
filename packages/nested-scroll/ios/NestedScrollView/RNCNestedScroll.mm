#import "RNCNestedScroll.h"

#import <React/RCTFabricComponentsPlugins.h>
#import <react/renderer/components/RNCNestedScrollSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNCNestedScrollSpec/Props.h>
#import <react/renderer/components/RNCNestedScrollSpec/EventEmitters.h>
#import <react/renderer/components/RNCNestedScrollSpec/RCTComponentViewHelpers.h>

#import "RCTFabricComponentsPlugins.h"
#import "RNNestedScrollView.h"
using namespace facebook::react;

@interface RNCNestedScroll () <RCTRNCNestedScrollViewProtocol>

@end

@implementation RNCNestedScroll {
    RNNestedScrollView *nestedScroll;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<RNCNestedScrollComponentDescriptor>();
}


- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        static const auto defaultProps = std::make_shared<RNCNestedScrollProps const>();
        _props = defaultProps;
        nestedScroll = [[RNNestedScrollView alloc] initWithFrame:self.bounds];
        self.contentView = nestedScroll;
    }
    return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
    [super updateProps:props oldProps:oldProps];
}

- (void)updateEventEmitter:(const facebook::react::EventEmitter::Shared &)eventEmitter
{
    [super updateEventEmitter:eventEmitter];
    assert(std::dynamic_pointer_cast<RNCNestedScrollEventEmitter const>(eventEmitter));
    [nestedScroll setEventEmitter:std::static_pointer_cast<RNCNestedScrollEventEmitter const>(eventEmitter)];
    
}

- (void)mountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
  [super mountChildComponentView:childComponentView index:index];
   
    [nestedScroll insertReactSubview:childComponentView atIndex:index];
//  [self.contentView addSubview:childComponentView];
}

- (void)unmountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
    [childComponentView removeFromSuperview];
}

- (void)prepareForRecycle
{
    [super prepareForRecycle];
    nestedScroll = [[RNNestedScrollView alloc] initWithFrame:self.bounds];
    self.contentView = nestedScroll;
}

@end

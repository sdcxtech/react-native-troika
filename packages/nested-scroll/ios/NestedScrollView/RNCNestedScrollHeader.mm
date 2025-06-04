
#import "RNCNestedScrollHeader.h"
#import <React/RCTFabricComponentsPlugins.h>
#import <React/RCTFabricComponentsPlugins.h>
#import <react/renderer/components/RNCNestedScrollSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNCNestedScrollSpec/Props.h>
#import <react/renderer/components/RNCNestedScrollSpec/EventEmitters.h>
#import <react/renderer/components/RNCNestedScrollSpec/RCTComponentViewHelpers.h>
#import "RCTFabricComponentsPlugins.h"
#import "RNNestedScrollViewHeader.h"
using namespace facebook::react;
@interface RNCNestedScrollHeader ()<RCTRNCNestedScrollHeaderViewProtocol>
@end
@implementation RNCNestedScrollHeader
{
    RNNestedScrollViewHeader *nestedScrollHeader;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<RNCNestedScrollHeaderComponentDescriptor>();
}


- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        static const auto defaultProps = std::make_shared<RNCNestedScrollHeaderProps const>();
        _props = defaultProps;
        nestedScrollHeader = [[RNNestedScrollViewHeader alloc] initWithFrame:self.bounds];
        self.contentView = nestedScrollHeader;
    }
    return self;
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
    const auto &newViewProps = *std::static_pointer_cast<RNCNestedScrollHeaderProps const>(props);
    [nestedScrollHeader setStickyHeight:newViewProps.stickyHeight];
    [super updateProps:props oldProps:oldProps];
}

- (void)updateEventEmitter:(const facebook::react::EventEmitter::Shared &)eventEmitter
{
    [super updateEventEmitter:eventEmitter];
    assert(std::dynamic_pointer_cast<RNCNestedScrollHeaderEventEmitter const>(eventEmitter));
    [nestedScrollHeader setEventEmitter:std::static_pointer_cast<RNCNestedScrollHeaderEventEmitter const>(eventEmitter)];
    
}

- (void)mountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
  [super mountChildComponentView:childComponentView index:index];
  
  [self.contentView addSubview:childComponentView];
}

- (void)unmountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index {
    [childComponentView removeFromSuperview];
}

- (void)prepareForRecycle
{
    [super prepareForRecycle];
    nestedScrollHeader = [[RNNestedScrollViewHeader alloc] initWithFrame:self.bounds];
    self.contentView = nestedScrollHeader;
}

- (CGFloat)maxScrollRange {
    return [nestedScrollHeader maxScrollRange];
}
@end

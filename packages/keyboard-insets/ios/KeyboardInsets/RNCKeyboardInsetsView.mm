//
//  RNCKeyboardInsetsView.m
//  RNKeyboardInsets
//
//  Created by 伟宁 on 2025/5/22.
//
#import "RNCKeyboardInsetsView.h"

#import <React/RCTFabricComponentsPlugins.h>
#import <react/renderer/components/RNCKeyboardInsetsViewSpec/ComponentDescriptors.h>
#import <react/renderer/components/RNCKeyboardInsetsViewSpec/Props.h>
#import <react/renderer/components/RNCKeyboardInsetsViewSpec/EventEmitters.h>
#import <react/renderer/components/RNCKeyboardInsetsViewSpec/RCTComponentViewHelpers.h>

#import "RNKeyboardInsetsView.h"
#import "RCTFabricComponentsPlugins.h"

using namespace facebook::react;

@interface RNCKeyboardInsetsView () <RCTRNCKeyboardInsetsViewViewProtocol>

@end

@implementation RNCKeyboardInsetsView {
    RNKeyboardInsetsView *insetsView;
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<RNCKeyboardInsetsViewComponentDescriptor>();
}

+ (void)load
{
  [super load];
}


- (instancetype)initWithFrame:(CGRect)frame
{
    if (self = [super initWithFrame:frame]) {
        static const auto defaultProps = std::make_shared<RNCKeyboardInsetsViewProps const>();
        _props = defaultProps;
        insetsView = [[RNKeyboardInsetsView alloc] initWithFrame:self.bounds];
        self.contentView = insetsView;
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
    assert(std::dynamic_pointer_cast<RNCKeyboardInsetsViewEventEmitter const>(eventEmitter));
    [insetsView setEventEmitter:std::static_pointer_cast<RNCKeyboardInsetsViewEventEmitter const>(eventEmitter)];
    
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
    insetsView = [[RNKeyboardInsetsView alloc] initWithFrame:self.bounds];
    self.contentView = insetsView;
}



@end

Class<RCTComponentViewProtocol> RNCKeyboardInsetsViewCls(void)
{
    return RNCKeyboardInsetsView.class;
}

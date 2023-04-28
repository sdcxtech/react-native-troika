//
//  RNCropManager.m
//  RNCrop
//
//  Created by vibe on 2022/2/8.
//

#import "RNImageCropManager.h"
#import "RNImageCrop.h"

@implementation RNImageCropManager

RCT_EXPORT_MODULE(RNImageCrop)
RCT_EXPORT_VIEW_PROPERTY(fileUri, NSString)
RCT_EXPORT_VIEW_PROPERTY(cropStyle, NSString)
RCT_EXPORT_VIEW_PROPERTY(objectRect, id)
RCT_EXPORT_VIEW_PROPERTY(onCropped, RCTBubblingEventBlock)
RCT_EXPORT_METHOD(crop:(nonnull NSNumber *)reactTag) {
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
        RNImageCrop *rnCrop = viewRegistry[reactTag];
        if (![rnCrop isKindOfClass:[RNImageCrop class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting RNCrop, got: %@", rnCrop);
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                RNImageCrop *rnCrop = (RNImageCrop *)viewRegistry[reactTag];
                [rnCrop crop];
            });
        }
    }];
}

- (UIView *)view
{
  return [RNImageCrop new];
}

@end

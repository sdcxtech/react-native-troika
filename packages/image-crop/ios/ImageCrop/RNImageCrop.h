#import <UIKit/UIKit.h>
#import <React/UIView+React.h>

#import "TOCropView.h"

@interface RNImageCrop : UIView

/**
 RN传递进来的属性值：需要裁剪图片路径
 */
@property(nonatomic, copy, nonnull) NSString *fileUri;

/**
 RN传递进来的属性值：裁剪样式，default  | circular
 */
@property(nonatomic, copy, nullable) NSString *cropStyle;

/**
 RN传递进来的属性值：图像主体Rect，如{"width":208,"left":43,"top":111,"height":354}
 */
@property(nonatomic, copy, nullable) id objectRect;

/**
 选定图片区域后，确认裁剪操作
 */
- (void)crop;

@property(nonatomic, copy, nullable) RCTBubblingEventBlock onCropped;

@end

#import "RNImageCrop.h"
#import "UIImage+CropRotate.h"

@interface RNImageCrop()

/**
 The original, uncropped image that was passed to this controller.
 */
@property (nonatomic, nonnull) UIImage *image;

/**
 The cropping style of this particular crop view controller
 */
@property (nonatomic) TOCropViewCroppingStyle croppingStyle;

/**
 The crop view managed by this view controller.
 */
@property (nonatomic, strong, nonnull) TOCropView *cropView;

@property (nonatomic, assign) BOOL initialSetupPerformed;

@end

@implementation RNImageCrop

- (instancetype)initWithFrame:(CGRect)frame {
    if ((self = [super initWithFrame:frame])) {
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    if (self.cropView && [self.subviews containsObject:self.cropView]) {
        if (self.initialSetupPerformed) {
            return;
        }
        self.initialSetupPerformed = YES;
        
        //设置图片主体检测参数
        if (![_cropStyle isEqualToString:@"circular"]
            && _objectRect != nil
            && [_objectRect objectForKey:@"top"]
            && [_objectRect objectForKey:@"left"]
            && [_objectRect objectForKey:@"width"]
            && [_objectRect objectForKey:@"height"]) {
            int top = [[_objectRect valueForKey:@"top"] intValue];
            int left = [[_objectRect valueForKey:@"left"] intValue];
            int width = [[_objectRect valueForKey:@"width"] intValue];
            int height = [[_objectRect valueForKey:@"height"] intValue];
            [self.cropView setImageCropFrame:CGRectMake(left, top, width, height)];
        }
        
        //存在极小的概率，初始化时只在左上角展示一小块图片区域，目前没有找到较好的方案，尝试延迟一点时间初始化CropView可以解决问题
        // [self.cropView performInitialSetup];
        [self.cropView performSelector:@selector(performInitialSetup) withObject:nil afterDelay:0.05];
    }
}

- (void)setFileUri:(NSString *)fileUri {
    _fileUri = fileUri;
    _image = [UIImage imageWithContentsOfFile: [[NSURL alloc] initWithString:fileUri].path];
}

- (void)didSetProps:(NSArray<NSString *> *)changedProps {
    [self addCropView:self.croppingStyle image:self.image];
}

- (void)addCropView:(TOCropViewCroppingStyle)style image:(UIImage *)image {
    if (!_cropView) {
        _cropView = [[TOCropView alloc] initWithCroppingStyle:style image:image];
        _cropView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        [_cropView setBackgroundColor:[UIColor blackColor]];
    }
    
    if (![self.subviews containsObject:self.cropView]) {
        [self addSubview:_cropView];
    }
}

- (void)crop {
    CGRect cropFrame = self.cropView.imageCropFrame;
    NSInteger angle = self.cropView.angle;
    
    UIImage *image = nil;
    if (angle == 0 && CGRectEqualToRect(cropFrame, (CGRect){CGPointZero, self.image.size})) {
        image = self.image;
    } else {
        image = [self.image croppedImageWithFrame:cropFrame angle:angle circularClip:NO];
    }
    
    [self saveImage:image];
}

- (void)saveImage:(UIImage*)image {
    NSString *fileName = [NSString stringWithFormat:@"%@.png", [self produceUUID]];
    NSString *filePath = [NSTemporaryDirectory() stringByAppendingPathComponent:fileName];
    BOOL result =[UIImagePNGRepresentation(image)writeToFile:filePath atomically:YES];

    if(result ==YES) {
        NSLog(@"保存成功: %@", filePath);
        
        if (self.onCropped) {
            self.onCropped(@{
                @"uri": [[NSURL alloc] initFileURLWithPath:filePath].absoluteString
            });
        }
    }
}

- (TOCropViewCroppingStyle)croppingStyle {
    if (_cropStyle && [_cropStyle isEqualToString:@"circular"]) {
        return TOCropViewCroppingStyleCircular;
    }
    return TOCropViewCroppingStyleDefault;
}


- (NSString *)produceUUID {
    CFUUIDRef uuid_ref = CFUUIDCreate(NULL);
    CFStringRef uuid_string_ref= CFUUIDCreateString(NULL, uuid_ref);
    NSString *uuid = [NSString stringWithString:(__bridge NSString *)uuid_string_ref];
    CFRelease(uuid_ref);
    CFRelease(uuid_string_ref);
    return [uuid uppercaseString];
}

@end

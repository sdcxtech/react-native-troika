#import "RNImageCropView.h"
#import "UIImage+CropRotate.h"
#import "TOCropView.h"

#import <react/renderer/components/imagecrop/ComponentDescriptors.h>
#import <react/renderer/components/imagecrop/EventEmitters.h>
#import <react/renderer/components/imagecrop/Props.h>
#import <react/renderer/components/imagecrop/RCTComponentViewHelpers.h>

#import <React/RCTConversions.h>
#import <React/RCTLog.h>

using namespace facebook::react;

@interface RNImageCropView() <RCTImageCropViewViewProtocol>

/**
 RN传递进来的属性值：需要裁剪图片路径
 */
@property(nonatomic, copy, nonnull) NSString *fileUri;

@property(nonatomic, assign) ImageCropViewCropStyle cropStyle;

@property(nonatomic, assign) ImageCropViewObjectRectStruct objectRect;

/**
 The original, uncropped image that was passed to this controller.
 */
@property (nonatomic, nonnull) UIImage *image;

/**
 The cropping style of this particular crop view controller
 */
@property (nonatomic, assign) TOCropViewCroppingStyle croppingStyle;

/**
 The crop view managed by this view controller.
 */
@property (nonatomic, strong, nonnull) TOCropView *cropView;

@property (nonatomic, assign) BOOL initialSetupPerformed;

@end

@implementation RNImageCropView

// Needed because of this: https://github.com/facebook/react-native/pull/37274
+ (void)load {
	[super load];
}

+ (ComponentDescriptorProvider)componentDescriptorProvider {
	return concreteComponentDescriptorProvider<ImageCropViewComponentDescriptor>();
}

+ (BOOL)shouldBeRecycled {
	return NO;
}

- (instancetype)initWithFrame:(CGRect)frame {
    if ((self = [super initWithFrame:frame])) {
		static const auto defaultProps = std::make_shared<const ImageCropViewProps>();
		_props = defaultProps;
		_cropStyle = ImageCropViewCropStyle::Default;
    }
    return self;
}

- (void)updateProps:(const facebook::react::Props::Shared &)props oldProps:(const facebook::react::Props::Shared &)oldProps {


	const auto &oldViewProps = static_cast<const ImageCropViewProps &>(*_props);
	const auto &newViewProps = static_cast<const ImageCropViewProps &>(*props);

	// `fileUri`
	if (newViewProps.fileUri != oldViewProps.fileUri) {
		self.fileUri = RCTNSStringFromStringNilIfEmpty(newViewProps.fileUri);
	}

	// `cropStyle`
	if (newViewProps.cropStyle != oldViewProps.cropStyle) {
		self.cropStyle = newViewProps.cropStyle;
	}

	// `objectRect`
	if (newViewProps.objectRect.width != oldViewProps.objectRect.width || newViewProps.objectRect.height != oldViewProps.objectRect.height || newViewProps.objectRect.top != oldViewProps.objectRect.top || newViewProps.objectRect.left != oldViewProps.objectRect.left) {
		self.objectRect = newViewProps.objectRect;
	}

	[self addCropView:self.croppingStyle image:self.image];

	[super updateProps:props oldProps:oldProps];
}

- (void)setFileUri:(NSString *)fileUri {
	_fileUri = fileUri;
	_image = [UIImage imageWithContentsOfFile: [[NSURL alloc] initWithString:fileUri].path];
}

- (const ImageCropViewEventEmitter &)eventEmitter {
	return static_cast<const ImageCropViewEventEmitter &>(*_eventEmitter);
}

- (void)handleCommand:(const NSString *)commandName args:(const NSArray *)args {
	RCTImageCropViewHandleCommand(self, commandName, args);
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

- (void)layoutSubviews {
    [super layoutSubviews];
    if (self.cropView && [self.subviews containsObject:self.cropView]) {
        if (self.initialSetupPerformed) {
            return;
        }
        self.initialSetupPerformed = YES;

        //设置图片主体检测参数
		if (self.cropStyle != ImageCropViewCropStyle::Circular
			&& self.objectRect.width > 0 && self.objectRect.height > 0) {
			int top = static_cast<int>(self.objectRect.top);
			int left = static_cast<int>(self.objectRect.left);
			int width = static_cast<int>(self.objectRect.width);
			int height = static_cast<int>(self.objectRect.height);
            [self.cropView setImageCropFrame:CGRectMake(left, top, width, height)];
        }

        //存在极小的概率，初始化时只在左上角展示一小块图片区域，目前没有找到较好的方案，尝试延迟一点时间初始化CropView可以解决问题
        // [self.cropView performInitialSetup];
        [self.cropView performSelector:@selector(performInitialSetup) withObject:nil afterDelay:0.05];
    }
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

- (void)saveImage:(UIImage*)image {
    NSString *fileName = [NSString stringWithFormat:@"%@.png", [self produceUUID]];
    NSString *filePath = [NSTemporaryDirectory() stringByAppendingPathComponent:fileName];
    BOOL result =[UIImagePNGRepresentation(image)writeToFile:filePath atomically:YES];

    if(result ==YES) {
        NSLog(@"保存成功: %@", filePath);
		NSString *uri = [[NSURL alloc] initFileURLWithPath:filePath].absoluteString;
		[self eventEmitter].onCrop(ImageCropViewEventEmitter::OnCrop{
			.uri = RCTStringFromNSString(uri)
		});
    }
}

- (TOCropViewCroppingStyle)croppingStyle {
	if (self.cropStyle == ImageCropViewCropStyle::Circular) {
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

package com.reactnative.imagecrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ExifInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.reactnative.imagecrop.ucrop.callback.BitmapCropCallback;
import com.reactnative.imagecrop.ucrop.view.GestureCropImageView;
import com.reactnative.imagecrop.ucrop.view.OverlayView;
import com.reactnative.imagecrop.ucrop.view.UCropView;

import java.io.File;
import java.util.UUID;

public class RNImageCropView extends FrameLayout {
    private static final int DEFAULT_COMPRESS_QUALITY = 90;
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;

    private static final String TAG = "RNImageCropView";
    private String fileUri;
    private Uri mOutputUri;

    public void setFileUri(String fileUri) {
        FLog.i(TAG, "设置属性fileUri = " + fileUri);
        this.fileUri = fileUri;
    }

    private String cropStyle;

    public void setCropStyle(String cropStyle) {
        this.cropStyle = cropStyle;
        FLog.i(TAG, "设置属性cropStyle = " + cropStyle);
    }

    private ObjectRect objectRect;

    public void setObjectRect(ReadableMap map) {
        FLog.i(TAG, "设置属性objectRect = " + map);
        this.objectRect = ObjectRect.fromReadableMap(map);
    }

    public void initProperties() {
        try {
            mGestureCropImageView.setImageURI(Uri.parse(fileUri));
            mGestureCropImageView.setImageUri(Uri.parse(fileUri), mOutputUri);
        } catch (Exception e) {
            FLog.i(TAG, "设置初始图片失败：" + e.getMessage());
        }

        try {
            mGestureCropImageView.setBackgroundColor(Color.BLACK);
            mGestureCropImageView.setRotateEnabled(false);

            if (cropStyle != null && cropStyle.equals("circular")) {
                mOverlayView.setShowCropGrid(false);
                mOverlayView.setCircleDimmedLayer(true);
                mOverlayView.setShowCropFrame(false);
                mGestureCropImageView.setTargetAspectRatio(1f);
            } else {
                mOverlayView.setFreestyleCropMode(OverlayView.FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH);

                if (objectRect != null) {
                    //设置图像主体边框
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    File file = new File(fileUri.replace("file:///", "/"));
                    BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    //如果发现图片存在旋转，需要调转图片宽高
                    int degree = getBitmapDegree(file);
                    if (degree == 90 || degree == 270) {
                        FLog.i(TAG, "需要调转图片宽高");
                        int tmp = imageWidth;
                        imageWidth = imageHeight;
                        imageHeight = tmp;
                    }

                    FLog.i(TAG, "imageHeight ：" + imageHeight);
                    FLog.i(TAG, "imageWidth ：" + imageWidth);
                    mOverlayView.setupDetectedObjectBounds(imageWidth, imageHeight, objectRect.getTop(), objectRect.getLeft(), objectRect.getWidth(), objectRect.getHeight());
                }
            }
        } catch (Exception e) {
            FLog.i(TAG, "初始化相关属性失败：" + e.getMessage());
        }
    }

    private UCropView mUCropView;
    private GestureCropImageView mGestureCropImageView;
    private OverlayView mOverlayView;

    public RNImageCropView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.rn_crop_view, this, true);
        mUCropView = view.findViewById(R.id.ucrop);
        mGestureCropImageView = mUCropView.getCropImageView();
        mOverlayView = mUCropView.getOverlayView();

        mOutputUri = Uri.fromFile(new File(context.getCacheDir(), UUID.randomUUID().toString() + ".png"));
        FLog.i(TAG, "裁剪后将保存为 = " + mOutputUri.toString());
    }

    public void crop() {
        mGestureCropImageView.cropAndSaveImage(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, new BitmapCropCallback() {
            @Override
            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                FLog.i(TAG, "裁剪后已保存为 = " + resultUri.toString());
                onCropped(resultUri.toString());
            }

            @Override
            public void onCropFailure(@NonNull Throwable t) {
                FLog.i(TAG, "保存失败 = " + t.toString());
            }
        });
    }

    private void onCropped(String uri) {
        WritableMap data = Arguments.createMap();
        data.putString("uri", uri);
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                "onCropped",
                data);
    }

    private int getBitmapDegree(File file) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degree;
    }
}

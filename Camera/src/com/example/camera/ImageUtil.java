package com.example.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageUtil {
	//保存图片本地路径
	public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath()
			+ "/account_gcy/";
	public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "icon_gcy/";
	private static final String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;
    public static final String IMAGE_FILE_NAME = "icon.jpeg";
    public static final int REQUEST_GALLERY = 0xa0;
    public static final int REQUEST_CAMERA = 0xa1;
    public static final int RE_GALLERY = 127;
    public static final int RE_CAMERA = 128;
	@SuppressWarnings("unused")
	private static ImageUtil instance = null;
	public static ImageUtil getCropHelperInstance() {
        return instance=new ImageUtil();
    }
    @SuppressWarnings("null")
	public void sethandleResultListerner(CropHandler handler, int requestCode,
                                         int resultCode, Intent data) {
        if (handler == null)
            return;
        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onCropCancel();
          
        } else if (resultCode == Activity.RESULT_OK) {
            Bitmap photo=null;
        	
            switch (requestCode) {
                case RE_CAMERA:
                    if (data == null || data.getExtras() == null) {
                        handler.onCropFailed("图片获取失败");
                        return;
                    }
                    photo = data.getExtras().getParcelable("data");
                    try {
                        photo.compress(Bitmap.CompressFormat.JPEG, 30,
                                new FileOutputStream(new File(IMGPATH, IMAGE_FILE_NAME)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        photo.recycle();
                    }
                    handler.onPhotoCropped(photo, requestCode);
                    break;
                case RE_GALLERY:
                    if (data == null || data.getExtras() == null) {
                        handler.onCropFailed("图片获取失败");
                        return;
                    }
                    photo = data.getExtras().getParcelable("data");
                    try {
                        photo.compress(Bitmap.CompressFormat.JPEG, 30,
                                new FileOutputStream(new File(IMGPATH, IMAGE_FILE_NAME)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        photo.recycle();
                    }
                    handler.onPhotoCropped(photo, requestCode);
                    break;
                case REQUEST_CAMERA:
                    Intent intent = buildCropIntent(Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
                    if (handler.getContext() != null) {
                        handler.getContext().startActivityForResult(intent,
                                RE_CAMERA);
                    } else {
                        handler.onCropFailed("CropHandler's context MUST NOT be null!");
                    }
                    break;
                case REQUEST_GALLERY:
                    Intent intent2 = buildCropIntent(data.getData());
                    if (handler.getContext() != null) {
                        handler.getContext().startActivityForResult(intent2,
                                RE_GALLERY);
                    } else {
                        handler.onCropFailed("CropHandler's context MUST NOT be null!");
                    }
                    break;
            }
        }
    }

    /**
     * 相册
     * @return
     */
    public Intent buildGalleryIntent() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        return galleryIntent;
    }
    /**
     * 拍照
     * @return
     */
    public Intent buildCameraIntent() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasSdcard()) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMGPATH, IMAGE_FILE_NAME)));
        }
        return cameraIntent;
    }
    /**
     * 判断 sd 卡 存在不
     * @return
     */
    private boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 裁剪
     * @param uri
     * @return
     */
    private Intent buildCropIntent(Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 250);
        cropIntent.putExtra("outputY", 250);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        cropIntent.putExtra("noFaceDetection", true);
        cropIntent.putExtra("return-data", true);
        return cropIntent;
    }

    public interface CropHandler {
        void onPhotoCropped(Bitmap photo, int requesCode);
        void onCropCancel();
        void onCropFailed(String message);
        Activity getContext();
    }
}

package com.sunday.common.helper;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sunday.common.utils.FileProvider7;
import com.sunday.common.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by siwei on 2018/3/25.
 * 头像设置helper
 */

public class ImgSetHelper {

    private Activity mActivity;
    public static final int REQUEST_CODE_TAKE_PHOTO = 0x110;
    public static final int REQ_PERMISSION_CODE_TAKE_PHOTO = 0X112;
    public static final int REQ_CROP = 0X113;
    public static final int REQ_SELECT_PHOTO = 0x114;

    private String mCurrentPhotoPath;
    private String photoPath;
    private String takePhotoDir;

    private SetListener mListener;

    public ImgSetHelper(Activity activity, String photoPath) {
        this.mActivity = activity;
        this.photoPath = photoPath;
        takePhotoDir = FileUtils.getAppFileDir(activity).getPath()+"/takephoto";
    }

    public void setListener(SetListener listener){
        this.mListener = listener;
    }


    /**拍照并请求权限*/
    public void takePhotoNoCompress() {
        if (ContextCompat.checkSelfPermission(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQ_PERMISSION_CODE_TAKE_PHOTO);

        } else {
            takePhoto();
        }
    }

    /**选择图片*/
    public void selectPhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivity.startActivityForResult(intent, REQ_SELECT_PHOTO);
    }

    private String handleImage(Intent data) {
        Uri uri = data.getData();
        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if (DocumentsContract.isDocumentUri(mActivity, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                            "content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equals(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            }
        } else {
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = mActivity.getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(takePhotoDir, filename);
            mCurrentPhotoPath = file.getAbsolutePath();

            Uri fileUri = FileProvider7.getUriForFile(mActivity, file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mActivity.startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
        }
    }

    //切图
    private void crop(String imagePath){
        //mCropImageFile = FileUtils.createTmpFile(getBaseContext());
        File mCropImageFile = new File(photoPath);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(new File(imagePath)), "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        mActivity.startActivityForResult(intent, REQ_CROP);
    }

    //把fileUri转换成ContentUri
    private Uri getImageContentUri(File imageFile){
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = mActivity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return mActivity.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_PERMISSION_CODE_TAKE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoNoCompress();
            } else {
                dispatchOnSetFaild();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK && data != null){
            if(requestCode == REQUEST_CODE_TAKE_PHOTO){
                crop(mCurrentPhotoPath);
            }else if(requestCode == REQ_SELECT_PHOTO){
                crop(handleImage(data));
            }else if(requestCode == REQ_CROP){
                dispatchOnCropSuccess(new File(photoPath));
            }
        }
    }

    //删除拍照产生的中间文件
    private void deleteTakePhoto(File file){
        if(file.isDirectory()){
            File[] fs = file.listFiles();
            for(File file1 : fs)deleteTakePhoto(file1);
        }else{
            file.delete();
        }
    }

    private void dispatchOnCropSuccess(File file){
        if(mListener != null){
            mListener.onCropSuccess(file);
        }
        deleteTakePhoto(new File(takePhotoDir));
    }

    private void dispatchOnSetFaild(){
        if(mListener != null){
            mListener.onSetFaild();
        }
        deleteTakePhoto(new File(takePhotoDir));
    }

    public interface SetListener{

        /**剪切成功*/
        void onCropSuccess(File file);

        /**设置失败*/
        void onSetFaild();
    }
}

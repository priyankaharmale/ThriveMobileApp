package hnweb.com.thrivemobileapp.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by neha on 11/17/2016.
 */
public class PickerUtils {
    public static File destination;
    public static Bitmap bmp;


    ///////////////------- MEDIA SELECT DIALOG -----------///////////
    public static void selectImage(final Activity context, final int REQUEST_CAMERA, final int FROM_GALLARY, final int SELECT_VIDEO) {
        final CharSequence[] items = {"Take Photo", "Choose Picture from Gallery", "Choose Video from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    takePictureIntent(context, REQUEST_CAMERA);

                } else if (items[item].equals("Choose Picture from Gallery")) {

                    galleryIntent(context, FROM_GALLARY);

                } else if (items[item].equals("Choose Video from Gallery")) {

                    chooseVideoIntent(context, SELECT_VIDEO);
//                    galleryIntent(context, FROM_GALLARY);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    ///////////////------- END MEDIA SELECT DIALOG -----------///////////


    ///////////////------- MEDIA SELECT DIALOG -----------///////////
    public static void selectImageDialog(final Activity context, final int REQUEST_CAMERA, final int FROM_GALLARY) {
        final CharSequence[] items = {"Take Photo", "Choose Picture from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    takePictureIntent(context, REQUEST_CAMERA);

                } else if (items[item].equals("Choose Picture from Gallery")) {

                    galleryIntent(context, FROM_GALLARY);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    ///////////////------- END MEDIA SELECT DIALOG -----------///////////

    //////////////---------- TAKE PICTURE FROM GALLERY ------------///////////////////
    public static void galleryIntent(Activity context, int FROM_GALLARY) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        context.startActivityForResult(intent, FROM_GALLARY);
    }

    public static String gallaryPath(Activity context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            if (picturePath != null) {
                Log.e("Path", picturePath);
                return picturePath;
//                imageVideoUpload(picturePath, hunt_id, hunt_item_id, user_id);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    //////////////---------- END TAKE PICTURE FROM GALLERY ------------///////////////////

    //////////////---------- TAKE PICTURE CAMERA ------------///////////////////

    public static void takePictureIntent(Activity context, int REQUEST_CAMERA) {
        String name = Constants.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".png");


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));

        //arshad
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", destination));
        context.startActivityForResult(intent, REQUEST_CAMERA);
    }

    public static String camerapath(ImageView uploadIV) {
        try {
            FileInputStream in = new FileInputStream(destination);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            String imagePath = destination.getAbsolutePath();
            Log.i("Path", imagePath);
            bmp = BitmapFactory.decodeStream(in, null, options);

            uploadIV.setImageBitmap(bmp);
            return imagePath;

//            picture.setImageBitmap(bmp);
//            imageVideoUpload(imagePath, hunt_id, hunt_item_id, user_id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String cameraPath() {
        try {
            FileInputStream in = new FileInputStream(destination);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            String imagePath = destination.getAbsolutePath();
            Log.i("Path", imagePath);
//            bmp = BitmapFactory.decodeStream(in, null, options);

//            uploadIV.setImageBitmap(bmp);
            return imagePath;

//            picture.setImageBitmap(bmp);
//            imageVideoUpload(imagePath, hunt_id, hunt_item_id, user_id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    //////////////---------- END TAKE PICTURE CAMERA ------------///////////////////


    //////////////---------- TAKE VIDEO FROM GALLERY ------------///////////////////

    public static void chooseVideoIntent(Activity context, int SELECT_VIDEO) {
        Intent intent = new Intent();
        intent.setType("video/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    public static String getGalleryVideoPath(Activity context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    //////////////---------- TAKE VIDEO FROM GALLERY ------------///////////////////


    /////////////----------- CAPTURE VIDEO ----------------------/////////////////

    /**
     * Launching camera app to record video
     */
    public static void recordVideo(Activity activity) {


        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);


        Constants.fileUri = getOutputMediaFileUri(Constants.MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Constants.fileUri); // set the image file
        // name

        // start the video capture Intent
        activity.startActivityForResult(intent, Constants.CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    public static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                Constants.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
//                Log.d(TAG, "Oops! Failed create "
//                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == Constants.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == Constants.MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
//            Constant.VIDFILE = mediaFile;
        } else {
            return null;
        }

        return mediaFile;
    }

    ///////////////------- MEDIA SELECT DIALOG -----------///////////
    public static void selectImage1(final Activity context, final int REQUEST_CAMERA, final int FROM_GALLARY, final int SELECT_VIDEO, int captureVideo) {
        final CharSequence[] items = {"Take Photo", "Take Video", "Choose Picture from Gallery", "Choose Video from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    takePictureIntent(context, REQUEST_CAMERA);

                } else if (items[item].equals("Take Video")) {

                    recordVideo(context);

                } else if (items[item].equals("Choose Picture from Gallery")) {

                    galleryIntent(context, FROM_GALLARY);

                } else if (items[item].equals("Choose Video from Gallery")) {

                    chooseVideoIntent(context, SELECT_VIDEO);
//                    galleryIntent(context, FROM_GALLARY);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    ///////////////------- END MEDIA SELECT DIALOG -----------///////////
}

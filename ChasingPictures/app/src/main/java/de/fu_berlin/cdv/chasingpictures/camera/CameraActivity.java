package de.fu_berlin.cdv.chasingpictures.camera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.fu_berlin.cdv.chasingpictures.MainActivity;
import de.fu_berlin.cdv.chasingpictures.R;


public class CameraActivity extends Activity {

    private static final String TAG = "CameraActivity";
    private static final File pictureStorageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private Camera mCamera;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPictureCallback;
    private Camera.Parameters params;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String EXTRA_IMAGE_FILE = "de.fu_berlin.cdv.chasingpictures.EXTRA_IMAGE_FILE";
    private Intent mResultData;
    private Button buttonEscape;
    private Button buttonTakePicture;
    private Button buttonRetry;
    private Button buttonFinish;
    private Button buttonFlashToAuto;
    private Button buttonFlashToOn;
    private Button buttonFlashToOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_activity);
        mPictureCallback = new PictureCallback();
        mResultData = new Intent();

        buttonEscape = (Button) findViewById(R.id.escapeButton);
        buttonTakePicture = (Button) findViewById(R.id.takePictureButton);
        buttonRetry = (Button) findViewById(R.id.retryPictureButton);
        buttonFinish = (Button) findViewById(R.id.finishCameraButton);
        buttonFlashToAuto = (Button) findViewById(R.id.flashToAutoCameraButton);
        buttonFlashToOn = (Button) findViewById(R.id.flashToOnCameraButton);
        buttonFlashToOff = (Button) findViewById(R.id.flashToOffCameraButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Create an instance of Camera
        mCamera = getCameraInstance();

        //region Autofocus
        // get Camera parameters
        params = mCamera.getParameters();

        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            // Autofocus mode is supported
            // set the focus mode
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        // set Camera parameters
        mCamera.setParameters(params);
        //endregion

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public void takePic(View view){
        // get an image from the camera
        mCamera.takePicture(null, null, mPictureCallback);

        buttonEscape.setVisibility(View.GONE);
        buttonTakePicture.setVisibility(View.GONE);
        buttonRetry.setVisibility(View.VISIBLE);
        buttonFinish.setVisibility(View.VISIBLE);
        buttonFlashToAuto.setVisibility(View.GONE);
        buttonFlashToOn.setVisibility(View.GONE);
        buttonFlashToOff.setVisibility(View.GONE);
    }

    public void showMyPic(View view){
        doFinish();
    }

    /**
     * Return to the previous activity, and return
     * the taken picture (if available).
     */
    public void doFinish() {
        setResult(RESULT_OK, mResultData);
        finish();
    }

    public void retry(View view){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void escape(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setFlashAuto(View view){
        params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        buttonFlashToAuto.setVisibility(View.GONE);
        buttonFlashToOn.setVisibility(View.VISIBLE);
        buttonFlashToOff.setVisibility(View.GONE);
    }

    public void setFlashOn(View view){
        params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        buttonFlashToAuto.setVisibility(View.GONE);
        buttonFlashToOn.setVisibility(View.GONE);
        buttonFlashToOff.setVisibility(View.VISIBLE);
    }

    public void setFlashOff(View view){
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        buttonFlashToAuto.setVisibility(View.VISIBLE);
        buttonFlashToOn.setVisibility(View.GONE);
        buttonFlashToOff.setVisibility(View.GONE);
    }


    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c;
    }


    private class PictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.e(TAG, "Error creating media file, check storage permissions.");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();


                Toast.makeText(
                        getApplicationContext(),
                        "Image Saved",
                        Toast.LENGTH_SHORT
                ).show();

                /*
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                Bitmap bmp = BitmapFactory.decodeFile(pictureFile);
                iv.setImageBitmap(bmp);
                */
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found", e);
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file", e);
            }

            // Refresh storage show our picture shows up in the gallery
            Log.i(TAG, "Scanning for file: " + pictureFile);
            MediaScannerConnection.scanFile(
                    getApplicationContext(),
                    new String[]{pictureFile.toString()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    }
            );

            // Put picture into result data
            mResultData.putExtra(EXTRA_IMAGE_FILE, pictureFile);
        }
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(
                pictureStorageDirectory,
                getString(R.string.app_name).replace(" ", "")
        );
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        if (type == MEDIA_TYPE_IMAGE){
            return new File(mediaStorageDir.getPath(), "IMG_"+ timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            return new File(mediaStorageDir.getPath(), "VID_" + timeStamp + ".mp4");
        }

        return null;
    }

}

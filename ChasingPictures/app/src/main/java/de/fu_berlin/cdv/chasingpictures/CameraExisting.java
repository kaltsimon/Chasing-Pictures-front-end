package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class CameraExisting extends Activity{

    ImageView iv;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        iv = (ImageView) findViewById(R.id.imageView);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

       // fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
       // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name


        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Intent intent = new Intent(this, MainActivity.class);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show();
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                iv.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                startActivity(intent);
            } else {
                // Image capture failed, advise user
                startActivity(intent);
            }
        }
    }


    public void showEnd(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}

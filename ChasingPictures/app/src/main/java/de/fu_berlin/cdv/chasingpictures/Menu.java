package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class Menu extends Activity {

    private static final int REQUEST_PICTURE_SELECTION = 0;
    private ImageView mImageNext;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mImageNext = (ImageView) findViewById(R.id.imageNext);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        mImageNext.setColorFilter(cf);

        // TODO: Send an API request with this location
        // TODO: Use the first place received to set the *next* picture
        // TODO: Register a listener to update the distance to that place

    }

    public void goToPictureSelection(View view) {
        Intent intent = new Intent(this, PictureSelectionActivity.class);
        startActivityForResult(intent, REQUEST_PICTURE_SELECTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICTURE_SELECTION:
                if (resultCode == RESULT_OK) {
                    // This means, that the user successfully finished a search
                    // TODO: Update the picture *last* to the finished search
                    // TODO: Set the picture *next* to another photo
                }
                break;
        }
    }
}

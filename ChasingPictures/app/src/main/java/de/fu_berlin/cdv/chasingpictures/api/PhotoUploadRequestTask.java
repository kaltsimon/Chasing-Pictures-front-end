package de.fu_berlin.cdv.chasingpictures.api;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;

import de.fu_berlin.cdv.chasingpictures.Menu;
import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.activity.Slideshow;
import de.fu_berlin.cdv.chasingpictures.utilities.UtilitiesPackage;

/**
 * @author Simon Kalt
 */
public class PhotoUploadRequestTask extends AsyncTask<Void, Void, ResponseEntity<Picture>> {

    private final Activity context;
    private final Place place;
    private final File image;

    public PhotoUploadRequestTask(Activity context, Place place, File image) {
        this.context = context;
        this.place = place;
        this.image = image;
    }

    @Override
    protected ResponseEntity<Picture> doInBackground(Void... params) {
        PhotoUploadRequest request = new PhotoUploadRequest(context, place, image);
        return request.sendRequest();
    }

    @Override
    protected void onPostExecute(ResponseEntity<Picture> response) {
        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            Intent intent = Slideshow.createIntent(
                    context,
                    place
            );
            context.startActivityForResult(intent, Menu.SLIDESHOW_REQUEST_SHOW_ONCE);
        } else {
            UtilitiesPackage.showError(context, R.string.error_upload_photo);
        }
    }
}

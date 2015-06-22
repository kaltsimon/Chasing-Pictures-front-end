package de.fu_berlin.cdv.chasingpictures.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.PictureDownloader;
import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.api.Picture;

/**
 * Display a slideshow of the given pictures.
 *
 * @author Simon Kalt
 */
public class Slideshow extends Activity {

    private static final String PICTURES_EXTRA = "de.fu_berlin.cdv.chasingpictures.EXTRA_PICTURES";
    protected List<Picture> pictures;
    private int slideShowIndex;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    /**
     * Creates an {@link Intent} for a slideshow using the given pictures.
     *
     * @param context  The current context
     * @param pictures A {@link Serializable} list of pictures
     * @return An intent to be used with {@link #startActivity(Intent)}.
     */
    public static Intent createIntent(Context context, List<Picture> pictures) {
        Intent intent = new Intent(context, Slideshow.class);
        intent.putExtra(PICTURES_EXTRA, (Serializable) pictures);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        mImageView = (ImageView) findViewById(R.id.slideshowImage);
        mProgressBar = (ProgressBar) findViewById(R.id.slideshowProgressBar);

        // Since we don't receive progress updates from the downloader yet,
        // make the progress bar indeterminate
        mProgressBar.setIndeterminate(true);

        // Retrieve list of pictures from intent
        pictures = (List<Picture>) getIntent().getSerializableExtra(PICTURES_EXTRA);

        // Download pictures
        PictureDownloader pd = new PictureDownloader(getCacheDir()) {
            @Override
            protected void onProgressUpdate(Object... values) {
                // TODO: Update mProgressBar
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // pictures have now been downloaded
                slideShowIndex = 0;
                updateImageView();
                hideProgressBarShowImage();
            }
        };
        pd.execute(pictures.toArray(new Picture[pictures.size()]));
    }

    private void hideProgressBarShowImage() {
        mProgressBar.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
    }

    private void updateImageView() {
        String file = pictures.get(slideShowIndex).getCachedFile().getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(file);
        mImageView.setImageBitmap(bitmap);
    }
}

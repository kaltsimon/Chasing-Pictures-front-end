package de.fu_berlin.cdv.chasingpictures.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

        // Retrieve list of pictures from intent
        pictures = (List<Picture>) getIntent().getSerializableExtra(PICTURES_EXTRA);

        // Download pictures
        PictureDownloader pd = new PictureDownloader(getCacheDir());
        pd.execute(pictures.toArray(new Picture[pictures.size()]));

    }
}

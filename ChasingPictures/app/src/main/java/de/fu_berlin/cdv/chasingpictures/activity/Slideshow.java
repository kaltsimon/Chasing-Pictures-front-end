package de.fu_berlin.cdv.chasingpictures.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
    private ProgressBar mProgressBar;
    private ViewGroup mContainerView;
    private RelativeLayout currentImageLayout;
    private Handler mHandler;

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
        mContainerView = (ViewGroup) findViewById(R.id.slideshowLayout);

        // Retrieve list of pictures from intent
        pictures = (List<Picture>) getIntent().getSerializableExtra(PICTURES_EXTRA);

        mProgressBar = (ProgressBar) findViewById(R.id.slideshowProgressBar);
        mProgressBar.setMax(pictures.size());

        mHandler = new Handler();

        // Download pictures
        PictureDownloader pd = new PictureDownloader(getCacheDir()) {
            @Override
            protected void onProgressUpdate(Progress... values) {
                if (values.length > 0)
                    mProgressBar.setProgress(values[0].getCurrent());
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                hideProgressBar();
                new SlideshowTask().executeOnExecutor(THREAD_POOL_EXECUTOR);
            }
        };

        pd.execute(pictures.toArray(new Picture[pictures.size()]));
    }

    private class SlideshowTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            for(int i = 0; i < pictures.size(); i++) {
                final int finalI = i;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setNewPicture(finalI);
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * Replace the currently displayed picture with the one with the given index.
     * @param index Index of the picture in the {@link #pictures} list
     */
    private void setNewPicture(int index) {
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.slideshow_image,
                mContainerView,
                false
        );

        ImageView newImageView = (ImageView) relativeLayout.getChildAt(0);

        newImageView.setImageBitmap(getBitmapForIndex(index));

        if (currentImageLayout != null) {
            currentImageLayout.setVisibility(View.INVISIBLE);
            mContainerView.removeView(currentImageLayout);
        }

        currentImageLayout = relativeLayout;
        mContainerView.addView(relativeLayout, 0);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private Bitmap getBitmapForIndex(int idx) {
        String file = pictures.get(idx).getCachedFile().getPath();
        return BitmapFactory.decodeFile(file);
    }
}

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

import java.io.File;
import java.io.Serializable;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.Maps;
import de.fu_berlin.cdv.chasingpictures.PictureDownloader;
import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.api.Picture;
import de.fu_berlin.cdv.chasingpictures.api.PictureRequest;
import de.fu_berlin.cdv.chasingpictures.api.Place;

/**
 * Display a slideshow of the given pictures.
 *
 * @author Simon Kalt
 */
public class Slideshow extends Activity {

    private static final String PICTURES_EXTRA = "de.fu_berlin.cdv.chasingpictures.EXTRA_PICTURES";
    protected List<Picture> mPictures;
    private ProgressBar mProgressBar;
    private ViewGroup mContainerView;
    private RelativeLayout currentImageLayout;
    private Handler mHandler;
    private Place mPlace;

    // Downloader for pictures
    private class SlideshowPictureDownloader extends PictureDownloader {
        public SlideshowPictureDownloader(File targetDirectory) {
            super(targetDirectory);
        }

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
    }

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

    /**
     * Creates an {@link Intent} for a slideshow using the given place.
     *
     * @param context  The current context
     * @param place A place for which to show the slideshow
     * @return An intent to be used with {@link #startActivity(Intent)}.
     */
    public static Intent createIntent(Context context, Place place) {
        Intent intent = new Intent(context, Slideshow.class);
        intent.putExtra(Maps.EXTRA_PLACE, place);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        mContainerView = (ViewGroup) findViewById(R.id.slideshowLayout);
        mProgressBar = (ProgressBar) findViewById(R.id.slideshowProgressBar);
        mHandler = new Handler();

        final PictureDownloader downloader = new SlideshowPictureDownloader(getCacheDir());

        // Try to get the place first
        mPlace = (Place) getIntent().getSerializableExtra(Maps.EXTRA_PLACE);

        if (mPlace == null) {
            // TODO: Remove this branch when no longer needed
            // Retrieve list of pictures from intent
            mPictures = (List<Picture>) getIntent().getSerializableExtra(PICTURES_EXTRA);
            downloader.execute(mPictures.toArray(new Picture[mPictures.size()]));
        } else {
            // TODO: Send an API request for the images
            final AsyncTask<PictureRequest, Void, List<Picture>> task = new AsyncTask<PictureRequest, Void, List<Picture>>() {
                @Override
                protected List<Picture> doInBackground(PictureRequest... params) {
                    return params[0].sendRequest().getBody().getPlaces().get(0).getPictures();
                }

                @Override
                protected void onPostExecute(List<Picture> pictures) {
                    mPictures = pictures;
                    mProgressBar.setMax(mPictures.size());
                    downloader.execute(mPictures.toArray(new Picture[mPictures.size()]));
                }
            };
            task.execute(new PictureRequest(this, mPlace));
        }
    }

    private class SlideshowTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            for(int i = 0; i < mPictures.size(); i++) {
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
     * @param index Index of the picture in the {@link #mPictures} list
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
        String file = mPictures.get(idx).getCachedFile().getPath();
        return BitmapFactory.decodeFile(file);
    }
}

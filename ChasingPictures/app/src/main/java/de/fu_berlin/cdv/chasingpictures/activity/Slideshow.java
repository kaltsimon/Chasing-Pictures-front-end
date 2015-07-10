package de.fu_berlin.cdv.chasingpictures.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.Maps;
import de.fu_berlin.cdv.chasingpictures.PictureDownloader;
import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.api.Picture;
import de.fu_berlin.cdv.chasingpictures.api.PictureRequest;
import de.fu_berlin.cdv.chasingpictures.api.Place;
import de.fu_berlin.cdv.chasingpictures.api.PlacesApiResult;
import de.fu_berlin.cdv.chasingpictures.util.Utilities;

/**
 * Display a slideshow of all the pictures for a place.
 *
 * @author Simon Kalt
 */
public class Slideshow extends Activity {

    private static final String TAG = "SlideshowActivity";
    protected List<Picture> mPictures;
    private ProgressBar mProgressBar;
    private ViewGroup mContainerView;
    private ImageView currentImageView;
    private Handler mHandler;
    private Place mPlace;
    private SlideshowTask mSlideshowTask;
    private PictureDownloader mDownloader;
    private PictureRequestTask mPictureRequestTask;

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

        // Make activity fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_slideshow);
        mContainerView = (ViewGroup) findViewById(R.id.slideshowLayout);
        mProgressBar = (ProgressBar) findViewById(R.id.slideshowProgressBar);
        mHandler = new Handler();
        mPlace = (Place) getIntent().getSerializableExtra(Maps.EXTRA_PLACE);

        if (mPlace == null) {
            Utilities.showError(this, R.string.error_api_no_place);
            setResult(RESULT_CANCELED);
            finish();
        }

        mDownloader = new SlideshowPictureDownloader(getCacheDir());
        mPictureRequestTask = new PictureRequestTask(mDownloader);
        mPictureRequestTask.execute(new PictureRequest(this, mPlace));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Resume slideshow, if possible
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cancel anything still running

        if (mDownloader != null)
            mDownloader.cancel(true);

        if (mSlideshowTask != null)
            mSlideshowTask.cancel(true);
    }

    /**
     * Replace the currently displayed picture with the one at the given index.
     *
     * @param index Index of the picture in the {@link #mPictures} list
     */
    private void setNewPicture(int index) {
        ImageView newImageView = (ImageView) LayoutInflater.from(this).inflate(
                R.layout.slideshow_image,
                mContainerView,
                false
        );

        newImageView.setImageBitmap(getBitmapForIndex(index));

        if (currentImageView != null) {
            currentImageView.setVisibility(View.INVISIBLE);
            mContainerView.removeView(currentImageView);
        }

        currentImageView = newImageView;
        mContainerView.addView(currentImageView, 0);
    }

    /**
     * Return the bitmap at the given index of available pictures.
     *
     * @param index Index of the picture
     * @return A bitmap of the picture or {@code null} if an error occurred.
     */
    @Nullable
    private Bitmap getBitmapForIndex(int index) {
        Bitmap bitmap = null;
        try {
            Picture picture = mPictures.get(index);
            File cachedFile = picture.getCachedFile();
            String filePath = cachedFile.getPath();
            bitmap = BitmapFactory.decodeFile(filePath);

            if (bitmap == null)
                throw new NullPointerException("File " + filePath + " could not be read.");

        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            Log.e(TAG, "Could not read picture at slideshow index " + index + ".", ex);
            Utilities.showError(this, R.string.error_slideshow_photo_unavailable, index);
        }

        return bitmap;
    }

    /**
     * A background task for requesting all the available pictures for the current place.
     */
    private class PictureRequestTask extends AsyncTask<PictureRequest, Void, List<Picture>> {
        private final PictureDownloader downloader;

        public PictureRequestTask(PictureDownloader downloader) {
            this.downloader = downloader;
        }

        @Override
        protected List<Picture> doInBackground(PictureRequest... params) {
            if (params.length == 0)
                return null;

            List<Picture> pictures = null;
            try {
                ResponseEntity<PlacesApiResult> responseEntity = params[0].sendRequest();
                PlacesApiResult body = responseEntity.getBody();
                List<Place> places = body.getPlaces();
                Place place = places.get(0);
                pictures = place.getPictures();
            } catch (NullPointerException | IndexOutOfBoundsException ex) {
                Log.e(TAG, "Did not receive any pictures", ex);
            }

            return pictures;
        }

        @Override
        protected void onPostExecute(List<Picture> pictures) {
            if (pictures == null) {
                // We have no reason to stay here...
                Utilities.showError(getApplicationContext(), R.string.error_api_no_pictures);
                finish();
            }
            else {
                mPictures = pictures;
                mProgressBar.setMax(mPictures.size());
                downloader.execute(mPictures.toArray(new Picture[mPictures.size()]));
            }
        }
    }

    /**
     * A background task for downloading the given pictures
     * and, when finished, starting the slideshow.
     */
    private class SlideshowPictureDownloader extends PictureDownloader {
        public SlideshowPictureDownloader(File targetDirectory) {
            super(targetDirectory);
        }

        @Override
        protected void handleProgressUpdate(@NonNull Progress progress) {
            super.handleProgressUpdate(progress);
            mProgressBar.setProgress(progress.getState());
        }

        @Override
        protected void handleException(@Nullable Throwable exception) {
            super.handleException(exception);
            if (exception != null) {
                Utilities.showError(getApplicationContext(), R.string.error_download_failed);
                // TODO: Cancel and exit?
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressBar.setVisibility(View.GONE);
            mSlideshowTask = new SlideshowTask();
            mSlideshowTask.execute();
        }
    }

    /**
     * A background task for animating the transition between images.
     */
    private class SlideshowTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            for(int i = 0; i < mPictures.size(); i++) {
                if (isCancelled())
                    return null;

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
                    Log.e(TAG, "Could not sleep.", e);
                }
            }

            finish();

            return null;
        }
    }
}

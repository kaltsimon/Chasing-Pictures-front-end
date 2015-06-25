package de.fu_berlin.cdv.chasingpictures;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.fu_berlin.cdv.chasingpictures.api.Picture;

/**
 * @author Simon Kalt
 */
public class PictureDownloader extends AsyncTask<Picture, PictureDownloader.Progress, Void> {
    private static final String TAG = "PictureDownloader";
    private static final int BUFFER_SIZE = 1024 * 100; // 100 KB
    private final File targetDirectory;
    private boolean cancelOnError = false;

    /**
     * Creates a new downloader, that tries to download all given
     * pictures, even if one download fails.
     *
     * @param targetDirectory Directory in the file system the pictures should be saved to.
     */
    public PictureDownloader(File targetDirectory) {
        this(targetDirectory, false);
    }

    /**
     * Creates a new downloader.
     *
     * @param targetDirectory Directory in the file system the pictures should be saved to.
     * @param cancelOnError Whether or not the downloader should stop downloading after an exception.
     */
    public PictureDownloader(File targetDirectory, boolean cancelOnError) {
        this.targetDirectory = targetDirectory;
        this.cancelOnError = cancelOnError;
    }

    @Override
    protected Void doInBackground(Picture... params) {
        int count = 0;

        for (Picture picture : params) {
            final Progress progress = new Progress(count, null);

            if (isCancelled())
                return null;

            try {
                String fileName = picture.getId() + "_" + picture.getUpdatedAt().getTime();
                File destinationFile = new File(targetDirectory, fileName);

                picture.setCachedFile(destinationFile);

                // If file already exists, (and isn't empty) skip the download.
                if (!(destinationFile.exists() && destinationFile.length() > 0)) {
                    URL url = picture.getASCIIUrl();
                    downloadUrlToFile(url, destinationFile);
                }
            } catch (IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
                picture.setCachedFile(null);
                progress.setException(e);
            }

            publishProgress(progress);
            count++;
        }
        return null;
    }

    @Override
    protected final void onProgressUpdate(Progress... values) {
        if (values.length > 0) {
            Progress progress = values[0];
            if (progress != null) {
                handleProgressUpdate(progress);
                handleException(progress.getException());
            }
        }
    }

    /**
     * Handle progress updates.
     *
     * @param progress The current progress, guaranteed to not be {@code null}
     */
    protected void handleProgressUpdate(@NonNull Progress progress) {

    }

    /**
     * If an exception occurred and {@link #cancelOnError} is set to true,
     * the downloading process will be stopped.
     * <p />
     * <em>Derived classes should call through to the super class's implementation of this method.</em>
     * @param exception An exception, or {@code null} if none occurred
     */
    protected void handleException(@Nullable Throwable exception) {
        if (exception != null) {
            Log.e(TAG, "Exception during download.", exception);

            if (cancelOnError)
                cancel(true);
        }
    }

    private void downloadUrlToFile(URL url, File destinationFile) throws IOException {
        String location;
        HttpURLConnection urlConnection;

        // Follow redirects
        while (true) {
            urlConnection = ((HttpURLConnection) url.openConnection());

            /*
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setInstanceFollowRedirects(false);
             */

            switch (urlConnection.getResponseCode()) {
                case HttpURLConnection.HTTP_MOVED_PERM:
                case HttpURLConnection.HTTP_MOVED_TEMP:
                    location = urlConnection.getHeaderField("Location");
                    url = new URL(url, location);  // Deal with relative URLs
                    continue;
            }

            break;
        }

        int contentLength = urlConnection.getContentLength();

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        FileOutputStream fos = new FileOutputStream(destinationFile);
        byte[] buffer;

        if (contentLength > 0) {
            buffer = new byte[contentLength];
        } else {
            buffer = new byte[BUFFER_SIZE];
        }

        try {
            int result;
            do {
                result = in.read(buffer);
                if (result > 0)
                    fos.write(buffer, 0, result);
            } while (result > 0);
        } finally {
            in.close();
            fos.close();
        }
    }

    /**
     * Class indicating the progress of downloading.
     * If an exception happened, it can be retrieved using {@link #getException()}.
     */
    public static class Progress {
        private final int state;
        @Nullable
        private Throwable exception;

        public Progress(int state) {
            this.state = state;
        }

        public Progress(int state, @Nullable Throwable exception) {
            this.state = state;
            this.exception = exception;
        }

        public int getState() {
            return state;
        }

        @Deprecated
        public int getCurrent() {
            return state;
        }

        /**
         * Returns the exception published with this progress update.
         *
         * @return A {@link Throwable} describing the error, or {@code null} if no exception occurred.
         */
        @Nullable
        public Throwable getException() {
            return exception;
        }

        public void setException(@Nullable Throwable exception) {
            this.exception = exception;
        }
    }
}

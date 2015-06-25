package de.fu_berlin.cdv.chasingpictures;

import android.os.AsyncTask;
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
 * @author Simon
 */
public class PictureDownloader extends AsyncTask<Picture, PictureDownloader.Progress, Void> {
    private static final String TAG = "PictureDownloader";
    private static final int BUFFER_SIZE = 1024 * 100; // 100 KB
    private final File targetDirectory;

    public PictureDownloader(File targetDirectory) {
        this.targetDirectory = targetDirectory;
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
                if (destinationFile.exists() && destinationFile.length() > 0) {
                    continue;
                }

                URL url = picture.getASCIIUrl();
                downloadUrlToFile(url, destinationFile);
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

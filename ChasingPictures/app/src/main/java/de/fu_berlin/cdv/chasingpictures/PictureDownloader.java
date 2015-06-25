package de.fu_berlin.cdv.chasingpictures;

import android.os.AsyncTask;
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
        Progress progress = new Progress(params.length);

        for (Picture picture : params) {
            if (isCancelled())
                return null;

            try {
                String fileName = picture.getId() + "_" + picture.getUpdatedAt().getTime();
                File destinationFile = new File(targetDirectory, fileName);

                // If file already exists, skip the download.
                picture.setCachedFile(destinationFile);
                if (destinationFile.exists()) {
                    continue;
                }

                URL url = picture.getASCIIUrl();
                downloadUrlToFile(url, destinationFile);
            } catch (IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
                picture.setCachedFile(null);
            }

            publishProgress(progress.advance());
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

    public static class Progress {
        protected int current;
        public final int max;

        public Progress(int max) {
            this.max = max;
            this.current = 0;
        }

        public Progress advance() {
            current++;
            return this;
        }

        public double getPercent() {
            return (100.0 * current) / max;
        }

        public int getCurrent() {
            return current;
        }
    }
}

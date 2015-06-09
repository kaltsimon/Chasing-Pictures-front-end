package de.fu_berlin.cdv.chasingpictures;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import de.fu_berlin.cdv.chasingpictures.api.Picture;

/**
 * @author Simon
 */
public class PictureDownloader extends AsyncTask<Picture, Object, Void> {
    private static final String TAG = "PictureDownloader";
    private static final int BUFFER_SIZE = 1024 * 100; // 100 KB
    private final File targetDirectory;

    public PictureDownloader(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    @Override
    protected Void doInBackground(Picture... params) {
        for (Picture picture : params) {

            try {
                String fileName = picture.getId() + "_" + picture.getUpdatedAt().getTime();
                File destinationFile = new File(targetDirectory, fileName);

                // If file already exists, skip the download.
                picture.setCachedFile(destinationFile);
                if (destinationFile.exists()) {
                    continue;
                }

                URL url = new URL(picture.getEncodedUrl());
                downloadUrlToFile(url, destinationFile);
            } catch (IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
                picture.setCachedFile(null);
            }
        }
        return null;
    }

    private void downloadUrlToFile(URL url, File destinationFile) throws IOException {
        URLConnection urlConnection = url.openConnection();
        int contentLength = urlConnection.getContentLength();

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        FileOutputStream fos = new FileOutputStream(destinationFile);
        byte[] buffer;

        if (contentLength >= 0) {
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
}

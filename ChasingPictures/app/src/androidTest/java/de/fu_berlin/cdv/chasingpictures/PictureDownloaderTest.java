package de.fu_berlin.cdv.chasingpictures;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import junit.framework.TestCase;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.fu_berlin.cdv.chasingpictures.api.Picture;

/**
 * @author Simon
 */
public class PictureDownloaderTest extends ApplicationTestCase<Application> {

    private static final String TAG = "PictureDownloaderTest";

    public PictureDownloaderTest() {
        super(Application.class);
    }

    public void setUp() throws Exception {

    }

    public void testPictureDownloader() throws Exception {
        Picture p = new Picture();
        Picture p2 = new Picture();
        p.setId(5);
        p2.setId(7);
        p.setUpdatedAt(new GregorianCalendar(2015, 6, 9, 14, 25).getTime());
        p2.setUpdatedAt(new GregorianCalendar(2015, 6, 9, 14, 34).getTime());
        p.setUrl("http://upload.wikimedia.org/wikipedia/commons/8/89/Mauerstrasse-Dreifaltigkeitskirche-GDR-71-67.jpg");
        p2.setUrl("http://upload.wikimedia.org/wikipedia/commons/5/5c/Platz_am_Zeughaus-VII-59-513-x.jpg");
        Picture[] pictures = {p, p2};

        PictureDownloader pictureDownloader = new PictureDownloader(getContext().getCacheDir());
        pictureDownloader.execute(pictures);

        pictureDownloader.get();

        for (Picture pic : pictures) {
            Log.d(TAG, "Picture: " + pic.getUrl());
            Log.d(TAG, "File: " + pic.getCachedFile());
        }
    }
}
package de.fu_berlin.cdv.chasingpictures;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import junit.framework.TestCase;

import java.util.Calendar;
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

    protected static Picture makePicture(String url, long id, Date updatedAt) {
        Picture p = new Picture();
        p.setUrl(url);
        p.setId(id);
        p.setUpdatedAt(updatedAt);

        return p;
    }

    public void testPictureDownloader() throws Exception {
        GregorianCalendar updatedAt = new GregorianCalendar(2015, 6, 9, 14, 34);
        Picture p = makePicture("http://upload.wikimedia.org/wikipedia/commons/8/89/Mauerstrasse-Dreifaltigkeitskirche-GDR-71-67.jpg", 5, updatedAt.getTime());
        updatedAt.roll(Calendar.HOUR, 1);
        Picture p2 = makePicture("http://upload.wikimedia.org/wikipedia/commons/5/5c/Platz_am_Zeughaus-VII-59-513-x.jpg", 7, updatedAt.getTime());
        updatedAt.roll(Calendar.MINUTE, 10);
        Picture p3 = makePicture("http://upload.wikimedia.org/wikipedia/commons/6/68/Schlossbrücke-VII-62-424-a-W.jpg", 8, updatedAt.getTime());

        Picture[] pictures = {p, p2, p3};

        PictureDownloader pictureDownloader = new PictureDownloader(getContext().getCacheDir());
        pictureDownloader.execute(pictures);

        pictureDownloader.get();

        for (Picture pic : pictures) {
            Log.d(TAG, "Picture: " + pic.getUrl());
            Log.d(TAG, "File: " + pic.getCachedFile());
        }
    }
}
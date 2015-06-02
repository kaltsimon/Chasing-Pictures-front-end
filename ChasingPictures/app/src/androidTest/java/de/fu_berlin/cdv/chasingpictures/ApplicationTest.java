package de.fu_berlin.cdv.chasingpictures;

import android.app.Application;
import android.content.res.Resources;
import android.test.ApplicationTestCase;
import android.util.Log;

import static junit.framework.Assert.*;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private static final String LOG_TAG = "ApplicationTest";
    Resources resources;

    public ApplicationTest() {
        super(Application.class);
    }

    public void setUp() throws Exception {
        resources = mContext.getResources();
    }

    public void testResourceBundle() throws Exception {
        assertEquals(resources.getString(R.string.app_name), "Chasing Pictures");
    }
}

package de.fu_berlin.cdv.chasingpictures.api;

import android.app.Application;
import android.os.Bundle;
import android.os.Parcelable;
import android.test.ApplicationTestCase;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author Simon
 */
public class UserDataTest extends ApplicationTestCase<Application> {
    UserData data;

    public UserDataTest() {
        super(Application.class);
    }

    public void setUp() throws Exception {
        data = new UserData();
        data.setId(5);
        data.setProvider("email");
        data.setUid("a@b.de");
        data.setName("Tom");
        data.setEmail("a@b.de");
        data.setCreatedAt(new Date());
        data.setAccessToken("224g9f8z2h3d");
    }

    public void tearDown() throws Exception {

    }

    public void testWriteToParcel() throws Exception {
        Bundle b = new Bundle();
        b.putParcelable("userData", data);

        // TODO: check something?
    }

    public void testWriteAndReadFromParcel() throws Exception {
        Bundle b = new Bundle();
        b.putParcelable("userData", data);
        final UserData loadedData = b.getParcelable("userData");
        assertEquals("Data loaded from Bundle does not match original data.", data, loadedData);
    }
}
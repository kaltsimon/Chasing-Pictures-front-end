package de.fu_berlin.cdv.chasingpictures.security;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Base64;

import java.security.SecureRandom;

/**
 * @author Simon
 */
public class SecurePreferencesTest extends ApplicationTestCase<Application> {

    private static final String preferencesName = "de.fu_berlin.cdv.chasingpictures.testPrefs";
    private static final String persistentKey = "myPersistentKey";
    private static final String persistentValue = "myPersistentValue";
    private static final String TAG = "SecurePreferencesTest";
    private SecureRandom secureRandom;

    public SecurePreferencesTest() {
        super(Application.class);
    }

    public void setUp() throws Exception {
        secureRandom = new SecureRandom();
    }

    private SecurePreferences getPreferences() {
        return SecurePreferences.getInstanceFromResources(getContext(), preferencesName);
    }

    public void testEncryptDecrypt() throws Exception {
        String key = randomString(10);
        String value = randomString(10);
        getPreferences().put(key, value);
        assertEquals("Decrypted value does not match.", value, getPreferences().get(key));
        getPreferences().remove(key);
    }

    public void testPersistence() throws Exception {
        getPreferences().put(persistentKey, persistentValue);
        String received = getPreferences().get(persistentKey);
        assertEquals("Decrypted value does not match over multiple instances", persistentValue, received);
    }

    private String randomString(int byteCount) {
        byte[] bytes = new byte[byteCount];
        secureRandom.nextBytes(bytes);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}
package de.fu_berlin.cdv.chasingpictures.security;

import android.content.Context;

import de.fu_berlin.cdv.chasingpictures.R;

/**
 * @author Simon Kalt
 */
public class Access {
    public static SecurePreferences getSecurePreferences(Context context) {
        return SecurePreferences.getInstanceFromResources(context, R.string.security_prefsID);
    }

    public static boolean hasAccess(Context context) {
        String accessToken = getAccessToken(context);
        return accessToken != null && !accessToken.isEmpty();
    }

    public static String getAccessToken(Context context) {
        SecurePreferences prefs = getSecurePreferences(context);
        return prefs.get(context.getString(R.string.security_prefs_accessToken));
    }

    public static void setAccessToken(Context context, String accessToken) {
        SecurePreferences prefs = getSecurePreferences(context);
        prefs.put(context.getString(R.string.security_prefs_accessToken), accessToken);
    }

    /**
     * Salts and hashes the given password.
     */
    public String saltAndHash(Context context, String password) {
        // TODO: actually do something
        return password;
    }
}

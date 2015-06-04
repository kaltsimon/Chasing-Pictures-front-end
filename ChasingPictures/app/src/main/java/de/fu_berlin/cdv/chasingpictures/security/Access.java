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
        SecurePreferences prefs = getSecurePreferences(context);
        String accessToken = prefs.get(context.getString(R.string.security_prefs_accessToken));

        return accessToken != null && !accessToken.isEmpty();
    }

    public static void setAccessToken(Context context, String accessToken) {
        SecurePreferences prefs = getSecurePreferences(context);
        prefs.put(context.getString(R.string.security_prefs_accessToken), accessToken);
    }
}

package de.fu_berlin.cdv.chasingpictures.security;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import de.fu_berlin.cdv.chasingpictures.R;
import xdroid.toaster.Toaster;

/**
 * Utility class to handle API access information.
 * <p/>
 * Using this class, the access headers for the back end API can
 * easily be stored and retrieved.
 *
 * @author Simon Kalt
 */
public final class Access {

    private static SharedPreferences preferences;

    private Access() {

    }

    private static void ensureInstance(Context context) {
        if (Access.preferences == null) {
            Access.preferences = getSharedPreferences(context);
            Access.preferences.edit().commit();
        }
    }

    /**
     * Get the shared preferences for this app.
     *
     * @param context The current context
     * @return The {@link SecurePreferences} object for this app
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString(R.string.security_prefsID), Context.MODE_PRIVATE);
    }


    /**
     * Returns true if all access headers are stored in the application
     * and the access token has not yet expired.
     *
     * @param context The current context
     * @return {@code true} if the stored access information is complete and not expired.
     */
    public static boolean hasAccess(Context context) {
        ensureInstance(context);

        for (Headers header : Headers.values()) {
            String value = header.load(preferences);
            if (value == null || value.isEmpty()) {
                return false;
            }
        }

        String expiryString = Headers.EXPIRY.load(preferences);
        assert expiryString != null; // If it was, we would not be here
        long expirationTime = Long.parseLong(expiryString);
        long currentTime = System.currentTimeMillis() / 1000;
        return expirationTime - currentTime > 0;
    }

    /**
     * Stores the received access information in the application
     *
     * @param context        The current context
     * @param responseEntity The response received from the API request
     */
    public static void setAccess(Context context, ResponseEntity<?> responseEntity) {
        ensureInstance(context);
        Headers.storeAll(preferences, responseEntity);
    }

    /**
     * Retrieves the saved access information and puts it in
     * the given HTTP headers.
     *
     * @param context The current context
     * @param headers The HTTP headers to be sent to the API
     */
    public static void getAccess(Context context, HttpHeaders headers) {
        ensureInstance(context);
        for (Headers header : Headers.values()) {
            header.loadAndSet(preferences, headers);
        }
    }

    /**
     * Deletes all saved access information.
     *
     * @param context The current context
     */
    public static void revokeAccess(Context context) {
        ensureInstance(context);
        Headers.deleteAll(preferences);
    }

    /**
     * Salts and hashes the given password.
     * <strong style="color: red;">Currently does not do anything!</strong>
     *
     * @param context  Current context
     * @param password Password to be hashed
     * @return (eventually) salted and hashed version of the password
     */
    public static String saltAndHash(Context context, String password) {
        // FIXME: actually do something
        return password;
    }

    /**
     * Returns the <em>first</em> header value for the given key.
     *
     * @param responseEntity The request response
     * @param key            The name of the header field
     * @return A string containing the first value for this header field
     */
    public static String getHeader(ResponseEntity<?> responseEntity, String key) {
        List<String> headers = getHeaders(responseEntity, key);
        return headers == null || headers.isEmpty() ? null : headers.get(0);
    }

    /**
     * Returns all available header values for the given key.
     *
     * @param responseEntity The request response
     * @param key            The name of the header field
     * @return A list of strings containing the values for this header field
     */
    public static List<String> getHeaders(ResponseEntity<?> responseEntity, String key) {
        HttpHeaders headers = responseEntity.getHeaders();
        return headers == null ? null : headers.get(key);
    }

    /**
     * Enum for HTTP headers that store access information.
     */
    public enum Headers {
        ACCESS_TOKEN("Access-Token"),
        CLIENT("Client"),
        UID("Uid"),
        EXPIRY("Expiry");

        public final String field;

        Headers(String field) {
            this.field = field;
        }

        /**
         * Load the value of this header from the shared preferences.
         *
         * @param prefs The preferences to use
         * @return The stored value of the header, or {@code null} if it is not stored
         */
        @Nullable
        public String load(SharedPreferences prefs) {
            return prefs.getString(field, null);
        }

        public static void storeAll(SharedPreferences prefs, ResponseEntity<?> responseEntity) {
            SharedPreferences.Editor edit = prefs.edit();
            HttpHeaders headers = responseEntity.getHeaders();

            // Store the headers, but only if they aren't empty
            if (headers != null) {
                for (Headers header : values()) {
                    List<String> strings = headers.get(header.field);
                    if (strings != null && !strings.isEmpty()) {
                        String headerValue = strings.get(0);
                        if (!headerValue.isEmpty())
                            edit.putString(header.field, headerValue);
                    }
                }
            }

            edit.apply();
        }

        /**
         * Gets the stored value of this header and sets it in the given HTTP headers.
         *
         * @param prefs   The preferences to use
         * @param headers The headers to be sent to the API
         */
        public void loadAndSet(SharedPreferences prefs, HttpHeaders headers) {
            headers.set(field, load(prefs));
        }

        /**
         * Deletes the access values stored in this application.
         *
         * @param prefs The preferences to use
         */
        public static void deleteAll(SharedPreferences prefs) {
            SharedPreferences.Editor edit = prefs.edit().clear();
            for (Headers header : values()) {
                edit.remove(header.field);
            }

            boolean commit = edit.commit();
            if (!commit)
                Toaster.toast("Could not delete access information!");
        }
    }
}

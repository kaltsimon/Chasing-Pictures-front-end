package de.fu_berlin.cdv.chasingpictures.security;

import android.content.Context;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import de.fu_berlin.cdv.chasingpictures.R;
import de.fu_berlin.cdv.chasingpictures.api.ApiUtil;

/**
 * Utility class to handle API access information.
 * <p/>
 * Using this class, the access headers for the back end API can
 * easily be stored and retrieved.
 *
 * @author Simon Kalt
 */
public abstract class Access {

    public static SecurePreferences getSecurePreferences(Context context) {
        return SecurePreferences.getInstanceFromResources(context, R.string.security_prefsID);
    }

    /**
     * Returns true if all access headers are stored in the application
     * and the access token has not yet expired.
     *
     * @param context The current context.
     * @return {@code true} if the stored access information is complete and not expired.
     */
    public static boolean hasAccess(Context context) {
        SecurePreferences prefs = getSecurePreferences(context);
        for (Headers header : Headers.values()) {
            String value = header.load(prefs);
            if (value == null || value.isEmpty()) {
                return false;
            }
        }

        long expirationTime = Long.parseLong(Headers.EXPIRY.load(prefs));
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
        SecurePreferences prefs = getSecurePreferences(context);
        for (Headers header : Headers.values()) {
            header.store(prefs, responseEntity);
        }
    }

    /**
     * Retrieves the saved access information and puts it in
     * the given HTTP headers.
     *
     * @param context The current context
     * @param headers The HTTP headers to be sent to the API
     */
    public static void getAccess(Context context, HttpHeaders headers) {
        SecurePreferences prefs = getSecurePreferences(context);
        for (Headers header : Headers.values()) {
            header.loadAndSet(prefs, headers);
        }
    }

    /**
     * Deletes all saved access information.
     *
     * @param context The current context
     */
    public static void revokeAccess(Context context) {
        SecurePreferences prefs = getSecurePreferences(context);
        for (Headers header : Headers.values()) {
            header.delete(prefs);
        }
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
         * Load the value of this header from the secure preferences.
         *
         * @param prefs The preferences to use
         * @return The stored value of the header, or {@code null} if it is not stored
         */
        public String load(SecurePreferences prefs) {
            return prefs.get(field);
        }

        /**
         * Store the value of this header received in the given response entity
         * in the given secure preferences.
         *
         * @param prefs          The preferences to use
         * @param responseEntity The entity received from the API
         */
        public void store(SecurePreferences prefs, ResponseEntity<?> responseEntity) {
            prefs.put(field, ApiUtil.getHeader(responseEntity, field));
        }

        /**
         * Gets the stored value of this header and sets it in the given HTTP headers.
         *
         * @param prefs   The preferences to use
         * @param headers The headers to be sent to the API
         */
        public void loadAndSet(SecurePreferences prefs, HttpHeaders headers) {
            headers.set(field, load(prefs));
        }

        /**
         * Deletes the value stored in the application for this header.
         *
         * @param prefs The preferences to use
         */
        public void delete(SecurePreferences prefs) {
            prefs.remove(field);
        }
    }
}

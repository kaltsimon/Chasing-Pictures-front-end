package de.fu_berlin.cdv.chasingpictures.util;

import android.content.Context;
import android.support.annotation.StringRes;

import xdroid.toaster.Toaster;

/**
 * Class that provides utility methods.
 *
 * @author Simon Kalt
 */
public class Utilities {
    /**
     * @param context      The current context
     * @param formatString The message to be displayed
     * @param args         Arguments to be formatted into the string
     * @deprecated Use {@link #showError(Context, int, Object...)} if possible.
     */
    @Deprecated
    public static void showError(Context context, String formatString, Object... args) {
        String message = args.length == 0 ? formatString : String.format(formatString, args);
        Toaster.toast(message);
    }

    /**
     * Displays an error to the user.
     * Currently does the same thing as {@link #showToast(Context, int, Object...)} but
     * could later be changed to look differently.
     *
     * @param context The current context
     * @param resID   A string resource to be displayed
     * @param args    Arguments to be formatted into the string
     */
    public static void showError(Context context, @StringRes int resID, Object... args) {
        showToast(context, resID, args);
    }

    /**
     * Displays a message to the user.
     *
     * @param context The current context
     * @param resID   A string resource to be displayed
     * @param args    Arguments to be formatted into the string
     */
    public static void showToast(Context context, @StringRes final int resID, final Object... args) {
        String message = context.getString(resID);
        message = args.length == 0 ? message : String.format(message, args);
        Toaster.toast(message);
    }
}

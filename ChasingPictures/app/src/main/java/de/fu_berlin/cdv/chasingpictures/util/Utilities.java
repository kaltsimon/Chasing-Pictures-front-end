package de.fu_berlin.cdv.chasingpictures.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Class that provides utility methods.
 *
 * @author Simon Kalt
 */
public class Utilities {

    /**
     *
     * @param context The current context
     * @param message The message to be displayed
     * @deprecated Use {@link #showError(Context, int)} if possible.
     */
    @Deprecated
    public static void showError(Context context, String message) {
        Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    /**
     * Displays an error to the user.
     * Currently does the same thing as {@link #showToast(Context, int)} but
     * could later be changed to look differently.
     *
     * @param context The current context
     * @param resID A string resource to be displayed
     */
    public static void showError(Context context, @StringRes int resID) {
        Toast.makeText(
                context,
                resID,
                Toast.LENGTH_SHORT
        ).show();
    }

    /**
     * Displays a message to the user.
     *
     * @param context The current context
     * @param resID A string resource to be displayed
     */
    public static void showToast(Context context, @StringRes int resID) {
        Toast.makeText(
                context,
                resID,
                Toast.LENGTH_SHORT
        ).show();
    }
}

package de.fu_berlin.cdv.chasingpictures.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
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
     * @param formatString The message to be displayed
     * @param args Arguments to be formatted into the string
     * @deprecated Use {@link #showError(Context, int, Object...)} if possible.
     */
    @Deprecated
    public static void showError(Context context, String formatString, Object... args) {
        showError(context, formatString, null, args);
    }

    /**
     *
     * @param context The current context
     * @param formatString The message to be displayed
     * @param handler The handler to run Toast.show on
     *@param args Arguments to be formatted into the string  @deprecated Use {@link #showError(Context, int, Object...)} if possible.
     */
    @Deprecated
    public static void showError(Context context, String formatString, @Nullable Handler handler, Object... args) {
        @SuppressLint("ShowToast")
        final Toast toast = Toast.makeText(
                context,
                String.format(formatString, args),
                Toast.LENGTH_SHORT
        );

        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        } else {
            toast.show();
        }
    }

    /**
     * Displays an error to the user.
     * Currently does the same thing as {@link #showToast(Context, int, Object...)} but
     * could later be changed to look differently.
     * @param context The current context
     * @param resID A string resource to be displayed
     * @param args Arguments to be formatted into the string
     */
    public static void showError(Context context, @StringRes int resID, Object... args) {
        showError(context, resID, null, args);
    }

    /**
     * Displays an error to the user.
     * Currently does the same thing as {@link #showToast(Context, int, Object...)} but
     * could later be changed to look differently.
     * @param context The current context
     * @param resID A string resource to be displayed
     * @param handler
     * @param args Arguments to be formatted into the string
     */
    public static void showError(Context context, @StringRes int resID, @Nullable Handler handler, Object... args) {
        showToast(context, resID, handler, args);
    }

    /**
     * Displays a message to the user.
     * @param context The current context
     * @param resID A string resource to be displayed
     * @param args Arguments to be formatted into the string
     */
    public static void showToast(Context context, @StringRes final int resID, final Object... args) {
        showToast(context, resID, null, args);
    }

    /**
     * Displays a message to the user.
     * @param context The current context
     * @param resID A string resource to be displayed
     * @param handler
     * @param args Arguments to be formatted into the string
     */
    public static void showToast(Context context, @StringRes final int resID, @Nullable Handler handler, final Object... args) {
        @SuppressLint("ShowToast")
        final Toast toast = Toast.makeText(
                context,
                String.format(context.getString(resID), args),
                Toast.LENGTH_SHORT
        );

        // Display on main looper.
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        } else {
            toast.show();
        }
    }
}

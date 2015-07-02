package de.fu_berlin.cdv.chasingpictures.util

import android.content.Context
import android.support.annotation.StringRes

import xdroid.toaster.Toaster
import kotlin.platform.platformStatic

/**
 * Class that provides utility methods.

 * @author Simon Kalt
 */
public object Utilities {
    /**
     * @param context      The current context
     * *
     * @param formatString The message to be displayed
     * *
     * @param args         Arguments to be formatted into the string
     * *
     */
    deprecated("Use {@link #showError(Context, int, Object...)} if possible.", ReplaceWith("Toaster.toast(context, args.length == 0 ? formatString : String.format(formatString, args))"))
    platformStatic public fun showError(context: Context, formatString: String, vararg args: Any) {
        val message = if (args.size() == 0) formatString else formatString.format(*args)
        Toaster.toast(message)
    }

    /**
     * Displays an error to the user.
     * Currently does the same thing as [.showToast] but
     * could later be changed to look differently.

     * @param context The current context
     * *
     * @param resID   A string resource to be displayed
     * *
     * @param args    Arguments to be formatted into the string
     */
    platformStatic public fun showError(context: Context, StringRes resID: Int, vararg args: Any) {
        showToast(context, resID, *args)
    }

    /**
     * Displays a message to the user.

     * @param context The current context
     * *
     * @param resID   A string resource to be displayed
     * *
     * @param args    Arguments to be formatted into the string
     */
    platformStatic public fun showToast(context: Context, StringRes resID: Int, vararg args: Any) {
        var message = context.getString(resID)
        message = if (args.size() == 0) message else message.format(*args)
        Toaster.toast(message)
    }
}

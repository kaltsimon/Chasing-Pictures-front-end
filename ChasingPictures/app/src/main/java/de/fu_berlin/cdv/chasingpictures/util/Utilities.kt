package de.fu_berlin.cdv.chasingpictures.util

import android.content.Context
import android.support.annotation.StringRes
import android.text.Html
import android.text.Spannable
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.text.util.Linkify
import android.widget.TextView

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

    /**
     * Remove the underlines from the links in a text spannable.

     * @param text A spannable containing the text
     * *
     * @return The same spannable, edited in place
     */
    platformStatic public fun removeURLUnderlines(text: Spannable): Spannable {
        for (u in text.getSpans(0, text.length(), javaClass<URLSpan>())) {
            text.setSpan(object : UnderlineSpan() {
                override fun updateDrawState(tp: TextPaint) {
                    super.updateDrawState(tp)
                    tp.setUnderlineText(false)
                }
            }, text.getSpanStart(u), text.getSpanEnd(u), 0)
        }
        return text
    }


    /**
     * Parse the given String as HTML.
     *
     *
     * The links will be underlined.

     * @param text The text to parse
     */
    platformStatic public fun getTextAsHtml(text: String): Spannable {
        return getTextAsHtml(text, true)
    }

    /**
     * Parse the given String as HTML.

     * @param text       The text to parse
     * *
     * @param underlines Whether or not the links should be underlined
     */
    platformStatic public fun getTextAsHtml(text: String, underlines: Boolean): Spannable {
        var textSpan = Html.fromHtml(text) as Spannable
        if (!underlines)
            textSpan = removeURLUnderlines(textSpan)
        return textSpan
    }

    private fun setLinkifiedText(textView: TextView, textWithLinks: Spannable) {
        textView.setText(textWithLinks)
        linkifyTextView(textView)
    }

    /**
     * Linkify the text inside the given [TextView].
     * @param textView The [TextView] to linkify
     */
    platformStatic public fun linkifyTextView(textView: TextView) {
        Linkify.addLinks(textView, Linkify.ALL)
        textView.setMovementMethod(LinkMovementMethod.getInstance())
    }

    /**
     * Set the text for the given [TextView] to the given HTML String and turn contained links
     * (either just URLs or HTML &lt;a&gt elements) into clickable ones.

     * @param textView   The text view for which to set the text
     * *
     * @param text       The text to parse
     * *
     * @param underlines Whether or not the links should be underlined
     */
    platformStatic public fun setLinkifiedText(textView: TextView, text: String, underlines: Boolean) {
        val textSpan = getTextAsHtml(text, underlines)
        setLinkifiedText(textView, textSpan)
    }

    /**
     * Set the text for the given [TextView] to the given HTML String and turn contained links
     * (either just URLs or HTML &lt;a&gt elements) into clickable ones.
     *
     *
     * The links will be underlined.

     * @param textView The text view for which to set the text
     * *
     * @param text     The text to parse
     */
    platformStatic public fun setLinkifiedText(textView: TextView, text: String) {
        setLinkifiedText(textView, text, true)
    }

    /**
     * Set the text for the given [TextView] to the given resource and turn contained links
     * (either just URLs or HTML &lt;a&gt elements) into clickable ones.
     *
     *
     * The links will be underlined.

     * @param context   The current context
     * *
     * @param textView  The text view for which to set the text
     * *
     * @param textResId The id of the string resource to load
     */
    platformStatic public fun setLinkifiedText(context: Context, textView: TextView, StringRes textResId: Int) {
        setLinkifiedText(context, textView, textResId, true)
    }

    /**
     * Set the text for the given [TextView] to the given resource and turn contained links
     * (either just URLs or HTML &lt;a&gt elements) into clickable ones.

     * @param context    The current context
     * *
     * @param textView   The text view for which to set the text
     * *
     * @param textResId  The id of the string resource to load
     * *
     * @param underlines Whether or not links should be underlined
     */
    platformStatic public fun setLinkifiedText(context: Context, textView: TextView, StringRes textResId: Int, underlines: Boolean) {
        setLinkifiedText(textView, context.getString(textResId), underlines)
    }
}

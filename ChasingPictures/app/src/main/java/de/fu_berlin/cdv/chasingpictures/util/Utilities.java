package de.fu_berlin.cdv.chasingpictures.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.widget.TextView;

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

    /**
     * Remove the underlines from the links in a text spannable.
     *
     * @param text A spannable containing the text
     * @return The same spannable, edited in place
     */
    public static Spannable removeURLUnderlines(Spannable text) {
        for (URLSpan u : text.getSpans(0, text.length(), URLSpan.class)) {
            text.setSpan(new UnderlineSpan() {
                @Override
                public void updateDrawState(@NonNull TextPaint tp) {
                    super.updateDrawState(tp);
                    tp.setUnderlineText(false);
                }
            }, text.getSpanStart(u), text.getSpanEnd(u), 0);
        }
        return text;
    }


    /**
     * Parse the given String as HTML.
     * <p/>
     * The links will be underlined.
     *
     * @param text The text to parse
     */
    public static Spannable getTextAsHtml(String text) {
        return getTextAsHtml(text, true);
    }

    /**
     * Parse the given String as HTML.
     *
     * @param text       The text to parse
     * @param underlines Whether or not the links should be underlined
     */
    public static Spannable getTextAsHtml(String text, boolean underlines) {
        Spannable textSpan = (Spannable) Html.fromHtml(text);
        if (!underlines)
            textSpan = removeURLUnderlines(textSpan);
        return textSpan;
    }

    private static void setLinkifiedText(TextView textView, Spannable textWithLinks) {
        textView.setText(textWithLinks);
        Linkify.addLinks(textView, Linkify.ALL);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Set the text for the given {@link TextView} to the given HTML String and turn contained links
     * (either just URLs or HTML &lt;a&gt elements) into clickable ones.
     *
     * @param textView   The text view for which to set the text
     * @param text       The text to parse
     * @param underlines Whether or not the links should be underlined
     */
    public static void setLinkifiedText(TextView textView, String text, boolean underlines) {
        Spannable textSpan = getTextAsHtml(text, underlines);
        setLinkifiedText(textView, textSpan);
    }

    /**
     * Set the text for the given {@link TextView} to the given HTML String and turn contained links
     * (either just URLs or HTML &lt;a&gt elements) into clickable ones.
     * <p/>
     * The links will be underlined.
     *
     * @param textView The text view for which to set the text
     * @param text     The text to parse
     */
    public static void setLinkifiedText(TextView textView, String text) {
        setLinkifiedText(textView, text, true);
    }

    /**
     * Set the text for the given {@link TextView} to the given resource and turn contained links
     * (either just URLs or HTML &lt;a&gt elements) into clickable ones.
     * <p/>
     * The links will be underlined.
     *
     * @param context   The current context
     * @param textView  The text view for which to set the text
     * @param textResId The id of the string resource to load
     */
    public static void setLinkifiedText(Context context, TextView textView, @StringRes int textResId) {
        setLinkifiedText(context, textView, textResId, true);
    }

    /**
     * Set the text for the given {@link TextView} to the given resource and turn contained links
     * (either just URLs or HTML &lt;a&gt elements) into clickable ones.
     *
     * @param context    The current context
     * @param textView   The text view for which to set the text
     * @param textResId  The id of the string resource to load
     * @param underlines Whether or not links should be underlined
     */
    public static void setLinkifiedText(Context context, TextView textView, @StringRes int textResId, boolean underlines) {
        setLinkifiedText(textView, context.getString(textResId), underlines);
    }
}

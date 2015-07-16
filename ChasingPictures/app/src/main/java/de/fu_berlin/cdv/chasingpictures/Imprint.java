package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.TextView;

import de.fu_berlin.cdv.chasingpictures.util.Utilities;

/**
 * Activity that displays the imprint.
 */
public class Imprint extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imprint);

        int[] textViews = {
                R.id.imprint_text_start,
                R.id.imprint_text_license,
                R.id.imprint_text_libraries,
                R.id.imprint_text_photos_stadtmuseum,
                R.id.imprint_text_photos_user
        };

        // Parse the HTML content of each provided TextView and set its contents to the result
        TextView text;
        for (@IdRes int textViewResId : textViews) {
            text = (TextView) findViewById(textViewResId);
            Utilities.setLinkifiedText(text, String.valueOf(text.getText()));
        }

        // Get the list of libraries
        String[] libraries = getResources().getStringArray(R.array.imprint_libraries);

        // Concat the texts for the individual libraries into a list
        String listBulletPoint = "&raquo; ";
        StringBuilder content = new StringBuilder(libraries.length * 150);
        for (String library : libraries) {
            content.append(listBulletPoint)
                    .append(library)
                    .append("<br/>");
        }

        // Add the resulting list to the imprint
        text = (TextView) findViewById(R.id.imprint_list_libraries);
        Utilities.setLinkifiedText(text, content.toString());
    }

}

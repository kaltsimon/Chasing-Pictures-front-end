package de.fu_berlin.cdv.chasingpictures;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        TextView imprintText = (TextView) findViewById(R.id.imprintText);
        Utilities.setLinkifiedText(this, imprintText, R.string.imprint_text);
    }

}

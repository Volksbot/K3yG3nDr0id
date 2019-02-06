package ch3f.la.st3f.k3yg3ndr0id;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Activity zur Anzeige der HTML-Onlinehilfe. Oberfläche: layout/activity_help.xml
 * inspired by w.lang WS 15/16
 */

public class Help extends AppCompatActivity {

    private static final String MIMETYPE = "text/html";
    private static final String ENCODING = "UTF-8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("Hilfe");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final WebView view = (WebView) findViewById( R.id.webview );

        InputStream is = getResources().openRawResource( R.raw.hilfe );

        try {
            if (( is != null ) && is.available() > 0) {
                final byte[] bytes = new byte[is.available()];
                is.read( bytes );
                view.loadDataWithBaseURL( null, new String( bytes ), MIMETYPE,
                        ENCODING, null );

            }
        } catch ( IOException ex ) {
            ex.printStackTrace();
        }

        view.bringToFront();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.mnuExit) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

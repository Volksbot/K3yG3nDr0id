package ch3f.la.st3f.k3yg3ndr0id;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Die Activity ShowKonten dient der Übersicht über alle bereits angelegten Dienste.
 * Desweiteren können aus dieser Activity heraus die Activity EditKonto zur Bearbeitung eines Dienstes
 * aufgerufen und die Activity NewKonto zum Anlegen eines neuen Dienstes aufgerufen werden.
 */

public class ShowKonten extends AppCompatActivity {

    /**
     * Das File indem eine Liste der erfassten Dienste ablegt wird (durch Komma separiert)
     */
    File fileS;
    /**
     * Hilfvariable, welche den Pfad des Gerätespeichers holt
     */
    String path;
    /**
     * Enthält den Accountnamen zur Übergabebeschreibung an die Activity EditKonto
     */
    final static String ACCOUNTBEZEICHNUNG = "account";
    /**
     * ListView
     */
    ListView listview;
    /**
     * ArrayAdapter der Listview
     */
    ArrayAdapter<String> adapter;
    /**
     * String Array für Dienste
     */
    String[] dienst_array;
    /**
     * Hilfvariable - Liest den Inhalt der Zeile aus der eingelesenen Datei
     */
    String Zeile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_konten);
        /**
         * Titel der Activity festlegen
         */
        setTitle("Kontenübersicht:");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Die Methode zum Befüllen der ListView wird aufgerufen
         * und mit den Einträgen aus der Dienste_PWGen.txt befüllt.
         */
        ListViewfuellen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_konten, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        /**
         * Beendet die Activity, zurück zur MainActivity
         */
        switch(item.getItemId()){
            case R.id.actExitgen:
            {
                Toast.makeText(this, "Abbruch!", Toast.LENGTH_LONG).show();
                finish();
                break;
            }
            case R.id.actNew:
                /**
                 * Ruft die Methode clickAddService auf
                  */
                clickAddService();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Ruft mittels eines Intents die Activity NewKonto auf, mit deren Hilfe ein neuer Dienst hinzugefügt werden kann
     */
void clickAddService() {
    startActivity(new Intent(this, NewKonto.class));
}

    /**
     * Methode zur Befüllung der ListView, dient der Übersicht über bereits vorhandene Dienste
     *
     */
public void ListViewfuellen(){
    try{
        path = Environment.getExternalStorageDirectory().getPath() + "/Dienste_PWGen.txt";
        fileS = new File(path);
        FileReader reader = new FileReader(fileS);
        BufferedReader eingabedatei = new BufferedReader(reader);
        //Die Zeile der Datei wird ausgelesen
        Zeile = eingabedatei.readLine();
        reader.close();
    }
    catch(Exception ex)
    {
        Log.d("Fehler!!", ex.getMessage());
    }
    /**
     * Dient der Initialisierung von ListView
     */
    listview = (ListView) findViewById(R.id.listview);
    //Trennzeichen festlegen für die einzelnen Dienste in Datei
    char zeichen = ',';
    //zählt wie viele Kommas in der Datei vorkommen und legt größe des String Array fest
    int helper_count=1;
    for (int i = 0; i < Zeile.length(); i++) {
        if (Zeile.charAt(i) == zeichen) helper_count++;}
    /**
     * StringArray für die Substrings
     */
    //Array wird mit Größe des helper_count = Einträge initialisiert/eröffnet
    dienst_array = new String[helper_count];
    //Hilfs integer Variable für die Indexierung des Arrays innerhalb der Schleife (wird mit -1 initialisiert um
    //Schleifendurchlauf zu ermöglichen
    int h=-1;
    //String wird eingelesen bis Komma und dann wird innerhalb der Schleife der Inhalt (String retval) in das Array geschrieben
    for (String retval: Zeile.split(",")){
        h++;
        //Der Substring wird dem Arrayeintrag übergeben
        dienst_array[h]=retval;
    }
    // ArrayAdapter um den Spinner mit Accountbezeichnungen zu fuellen
    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dienst_array);
    listview.setAdapter(adapter);

    /**
     * Auf Benutzereingabe reagieren:
     * Per Mausklick auf einen vorhandenen Dienst wird mittels AdapterView setOnItemClickListener
     * die Activity EditKonto geöffnet, welche der Bearbeitung vorhandener Dienste dient.
     */
    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        //Die Accountbezeichnung wird mittels Intent an die Activity EditKonto übergeben
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Intent intent = new Intent(view.getContext(),
                    EditKonto.class);
            String account = listview.getAdapter().getItem(position).toString();
            intent.putExtra(ACCOUNTBEZEICHNUNG, account);
            view.getContext().startActivity(intent);
        }
    });
}

    /**
     * Bei Rückkehr aus Activity NewKonto wird ListView neu befüllt um neuen Eintrag anzuzeigen
     */
 protected void onResume(){
         super.onResume();
     ListViewfuellen();
 }
}
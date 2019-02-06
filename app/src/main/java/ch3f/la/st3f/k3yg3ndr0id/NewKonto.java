package ch3f.la.st3f.k3yg3ndr0id;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Die Activity NewKonto dient der Anlage eines neuen Dienstes und prüft,
 * ob der neu anzulegende Dienst bereits vorhanden ist.
 */

public class NewKonto extends AppCompatActivity {

    /**
     * Hilfvariable - Liest den Inhalt der Zeile aus der eingelesenen Datei
     */
    String Zeile;
    /**
     * Hilfvariable, welche den Pfad des Gerätespeichers holt
     */
    String path;
    /**
     * Das File für die Accountdaten (URL, Kennung)
     */
    File fileKonto;
    /**
     * Das File indem eine Liste der erfassten Dienste ablegt wird (durch Komma separiert)
     */
    File fileS;
    /**
     * Hilfvariable für den Prüfschritt ob File bereits existiert
     */
    boolean File_vorhanden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_konto);
        /**
         * Titel der Activity festlegen
          */
        setTitle("Konto anlegen:");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_konto, menu);
        return true;
    }

    /**
     * Methode checkVorhandensein() prüft, ob es bereits eine Datei im angegeben Pfad gibt,
     * welche den gleichen Namen des eingegebenen Dienstes trägt.
     * @param sDienst
     * @return
     */

    public boolean checkVorhandensein(String sDienst) {
        try {
            path = Environment.getExternalStorageDirectory().getPath();
            fileKonto = new File(path + "/" + sDienst + ".txt");

            if(fileKonto.exists())
            {
                return true;
            }
            else return false;

        } catch(Exception ex)
        {
            Log.d("Fehler!!", ex.getMessage());
        }
        return false; //Funktion benötigt zwingend einen Return-Wert am Ende, hier wird man aber nie ankommen
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //Beendet die Acivity ohne Speichern
        switch(item.getItemId()){
            case R.id.actExitgen:
            {
                Toast.makeText(this, "Abbruch!", Toast.LENGTH_LONG).show();
                finish();
                break;
            }

            case R.id.actSaveNew:
            {
                //neue Eingabedaten aus den Editfeldern in Datei zurückschreiben
                try{
                    //Inhalt des Edit Text Feld an String Variable übergeben
                    final EditText diensthelp = (EditText) findViewById(R.id.newDienst);
                    String sDienst = diensthelp.getText().toString();

                    final EditText kennunghelp = (EditText) findViewById(R.id.newKennung);
                    String sKennung = kennunghelp.getText().toString();

                    final EditText URLhelp = (EditText) findViewById(R.id.newURL);
                    String sURL = URLhelp.getText().toString();

                    if (checkVorhandensein(sDienst)==true)
                    {
                        File_vorhanden=true;
                        Toast.makeText(this, "Abbruch! Der Dienst "+ sDienst+" ist bereits vorhanden.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else{
                        path = Environment.getExternalStorageDirectory().getPath() + "/" + sDienst + ".txt";
                        fileKonto = new File(path);
                        FileOutputStream Services = new FileOutputStream(fileKonto);
                        String neuerInhalt = (sKennung + "," + sURL);
                        Services.write(neuerInhalt.getBytes());
                        Services.close();
                        //Toast.makeText(this, "Erfolg", Toast.LENGTH_SHORT).show(); //Debugzeile
                    }
                }
                catch(IOException ex){
                    Log.d("Fehler", ex.getMessage());
                }

                // Die Datei Dienste anpassen bzw füge Dienst an
                //zunächst Datei einlesen...
                if (File_vorhanden==false) {
                    try {
                        path = Environment.getExternalStorageDirectory().getPath() + "/Dienste_PWGen.txt";
                        fileS = new File(path);
                        FileReader reader = new FileReader(fileS);
                        BufferedReader eingabedatei = new BufferedReader(reader);
                        Zeile = eingabedatei.readLine();
                        reader.close();
                        //   Toast.makeText(this, Zeile, Toast.LENGTH_SHORT).show();   //Debug
                    } catch (Exception ex) {
                        Log.d("Fehler!!", ex.getMessage());
                    }

                    //...dann Datei mit neuem Inhalt neu schreiben
                    try {
                        path = Environment.getExternalStorageDirectory().getPath() + "/Dienste_PWGen.txt";
                        fileS = new File(path);
                        FileOutputStream Services = new FileOutputStream(fileS);
                        //Inhalt des Edit Text Feld an String Variable übergeben
                        final EditText diensthelp = (EditText) findViewById(R.id.newDienst);
                        String sDienst = diensthelp.getText().toString();

                        String defaultInhalt = Zeile + "," + sDienst;
                        Services.write(defaultInhalt.getBytes());
                        Services.close();
                    } catch (IOException ex) {
                        Log.d("Fehler", ex.getMessage());
                    }
                }
                finish();

            }
        }
        return super.onOptionsItemSelected(item);
    }
}

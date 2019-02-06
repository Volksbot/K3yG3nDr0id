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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Die Activity EditKonto dient der Bearbeitung bereits angelegter Dienste und bietet bei
 * Bedarf die Möglichkeit des Löschens eines bereits angelegten Dienstes
 */

public class EditKonto extends AppCompatActivity {


    /**
     * Das File indem eine Liste der erfassten Dienste ablegt ist (durch Komma separiert)
     */
    File fileS;
    /**
     * Hilfsvariable um die Dateiangabe zu vervollständigen
     */
    private final static String sServices = "/Dienste_PWGen.txt";
    /**
     * Das File für die Accountdaten (URL, Kennung)
     */
    File fileKonto;
    /**
     * Hilfvariable, welche später den Pfad aus dem Gerätespeicher holt
     */
    String path;
    /**
     * Hilfsvariable für Accountname bzw. den Dienstnamen
     */
    String accountname;
    /**
     * Hilfvariable - Liest den Inhalt der Zeile aus der eingelesenen Datei
     */
    String Zeile;
    /**
     * String Array für SubStrings
     */
    String[] accountdaten;
    /**
     * String Array für Dienste
     */
    String[] dienst_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_konto);
        setTitle("Konto editieren:");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bundle mit den uebertragenen Werten aus vorheriger Activity erhalten
        final Bundle extras = getIntent().getExtras();

            // Bezeichnung fuer den Account übernehmen und in EditTextfeld schreiben
            accountname = extras.getString(ShowKonten.ACCOUNTBEZEICHNUNG);
            ((EditText) findViewById(R.id.editDienst)).setText(accountname);
        // Datei "Servicename".txt wird geöffnet und der Inhalt ausgelesen
        try{
            path = Environment.getExternalStorageDirectory().getPath() + "/" + accountname + ".txt";
            fileKonto = new File(path);
            FileReader reader = new FileReader(fileKonto);
            BufferedReader eingabedatei = new BufferedReader(reader);
            Zeile = eingabedatei.readLine();
            reader.close();
          //  Toast.makeText(this, Zeile, Toast.LENGTH_SHORT).show();   //Debugzeile
        }
        catch(Exception ex)
        {
            Log.d("Fehler!!", ex.getMessage());
        }

        // Accountdaten aus Datei lesen und in edit Feld schreiben
        //Es gibt immer nur zwei Einträge in der Accountdatei, deshalb festgelegte Größe des Arrays
        accountdaten = new String[2];
        int h=-1;
        for (String retval: Zeile.split(",")){
            h++;
            //Der Substring wird dem Arrayeintrag übergeben
            accountdaten[h]=retval;
        }
        //Die EditText Felder werden mit den Einträgen aus dem Array befüllt
        ((EditText) findViewById(R.id.editKennung)).setText(accountdaten[0]);
        ((EditText) findViewById(R.id.editURL)).setText(accountdaten[1]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_konto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        switch(item.getItemId()){
            case R.id.actExitgen:
            {
                Toast.makeText(this, "Abbruch!", Toast.LENGTH_LONG).show();
                finish();
                break;
            }

            case R.id.actSave:
            {
                //geänderte Eingabedaten aus den Editfeldern in Datei zurückschreiben
                try{
                    path = Environment.getExternalStorageDirectory().getPath() + "/" + accountname + ".txt";
                    fileKonto = new File(path);
                    FileOutputStream Services = new FileOutputStream(fileKonto);
                    //String wird neu zusammengebaut um den neuen Inhalt in die Datei zurück zu schreiben
                    String neuerInhalt = ( (EditText) findViewById(R.id.editKennung)).getText()+","+( (EditText) findViewById(R.id.editURL)).getText();
                    Services.write(neuerInhalt.getBytes());
                    Services.close();
                }
                catch(IOException ex){
                    Log.d("Fehler", ex.getMessage());
                }
                //Beendet die Activity
                finish();
            }
            case R.id.actDelete:
            {
                //Löschfunktion für Dateien
                try {
                    //Zunächst das Einlesen der "originalen Dienste-Datei"
                    path = Environment.getExternalStorageDirectory().getPath();
                    fileS = new File(path + sServices);
                    //die Zeile der Dienste holen und in String "Zeile" schreiben
                    getstring();
                    //Aufsplitten der Dienste in das Array durch Beachtung des Trennzeichens
                    char zeichen = ',';
                    int helper_count=1;
                    for (int i = 0; i < Zeile.length(); i++) {
                        if (Zeile.charAt(i) == zeichen) helper_count++;}
                    /**
                     * StringArray für die Substrings
                     */
                    dienst_array = new String[helper_count];
                    int h=-1;
                    for (String retval: Zeile.split(",")){
                        h++;
                        //Der Substring wird dem Arrayeintrag übergeben
                        dienst_array[h]=retval;
                    }
                    h++; // Hilfvariable für Anzahl der Dienste inkrementieren, um Weiterverarbeitung möglich zu machen

                    //Toast.makeText(this, Integer.toString(h), Toast.LENGTH_SHORT).show(); //Debugzeile

                    //Hilfsvariable mit der innerhalb der Suchschleife das Array indexiert wird
                    int accountindex;
                    //Suchen nach dem String des Accountnamens
                    for (accountindex=0;accountindex<h;accountindex++)
                    {
                        if (dienst_array[accountindex].equals(accountname)) break;
                    }

                    //neuer String für das Zurückschreiben -> ohne den Eintrag
                    String nachLoesch ="";
                    //
                    for(int i = 0; i<h;i++) {
                        //ein Komma einfügen wenn es nicht der erste Eintrag ist ODER wenn es der Accountindex ist
                        if(i!=0) {
                            if (i!=accountindex) nachLoesch=nachLoesch+",";
                        }
                        //befüllen mit den Dienstnamen AUSSER der von dem Eintrag selbst(accountindex)
                        if(i!=accountindex) nachLoesch = nachLoesch+dienst_array[i];
                    }
                    try{
                        path = Environment.getExternalStorageDirectory().getPath() + "/Dienste_PWGen.txt";
                        fileS = new File(path);
                        FileOutputStream Services = new FileOutputStream(fileS);
                        //neuer String wird geschrieben (ohne den zu löschenden Eintrag)
                        Services.write(nachLoesch.getBytes());
                        Services.close();
                    }
                    catch(IOException ex){
                        Log.d("Fehler", ex.getMessage());
                    }
                    //abschließend die Kontodatei noch löschen
                    if(fileKonto.exists()) fileKonto.delete();
                    //Activity wird beendet
                    finish();
                }
                catch(Exception ex)
                {
                    Log.d("Fehler!!", ex.getMessage());
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void getstring(){
        try {
            FileInputStream in = new FileInputStream(path + sServices);
            InputStreamReader tmp=new InputStreamReader(in);
            BufferedReader br=new BufferedReader(tmp);
            String str;
            StringBuilder buf=new StringBuilder();
            while ((str = br.readLine()) != null) {
                buf.append(str);
            }
            Zeile = buf.toString();
            in.close();}
        catch (java.io.FileNotFoundException e) {}
        catch (Throwable t) {Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();}
        }

}


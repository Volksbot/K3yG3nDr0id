package ch3f.la.st3f.k3yg3ndr0id;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Die Main Activity des Programms dient der Initialisierung der Dateien
 * und gleichzeitig zur Navigation innerhalb der App
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Hilfsvariable String des Dateipfades für die Datei Dienste_PWGen.txt
     */
 private final static String sServices = "/Dienste_PWGen.txt";
    /**
     * Hilfsvariable String des Dateipfades für die erste Dummy Datei
     */
 private final static String sDummy = "/Facebook.txt";
    /**
     * String um den Inhalt aus der Datei Dienste_PWGen.txt auszulesen
     */
 String dienste = "";
    /**
     * Hilfsvariable für die Datei Dienste_PWGen.txt
     */
    File fileS;
    /**
     *    Hilfsvariable für die Datei der Services
      */
     File fileD;
    /**
     * String des Dateipfades
     */
    String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * Titel der Activity wird festgelegt
         */
        setTitle("K3yG3nDr0id");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        path = Environment.getExternalStorageDirectory().getPath();
        fileS = new File(path + sServices);

        /**
         * Methode zur Prüfung der Dateien aufrufen
         */
        checkDatei();
    }

    /**
     * Methode checkDatei() ruft die Methode getstring() auf
     * Anschließend wird geprüft ob das File Dienste_PWGen.txt existiert und
     * ob der String dienste leer ist
     */

        public void checkDatei() {

            if(fileS.exists()){

                /**
                * Methode getstring() wird aufgerufen, liest den String aus der Dienste_PWGen.txt
                */
             getstring();

            //Toast.makeText(this, "ES GIBT MICH SCHON!!", Toast.LENGTH_LONG).show(); //Debugzeile
            }

            try {
                if(!fileS.exists()||dienste.equals("")) {
                    fileS.createNewFile();
/**
 * Die Methode writedummy() wird aufgerufen, falls es die Datei nicht gibt oder
 * String "dienste" leer ist
  */
                    writedummy();
                }} catch (IOException e) {
                e.printStackTrace();}
    }

        // Datei mit einem Dummy Dienst anlegen, in dem Kennung und URL abgelegt werden

    /**
     * Methode writedummy() legt einen Dummy-Eintrag in der Dienste_PWGen.txt mit dem
     * Namen Facebook an, da diese Datei nicht leer sein darf.
     * Anschließend wird ein Dummy-Servicefile mit der zugehörigen Kennung und der URL angelegt
     */

    public void writedummy(){
        /**
         * die Datei Dienste_PWGen.txt wird mit dem Service "Facebook" initialisiert
          */
        try {
            FileOutputStream Services = new FileOutputStream(fileS);
            String defaultInhalt = "Facebook";
            Services.write(defaultInhalt.getBytes());
            Services.close();
        } catch (IOException ex) {
            Log.d("Fehler", ex.getMessage());
        }

        /**
         * Es wird eine Datei mit der Kennung und URL des Dienstes Facebook angelegt.
         */
        try{
            path = Environment.getExternalStorageDirectory().getPath();
            fileD = new File(path + sDummy);
            FileOutputStream Services = new FileOutputStream(fileD);
            String defaultInhalt = "mich@gibts.net, http://www.facebook.de";
            Services.write(defaultInhalt.getBytes());
            Services.close();
        }
        catch(IOException ex){
            Log.d("Fehler", ex.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
         * Ruft die Activity About mittels eines Intents auf
         */
    if (id == R.id.action_settings) {
        final Intent intentAbout = new Intent(this, About.class);
        startActivity(intentAbout);
        return true;
    }
        /**
         * Beendet das Programm
         */
    if (id == R.id.mnuExit) {
        finish();
    }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Die Activity Password wird mittels Intent aufgerufen
     */
    public void onClickGenerator (final View cmd){
        final Intent intentPW = new Intent(this, Password.class);
        startActivity(intentPW);
    }
    /**
     * Die Activity ShowKonten wird mittels Intent aufgerufen
     */
    public void onClickVerwaltung (final View cmd){
         final Intent intentVW = new Intent(this, ShowKonten.class);
         startActivity(intentVW);
    }

    /**
     * Methode getstring() liest den String aus der Dienste_PWGen.txt aus um zu prüfen,
     * ob die Datei leer ist
     */
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
            dienste = buf.toString();
            in.close();}
        catch (java.io.FileNotFoundException e) {}
        catch (Throwable t) {Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();}
        Log.v("keygen.MainActivity", "Methode getstring() wird beendet");}

    /**
     * Die onClickMethode onClickShowHelp ruft mittels Intent die Activity Help auf
     * @param cmd
     */
   public void onClickShowHelp(final View cmd){
        startActivity(new Intent(this, Help.class));
    }

  }

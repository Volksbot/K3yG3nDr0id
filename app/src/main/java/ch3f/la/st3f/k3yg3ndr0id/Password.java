package ch3f.la.st3f.k3yg3ndr0id;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * Die Activity Password dient der Generierung des Passworts
 */

//Adapter View für die Auswahl der Dienste

public class Password extends AppCompatActivity implements OnItemSelectedListener{

    private static ArrayList<String> without = new ArrayList<String>();

    boolean checkactual =true;

    EditText masterpasswort =null;
    EditText masterpasswort2 = null;
    //eingegebene Sonderzeichen
    EditText bset=null;
    EditText pwl;

    //Iterationen werden festgelegt
    int iteration = 1000;
    int pwll;
    int bitlaenge;

    //Datei mit den gelisteten Diensten
    File fileS;
    //Datei mit jeweiliger URL + Kennung
    File fileKonto;

    //Hilfsvariablen

    Spinner spinDienst;
    String hashed="";
    String kenn="";
    String mapa1 ="";
    String mapa2="";
    String neuertest="";
    String salt = "";
    String services="";
    String services2="";
    String vers="";
    String wsc="";
    String zeichenkette = "";
    static String bs="";
    String path;
    String test;
    String generatedhash;

    //Hilfsvariable für Dienst
    String Zeile;
    // Hilfszeile für URL+Kennung
    String hilfsZeile;
    //String Array der Dienste (open end)
    String[] dienst_array;
    //Hilfsvariable zur Auswahl der Dienste und Lesen der gewählten Dienstdatei
    String sc;
    //String Array für Accountdaten, zwei
    String[] accountdaten;

    TextView passwort;
    TextView kennung;
    EditText version;
    TextView url;

    /**
     * String Array mit den verwendeten Zeichen, dient zum Aussortieren bestimmter Zeichen.
     * 77 Zeichen
     */
    private static final String[] GEHEIM = { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "0", "-", "_", "+", "*", "/", "#", "!", ".", ":", "?", ";",
            "€", "&", "%", "§", "=", "@", "(", ")", "[", "]", "{", "}", "<",
            ">", "$", "~"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        //Titel der Activity setzen
        setTitle("Passwort-Generator:");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Zuordnung der Layout Elemente zu den Variablen
         */
        passwort = (TextView) findViewById((R.id.PassGen));
        kennung = (TextView) findViewById(R.id.Kennung);
        spinDienst = (Spinner) findViewById(R.id.spinDienst);
        version = (EditText) findViewById(R.id.Version);
        masterpasswort = (EditText) findViewById(R.id.Masterpw);
        masterpasswort2 = (EditText) findViewById(R.id.Masterpwwdh);
        bset = (EditText)findViewById(R.id.Sonderzeichen);
        url = (TextView)findViewById(R.id.URL);
        pwl = (EditText)findViewById(R.id.passwortlaenge);
        /**
         * Die Passwortlänge wird auf 50 Zeichen festgelegt.
         */
        pwll=50;
        /**
         * Path holt sich den Pfad um das Zugreifen auf die Textdateien zu ermöglichen
         */
        path = Environment.getExternalStorageDirectory().getPath();

        /** Ruft die Methode für die Initialisierung des Spinners auf
         *
         */
        der_spinnt_doch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch(item.getItemId()){
            case R.id.actExitgen:
            {
                Toast.makeText(this, "Abbruch!", Toast.LENGTH_LONG).show();
                finish();
                break;
            }

            case R.id.actCopy:
            {
                // Gets a handle to the clipboard service.
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);

                // Creates a new text clip to put on the clipboard
                ClipData clip = ClipData.newPlainText("password", passwort.getText());
                // Set the clipboard's primary clip.
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Password copied to clipboard", Toast.LENGTH_LONG).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Es wird geprüft, dass in jedem Feld eine Eingabe getätigt wurde und
     * ob die Eingabe des Masterpasswords übereinstimmt.
     * Wenn dies zutrifft, wird die Methode Hashing() aufgerufen
     * @param cmd
     */
    public void onClickGO(final View cmd) {
        Log.v("keygen.PasswordActivity", "Methode onClickGO() wird aufgerufen");
        if (checkactual == true) {
            Log.v("keygen.PasswordActivity", "Prüfwert für Doppelklick ist true");
            //holt die eingegebenen Sonderzeichen
            bs = bset.getText().toString();
/**
 * Die Methode getWithout() wird aufgerufen
 */
            getWithout();
            mapa1 = masterpasswort.getText().toString();
            mapa2 = masterpasswort2.getText().toString();
            kenn = kennung.getText().toString();
            vers = version.getText().toString();
            if (kenn.equals(null) || kenn.equals("") || mapa1.equals(null) || mapa1.equals("") ||
                    mapa2.equals(null) || mapa2.equals("") || vers.equals(null) || vers.equals("")) {
                Log.v("keygen.PasswordActivity", "Nicht alle Felder ausgefüllt");
/**
 * Falls nicht alle Felder ausgefüllt wurden, wird ein Alert Dialog ausgegeben.
 */
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setMessage(R.string.pDlgAlertMessage);
                adb.setCancelable(true);
                adb.setPositiveButton("O.K.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                adb.show();

            } else {
                Log.v("keygen.PasswordActivity", "Alle Felder sind ausgefüllt");
                if (mapa1.equals(mapa2)) {
                    Log.v("keygen.PasswordActivity", "Passwörter stimmen überein!");
/**
 * Hier wird die Zeichenkette zusammengesetzt aus
 * Masterpasswort, Version, ausgewählter Dienst (Spinner), zugeh. Kennung, zugeh. URL
 */
                    zeichenkette = mapa1 + vers + sc + kenn + url.getText().toString();
                    Log.v("keygen.PasswordActivity", "Zeichenkette erstellt: "+ zeichenkette);
/**
 * Methode hashing() wird aufgerufen und zeichenkette, salt übergeben
  */
                    Log.v("keygen.PasswordActivity", "Methode hashing() wird aufgerufen");
                    hashing(zeichenkette, salt);
                } else {
                    Log.v("keygen.PasswordActivity", "Passwörter stimmen nicht überein");
/**
* Falls die Passwörter nicht übereinstimmen, wird ein Alert Dialog ausgegeben
*/
                    AlertDialog.Builder adb2 = new AlertDialog.Builder(this);
                    adb2.setMessage(R.string.pDlgAlertMessagePW);
                    adb2.setCancelable(true);
                    adb2.setPositiveButton("O.K.", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    adb2.show();
                }
            }
            Log.v("keygen.PasswordActivity", "Prüfwert für Doppelklick auf false gesetzt");
            checkactual=false;
        }
        else{
            Log.v("keygen.PasswordActivity", "Prüfwert für Doppelklick ist false");
            Toast.makeText(this, "Fehler! Daten werden aktualisiert. Bitte Daten erneut eingeben.", Toast.LENGTH_LONG).show();
            Log.v("keygen.PasswordActivity", "Methode aktualisieren() wird aufgerufen");
            aktualisieren();
            Log.v("keygen.PasswordActivity", "Methode onClickGO() wird beendet");}}

    /**
     *
     * @param zeichenkette
     * @param Salt
     * @return
     */
    String hashing (String zeichenkette, String Salt) {
        Log.v("keygen.PasswordActivity", "Methode hashing() ist aufgerufen");
        generatedhash = "";
        test = "";
        try {
            /**
             * Die Methode zur Erstellung eines Salt wird aufgerufen
             */
            Log.v("keygen.PasswordActivity", "Methode salting() wird aufgerufen");
            salting();

            /**
             * Zeichenkette zum Char umwandeln für den PBKDF2-Algorithmus
             */
            char[] charpass = zeichenkette.toCharArray();
            /**
             * Bytes vom Salt erhalten
             */
            byte[] saltBytes = salt.getBytes();
            /**
             * Schluessellaenge in Bits festlegen
             */
            bitlaenge = 512;
            /**
             * Die Werte werden übergeben und mit PBKDF2+SHA1 gehasht.
             */
            Log.v("keygen.PasswordActivity", "Hashing wird durchgeführt");
            PBEKeySpec spec = new PBEKeySpec(charpass, saltBytes, iteration, bitlaenge);
            SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            /**
             *
             */
            byte[] hashedPassword = key.generateSecret(spec).getEncoded();
            /**
             * Fügt dem String das gehashte Passwort zu
             */
            for(int i =0;i<hashedPassword.length;i++){
                generatedhash=generatedhash+hashedPassword[i];}
            /**
             * entfernt alle "-" aus dem String
             */
            test=generatedhash.replaceAll("-", "");
            /**
             * Fügt dem String an jede 3 Stelle ein ";" hinzu.
             */
            for(int i=1;i< (test.length()/2);i++){
                neuertest=neuertest+test.substring(2*(i-1),2*i)+";";
            }
            int h=-1;
            String[] list = new String[test.length()/2];

            /**
             * Schleife zum Aufsplitten des Strings an den Stellen wo ein ; vorkommt, für jedes ";"
             */
            for (String retval: neuertest.split(";")){
                h++;
                //Der Substring wird dem Arrayeintrag übergeben
                list[h]=retval;
                Log.v("keygen.PasswordActivity","List array Wert: "+ list[h].toString());
            }
            neuertest="";
            /**
             * Wert j mit dem Inhalt der Elemente von list definieren.
             */
            for(int i=0;i<list.length-1;i++){
                int j =Integer.valueOf(list[i]);
                Log.v("keygen.PasswordActivity", "Aktueller j-Wert: "+String.valueOf(j));
                /**
                 * Testet, dass ob die Anzahl der Elemente von "Geheim" kleiner ist als die
                 * von list (oben befüllt)
                 * wenn ja wird j subtrahiert mit geheim.
                 * Der neue J Wert wird überprüft ob er verboten ist.
                 */
                if (j > GEHEIM.length - 1)j = j - (GEHEIM.length);
                Log.v("keygen.Editdata", "Methode testWithout() wird aufgerufen");
                j=testWithout(j);
                neuertest = neuertest + GEHEIM[j];}
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();}
        if(!pwl.getText().toString().equals("")) {
            pwll = Integer.parseInt(pwl.getText().toString());}
        neuertest=neuertest.substring(0,pwll);
        passwort.setText(neuertest);
        Log.v("keygen.PasswordActivity", "Hashing wird beendet");
        return generatedhash;
    }

    /**
     *Der Saltwert wird mittels den eingegebenen und aus den Dateien ausgelesenen Informationen festgelegt
     *
     */
    public void salting() {
        Log.v("keygen.PasswordActivity", "Methode salting() ist aufgerufen");
        /**
         * Salt wird festgelegt
         */
        salt = masterpasswort2.getText().toString() + "sadfahjFDAFaGH" + version.getText().toString()
                +"123456"+kennung.getText().toString();
        Log.v("keygen.PasswordActivity", "Methode salting() wird beendet");}

    /**
     * testWithout prüft, ob ein gefundenes Zeichen
     * ausgeschlossen ist. Wenn das so ist, wird das nächste Zeichen
     * aus GEHEIM ausgewählt.
     * @param dec
     * @return
     */
    private static int testWithout(int dec) {
        Log.v("keygen.PasswordActivity", "Methode testWithout() ist aufgerufen");

        /**
         * während die Variable without einen Wert entspricht der in dem Stringarray Geheim enthalten ist,
         * wird das Zeichen übersprungen und stattdessen das nächste genommen.
         */
        while (without.contains(GEHEIM[dec])) {
            dec++;
            if (dec > GEHEIM.length - 1)dec = dec - (GEHEIM.length);}
        Log.v("keygen.PasswordActivity", "Methode testWithout() wird beendet");
        return dec;}

    /**
     * getWithout füllt ein String Array mit den verbotenen Zeichen
     */
    public static void getWithout() {
        Log.v("keygen.PasswordActivity", "Methode getWithout() ist aufgerufen");
        /**
         * Elemente des StringArray Without werden entfernt.
         */
        without.clear();
        char[] letter = bs.toCharArray();
        for (int i = 0; i < letter.length; i++) {without.add(i, String.valueOf(letter[i]));}
        Log.v("keygen.: ", without.toString());
        Log.v("keygen.PasswordActivity", "Methode getWithout() wird beendet");
    }

    /**
     * Setzt alle von der Activity veränderten Variablen zurück und erlaubt ein erneutes Passwortgenerieren.
     */
    public void aktualisieren(){
        Log.v("keygen.PasswordActivity", "Methode aktualisieren() ist aufgerufen");
        passwort.setText("");
        kennung.setText("");
        version.setText("");
        masterpasswort2.setText("");
        masterpasswort.setText("");
        url.setText("");
        bset.setText("");
        hashed="";
        kenn="";
        mapa1 ="";
        mapa2="";
        neuertest="";
        salt = "";
        services="";
        services2="";
        vers="";
        wsc="";
        zeichenkette = "";
        bs="";
        pwll=50;
        pwl.setText("");
        checkactual=true;
        Log.v("keygen.PasswordActivity", "Methode aktualisieren() wird beendet");
    }

    /**
     * Methode um den Spinner mit den Informationen aus der Dienst-Datei Dienste_PWGen.txt
     * zu füllen.
     */
    public void der_spinnt_doch(){
        try{
            path = Environment.getExternalStorageDirectory().getPath() + "/Dienste_PWGen.txt";
            fileS = new File(path);
            FileReader reader = new FileReader(fileS);
            BufferedReader eingabedatei = new BufferedReader(reader);
            Zeile = eingabedatei.readLine();
            reader.close();
            //Toast.makeText(this, Zeile, Toast.LENGTH_SHORT).show();   //Debugzeile
        }
        catch(Exception ex)
        {
            Log.d("Fehler!!", ex.getMessage());
        }

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

        ArrayAdapter<String> spinneradapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dienst_array);

        spinDienst.setAdapter(spinneradapter);

        //Adapter View für OnClick Event
        spinDienst.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        choose_service();
    }

    // Notwendige Methode als Gegenstück für onItemSelected()

    public void onNothingSelected(AdapterView<?> parent){}

    // Methode zum Befüllen der Felder URL und Kennung
    // Liest den Inhalt aus den jeweiligen Service Files

    public void choose_service(){

       sc = spinDienst.getSelectedItem().toString();

        try{
            path = Environment.getExternalStorageDirectory().getPath() + "/" + sc + ".txt";
            fileKonto = new File(path);
            FileReader reader = new FileReader(fileKonto);
            BufferedReader eingabedatei = new BufferedReader(reader);
            hilfsZeile = eingabedatei.readLine();
            reader.close();
            //  Toast.makeText(this, Zeile, Toast.LENGTH_SHORT).show();   //Debugzeile
        }
        catch(Exception ex)
        {
            Log.d("Fehler!", ex.getMessage());
        }

        // Accountdaten aus Datei lesen und in edit Feld schreiben

        accountdaten = new String[2];
        int h=-1;
        for (String retval: hilfsZeile.split(",")){
            h++;
            //Der Substring wird dem Arrayeintrag übergeben
            accountdaten[h]=retval;
        }
        //
        ((EditText) findViewById(R.id.Kennung)).setText(accountdaten[0]);

        ((TextView) findViewById(R.id.URL)).setText(accountdaten[1]);

    }
    }



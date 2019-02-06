# K3yG3nDr0id
Passowortgenerator in Java zum generieren sicherer PW

Sich mit Passwörtern auseinander zu setzen oder gar zu verwalten ist kein aufregender Zeitvertreib. Es gibt viele Vorraussetzungen die für die Zusammensetzung eines sicheren Passwortes notwendig sind, dazu zählen die Länge, ein zufälliger Mix aus Groß- und Kleinbuchstaben, die Beinhaltung von Zahlen und Sonderzeichen etc. Am besten natürlich verwendet man für jeden Dienst ein anderes Passwort und dieses sollte sich der Anwender auch merken. Hier stoßen die meißten User an Ihre Grenzen – verständlicher Weise.
Ziel dieser Semesterarbeit soll daher sein, eine Android basierte App – bzw. einen Passwort-Generator – zu kreieren, welche Passwörter aus einem einzigen, sicheren Masterpasswort erzeugt. 
Zur Realisierung dieses Anspruchs wird die kryptografische Hash-Funktion PBKDF2 (Password-Based Key Derivation Function 2) verwendet, welche aus den Inputdaten Masterpasswort, Konto (bzw. URL), Kennung, Länge und einer Versionsnummer ein sicheres Passwort generiert. Zudem sollte die Möglichkeit vorgesehen werden, einzelne Sonderzeichen von der Passwort-Generierung auszuschließen um dann letztendlich alle Daten in einer Textdatei zu verwalten.

Zur Verwaltung der Inputdaten werden Textdateien verwendet, zum einen die Datei Dienste_PWGen.txt welche eine Auflistung der Dienstnamen beinhaltet. Diese Datei dient unter anderem in der Password Activity dazu, einen Spinner mit den Diensten zu füllen. Des weiteren wird für jeden angelegten Dienst jeweils eine Datei angelegt, in der die persönliche Kennung und die URL des Dienstes gespeichert werden. Um die Benutzerdaten zu verwalten, wurden die Activitys „ShowKonten“, „NewKonto“ und „EditKonto“ erstellt. Zur Generierung der Passwörter dient die Activity Password. Zusätzlich wurde eine Möglichkeit geschaffen, vorhandene Dienste wieder zu löschen, dadurch musste zusätzlich eine Methode geschaffen werden um zu prüfen, ob Dateien leer sind oder bereits existieren bzw. ob ein anzulegender Dienst bereits existiert. Falls dies nicht geschieht, würde die Applikation mit einer Exception abstürzen. Die Dateien werden auf dem internen Gerätespeicher verwaltet und somit wird vorher in AndroidManifest.xml die notwendige Berechtigung hierfür gesetzt.
Weiterführende Informationen und eine kurze Hilfe zur Bedienung der App werden in den Activitys „About“ und „Help“ abgebildet.

Bedienung

Funktionsweise

Nach dem ersten Programmaufruf befindet sich der Anwender mittels der MainActivity im Startbildschirm der App. Im folgenden stehen dem User mehrere Anwendungsmöglichkeiten zur Verfügung, wie beispielsweise die Hilfe, welche kurz die Anwendung des Programms darstellt. Des weiteren steht die Kernfunktionalität unter „Passwort-Generator“ und ein kurzes „About“ zur Verfügung.


Bei Betätigung des Buttons „Konten verwalten“ wird dem Anwender mittels einer ListView eine Listendarstellung der bereits vorhandenen Dienste dargestellt. Hier ist die Möglichkeit gegeben, einen neuen Dienst anzulegen, indem man innerhalb der Toolbar den „+“ Button wählt und die notwendigen Angaben wie den Namen des Dienstes, die persönliche Kennung und die URL eintippt. Ausserdem kann man einen vorhandenen Dienst entweder bearbeiten, indem man ihn direkt auswählt und die Angaben dementsprechend anpasst, oder den Dienst komplett löscht. 


Die Auswahl des Passwort Generator stellt sich im erstmaligen Aufruf dem Anwender mit folgenden erforderlichen Eingaben dar. Zu Beginn wird der User aufgefordert ein persönliches Master-Passwort festzulegen und den vorher angelegten Dienst auszuwählen. Mittels des Spinners kann zwischen den vorhandenen Diensten gewählt werden und bei Auswahl von beispielsweise „Facebook“, welches den Dummy-Eintrag darstellt, wird automatisch die zugehörige Kennung und die URL in die dafür vorgesehenen Felder geschrieben. Des weiteren ist es erforderlich eine Versionierung anzugeben, die Passwortlänge zu definieren und nicht zu verwendende Sonderzeichen einzutippen.


Bei Betätigen des Buttons „GO!“ werden alle Felder auf die korrekte Eingabe geprüft und aus sämtlichen Inputdaten ein sicheres Passwort generiert.
Es kann ohne erstmaliges Erfassen eines neuen Dienstes bereits ein Passwort generiert werden, da das Programm mit einem sogenannten Dummy ausgestatt ist. Davon wird aus Sicherheitsgründen allerdings dringlichst abgeraten, da es sich hierbei um eine Standard-Einstellung handelt und dies kein sicheres Passwort ist.

Die Activitys


Im folgenden Abschnitt werden die erstellten Activitys inklusive der erstellten Methoden und Ihre Funktionalität vorgestellt.


MainActivity


Die MainActivity dient in erster Linie zur Navigation innerhalb des Programms und zur Initialisierung und Prüfung der Dienst-Dateien. 
In der onCreate Methode der Activity wird die Methode checkDatei() aufgerufen. 


Methoden:


checkDatei()


Die Methode checkDatei() prüft ob die Datei mit den Dienstnamen (Dienst_PWGen.txt) existiert und ruft die Methode getString() auf. Im wesentlichen soll hier sicher gestellt werden, dass die Methode writedummy() aufgerufen wird falls die Datei nicht existiert.


getString()

Diese Methode liest Datei Dienste_PWGen.txt aus und beschreibt den String dienste.


writedummy()

Erzeugt mittels der Klasse FileOutputStream einen OutputStream mithilfe dessen der Inhalt der beiden Dateien geschrieben wird. In diesem Fall wird die Dienste-Datei Dienste_PWGen.txt mit dem Namen des Dienstes „Facebook“ und die zugehörige Service-Datei mit dem Namen „Facebook.txt“ beschrieben, wobei in letzterer die Kennung und die URL „mich@gibts.net, http://www.facebook.de“ gesetzt werden.


onClickGenerator()

Ruft mittels eines Intent die Password Activity auf.


onClickVerwaltung()

Ruft mittels eines Intent die ShowKonten Activity auf.

onClickShowHelp()

Ruft mittels eines Intent die Help Activity auf.

onOptionsItemSelected()
Hier wird mittels Intent die Activity About aufgerufen. 


Password

Die Password Activity dient der Generierung des Passworts und zusätzlich wird der Spinner für die Dienste befüllt.
Da das einfache hashen nur Kleinbuchstaben, Ziffern und ein „-“ zurückgibt wird eine Methode benötigt um dieses zu finden und zu ersetzen. Nun konnte man die Ziffern und Kleinbuchstaben in 2 Zeichen separieren, welche zusammen in eine Zahl umrechenbar sind. Durch ein CharArray ist es Möglich die Stelle des CharArrays mit der separierten Zahl zu vergleichen und dann das dort enthaltene Zeichen in ein String hinzuzufügen, wodurch ein Passwort gebaut wird. Wenn man vorher einen String mit verbotenen Zeichen in ein Chararray aufteilt und dieses gegen den Passwort-CharArray vergleicht, kann man bei Übereinstimmung dieses Zeichen überspringen, bzw. es wird das nachfolgende gewählt. 

In der onCreate Methode wird ausserdem die Methode zur Initialisierung des Spinners aufgerufen. Das konnte nur mithilfe einer AdapterView umgesetzt werden.

onItemSelected – ist notwendig um bei Auswahl eines ListItems die Hintergrundinformationen aus Datei zu lesen und in die Edit Felder zu schreiben.

onNothingSelected Gegenstück - Notwendige Methode als Gegenstück zu onItemSelected()


Methoden:

onClickGo()

Die Methode wird durch den Button aufgerufen und prüft zunächst ob alle Felder korrekt befüllt wurden und ob die Eingabe des Master-Passworts übereinstimmt. Wenn dies zutrifft wird die Methode zum hashen aufgerufen.

hashing()

Ruft die Methode “salting()” auf und macht danach aus der Zeichenkette ein CharArray.
Dann wird die Schlüssellänge in Bit festgelegt, charpass, saltBytes, iteration und Bitlänge wird in den PBEKeySpec übergeben und gibt ein gehashtes Passwort zurück.

salting()

Die Methode legt den Wert für den Salt fest.

testWithout()

Methode prüft mittels einer Schleife ob die Variable „without“ ein Zeichen enthält, dass ausgeschlossen ist. Wenn das zutrifft, wird das nächste Zeichen aus GEHEIM ausgewählt.

getWithout()

Diese Methode füllt ein CharArray mit den von der Generierung auszuschließenden Sonderzeichen

aktualisieren()

Methode zum zurücksetzen aller verwendeten Variablen auf den Ausgangswert 

der_spinnt_doch()

Methode um den Spinner mit den Informationen aus der Dienst-Datei Dienste_PWGen.txt zu füllen.

choose_service()

Methode zum Befüllen der Felder URL und Kennung, welche den Inhalt aus den jeweiligen Service Files liest.

ShowKonten

Die Activity ShowKonten dient zur Übersicht über die bereits vorhandenen Dienste und zur weiterführenden Verwaltung der jeweiligen Konten. Desweiteren können aus dieser Activity heraus die Activity EditKonto zur Bearbeitung eines Dienstes aufgerufen und die Activity NewKonto zum Anlegen eines neuen Dienstes aufgerufen werden.

Methoden:

clickAddService()

Ruft mittels eines Intents die Activity NewKonto auf, mit deren Hilfe ein neuer Dienst hinzugefügt werden kann.

ListViewfuellen()

Methode zur Befüllung der ListView, dient der Übersicht über bereits vorhandene Dienste. Mittels FileReader wird die Datei Dienste_PWGen.txt eingelesen und der Inhalt in den String „Zeile“ geschrieben. Mittels einer Schleife wird die Zeile ausgelesen und die Größe des String Array festgelegt. Dieses Array wird in einer ListView visualisiert. Eine ausführliche Beschreibung der jeweiligen Schritte ist im Quellcode hinterlegt.

Auf Benutzereingaben wird mit Hilfer einer AdapterView reagiert:

onResume()

Bei Rückkehr aus Activity NewKonto wird die ListView neu befüllt um neuen Eintrag anzuzeigen.

EditKonto

Die Activity EditKonto dient der Bearbeitung bereits angelegter Dienste und bietet bei Bedarf die Möglichkeit des Löschens eines bereits existierenden Dienstes. Zudem wird in der onCreate Methode mittels getIntent() ein Bundle mit den übertragenen Werten aus der vorherigen Activity geholt, um den jeweiligen Accountnamen zu erhalten. 
Es besteht zusätzlich die Möglichkeit mittels eines Buttons den Dienst in dieser Activity zu löschen. Eine ausführliche Beschreibung der jeweiligen Schritte liegt innerhalb des Quellcodes vor.

Methoden:

getString()

siehe oben.

NewKonto

Die Activity NewKonto dient der Anlage eines neuen Dienstes und prüft ausserdem, ob der neu anzulegende Dienst bereits vorhanden ist. Wenn es bereits eine Datei im angegeben Pfad gibt, welche den gleichen Namen des eingegebenen Dienstes trägt wird das Anlegen abgebrochen, falls nicht wird die Datei für den Dienst angelegt und beschrieben.

Methoden:

checkVorhandensein()

Die Methode checkVorhandensein() prüft, ob es eine Datei mit dem Namen der übergebenen Zeichenkette bereits gibt.

About

Diese Activity dient allein dem Anzeigen der Allgemeinen Informationen und einer Kontaktadresse bei Anregungen und Fragen. 

Help

Dient zur Anzeige einer kurzen Bedienanleitung mittels eines WebView.

Mehr Infos gibts direkt im Quellcode ;)

mfg S. Königsberg

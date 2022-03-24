import java.util.HashMap;
import java.net.Socket;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Der Kommunikationspuffer soll die Anwendungsschicht von der low level Kommunikation über TCP trennen.
 * 
 * Je Verbindung gibt es einen Eingangs- und einen Ausgangspuffer.
 * Diese werden mit add() und get()-Operationen entsprechend des FIFO-Prinzips verarbeitet.
 * (FIFO: first-in-first-out, wie eine normale Warteschlange)
 * 
 * @author LK
 * @version 2021-09-30
 */
public class Kommunikationspuffer  
{
    // Klassenvariablen

    // HashMap: Schlüssel-Wert-Tabelle 
    // hier: ein Schlüssel ist eine Socket, ein Wert ist ein Kommunikationspuffer 
    // HashMap: mit einem Schlüssel kann (direkt) auf einen Wert zugegriffen werden 
    // hier: mit einer Socket kann direkt auf einen Kommunikationspuffer zugegriffen werden

    // Verwaltung der Ausgänge (von der Anwendungsschicht zur TCP-Schicht)
    private static HashMap<Socket, Kommunikationspuffer> ausgänge = new HashMap<Socket, Kommunikationspuffer>();

    // Verwaltung der Eingänge (von der TCP-Schicht zur Anwendungsschicht)
    private static HashMap<Socket, Kommunikationspuffer> eingänge = new HashMap<Socket, Kommunikationspuffer>();
    
    // gemeinsame Variable aller Kommunikationspuffer
    private static int currID = 0;  // zählt die Anzahl der Instanzen, somit kann jeder Puffer eine eigene ID bekommen
                                    // Diese Variable wurde nur zur Fehlersuche eingeführt.
    

    // Klassenmethoden

    // Verwaltung der Ein- und Ausgangspuffer als Singeltons

    /**
     * Bestimmt den zur Socket gehörenden Ausgangspuffer. Ist dieser nicht vorhanden, so wird ein neuer erzeugt.
     * @param socket Socket zu der der Ausgangspuffer gesucht wird
     * @return Ausgangspuffer zur Socket
     */
    public static Kommunikationspuffer getAusgang(Socket socket){
        Kommunikationspuffer ret = null;
        if (socket != null){
            if (ausgänge.get(socket) == null){
                ausgänge.put(socket,new Kommunikationspuffer(socket));

                ret = ausgänge.get(socket);
            }
            ret = ausgänge.get(socket);
        }

        return ret;
    }

    /**
     * Bestimmt den zur Socket gehörenden Eingangspuffer. Ist dieser nicht vorhanden, so wird ein neuer erzeugt.
     * @param socket Socket zu der der Eingangspuffer gesucht wird
     * @return Eingangspuffer zur Socket
     */
    public static Kommunikationspuffer getEingang(Socket socket){
        Kommunikationspuffer ret = null;
        if (socket != null){
            if (eingänge.get(socket) == null){
                eingänge.put(socket,new Kommunikationspuffer(socket));
            }
            ret = eingänge.get(socket);
        }

        return ret;
    }

    /**
     * Gibt alle Ausgänge zurück.
     * @return alle Ausgänge
     */
    public static HashMap<Socket, Kommunikationspuffer> getAusgänge(){        
        return ausgänge;
    }

    /**
     * Gibt alle Eingänge zurück.
     * @return alle Eingänge
     */
    public static HashMap<Socket, Kommunikationspuffer> getEingänge(){        
        return eingänge;
    }

    /**
     * Gibt die Menge aller Sockets zurück für die es einen Eingangspuffer gibt.
     */
    public static Set<Socket> getSocketsEingang(){        
        return eingänge.keySet();
    }

    /**
     * Gibt die Menge aller Sockets zurück für die es einen Ausgangspuffer gibt.
     */
    public static Set<Socket> getSocketsAusgang(){        
        return ausgänge.keySet();
    }

    // Instanzvariablen / Attribute 
    private ArrayList<String> strListe = new ArrayList<String>(); // der Puffer speichert Strings, diese werden in dieser ArrayList verwaltet
    private int id;

    private Socket socket;

    /**
     * Konstruktor für Objekte der Klasse ZKommunikation
     */
    private Kommunikationspuffer(Socket socket)
    {
        id = currID; // merkt sich die pufferindividuelle ID
        currID++;    // zählt den ID-Tähler eins hoch, damit der nächste Puffer eine größere ID bekommt

        this.socket = socket; // Socket, für die der Puffer arbeitet

        System.out.println("Puffer erzeugt: "+toString());

    }

    /**
     * Fügt ein neues Datenpaket in den Puffer ein.
     * @param neues Datenpaket
     */
    public synchronized void add(String str){
        System.out.println("Puffer: add("+str+") : id="+toString());
        strListe.add(str);  // Element am Ende einfügen
    }

    /**
     * Gibt das erste Element im Puffer zurück und entfernt dieses aus dem Puffer.
     * @return erstes Element oder null, wenn keine Daten im Puffer vorliegen
     */
    public synchronized String get(){
        String ret = null;

        // wenn Daten im Pufer vorhanden sind
        if (strListe.size()>0) {
            ret = strListe.get(0); // hole das erste Element

            strListe.remove(0);    // entferne das erste Element
            System.out.println("Puffer: get("+ret+") : id="+toString());
        }

        return ret;

    }    

    /**
     * Bestimmt die Anzahl der Elemente im Puffer.
     * @return Anzahl Elemente im Puffer
     */
    public synchronized int size(){

        return strListe.size();

    }    

    /**
     * Ausgabe des Objektinhalts als Zeichenkette.<p>
     * Überschreibt die Methode toString() von <code>Object</code>.
     */
    @Override
    public String toString(){
        return "ID:"+id+" for "+socket.toString()+" : "+super.toString();
    }
}
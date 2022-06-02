import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;

import java.util.*;

/**
 * Mit einem ChatServer können sich ChatClients zu einer Chatkonferenz verbinden.
 * 
 * Der ChatServer schickt dafür den Text der einkommenden Nachrichten an alle Teilnehmer.
 * 
 * @author LK
 * @version 2021-11-17
 */
public class ChatServer  implements ServerType
{

    /**
     * Startet den Chatserver, Hauptprogramm
     * @param args wird nicht genutzt
     */
    public static void main(String[] args)
    {
        System.out.println("Server " + GLOBAL.VERSION+ " wird gestartet ...");
        new ChatServer(GLOBAL.PORT);
    }

    // Instanzvariablen 
    private boolean isActive = true; // true, solange der Server läuft
    private ChatAnwendungsschicht anwendungsschicht = new ChatAnwendungsschicht(this);
    private ArrayList<Socket> socketListe = new ArrayList<Socket>(); // Liste aller Verbindungen zu den Clients
    private NickVerwaltung nickVerwaltung = new NickVerwaltung();
    /**
     * Konstruktor für Objekte der Klasse ChatServer
     * @param port Port, auf dem der Server horchen soll
     */
    private ChatServer(int port)
    {
        InetAddress ip = null;
        String hostname = "";

        // Ausgabe der IP-Adresse auf dem Bildschirm
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            System.out.println("Meine IP : " + ip);
            System.out.println("Mein Hostname : " + hostname);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        SDU sdu = new SDU("");
        ICI ici = new ICI(port); 

        try {
            anwendungsschicht.ListenREQ(ici,sdu); // fordert an, dass der Server auf dem port horcht
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("ChatServer: Fatal Error, ChatSystem will shut down.");
            close();
        }
        if (isActive) System.out.println("Server horcht auf "+ip+":"+port);

    }

    /** 
     * Anmeldung einer neuen Verbindung am Server.
     * <p>
     * Die neue Verbindung wird in die Liste aller Clientverbindungen aufgenommen.
     * @param ici neue Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsaufbauIND(ICI ici,SDU sdu)
    {
        // wenn ici existiert und ici.socket existiert
        if (ici != null && ici.socket!=null) {
            System.out.println("Server: VerbindungsaufbauIND("+ici.socket.toString()+","+"––"+")");
            
            //interne Verwaltung
            socketListe.add(ici.socket);
            
            try
            {
                //Erzeugung der Antwort an den Client mit Versionsnummer
                System.out.println("VerbindungsaufbauRESP wird ausgeführt");
                anwendungsschicht.VerbindungsaufbauRESP(ici,new SDU(GLOBAL.VERSION));
            }
            catch (Exception ze)
            {
                ze.printStackTrace();
                System.out.println("ChatServer: unknown Error, ChatSystem will shut down.");
                close();
            }
        }
    }

    /** 
     * Überprüfen und speichern des Namens wird aufgerufen.
     * <p>
     * ???
     * @param ici mit socket
     * @param sdu gewählter Name
     */
    public synchronized void NickNameIND(ICI ici,SDU sdu)
    {
        // wenn ici existiert und ici.socket existiert
        if (ici != null && ici.socket!=null) {
            System.out.println("Server: NickNameIND("+ici.socket.toString()+","+"––"+")");
            
            String antwort = nickVerwaltung.putNickname(sdu.text,ici.socket);
            
            try
            {
                System.out.println("NickNameRESP wird ausgeführt");
                anwendungsschicht.NickNameRESP(ici,new SDU(antwort));
            }
            catch (Exception ze)
            {
                ze.printStackTrace();
                System.out.println("ChatServer: unknown Error, ChatSystem will shut down.");
                close();
            }
        }
    }

    
    /**
     * Anzeige eines Textwunsches beim Server. <p>
     * Es wird eine Textanforderung für diesen Text an alle bekannten Clientverbindungen geschickt.
     * @param ici Verbindung, von der der Textwunsch kommt
     * @param sdu <code>sdu.text</code>: Text, der übertragen werden soll
     */
    public synchronized void TextAnmeldenIND(ICI ici, SDU sdu){
        System.out.println("Server: TextAnmeldenIND("+ici.socket.toString()+","+sdu.text+")");

        // Schicke den Text an alle zurück
        try{
            // für jede Verbindung
            for(Socket socketAusgang : socketListe){ 
                anwendungsschicht.TextREQ(new ICI(socketAusgang), sdu); // fordert die Textübertragung an
            }
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("ChatServer: unknown Error, ChatSystem will shut down.");
            close();
        }
    }

    /**
     * Anzeige eines Verbindungsabbauwunsches beim Server. <p>
     * Es wird eine Verbindungsabbauanforderung an alle bekannten Clientverbindungen geschickt.<p>
     * Es wird der Server geschlossen.
     * @param ici Verbindung, von der der Verbindungsabbauwunsch kommt
     * @param sdu wird nicht genutzt
     */public synchronized void VerbindungsabbauAnfrageIND(ICI ici, SDU sdu){
        System.out.println("Server: VerbindungsabbauAnfrageIND("+ici.socket.toString()+","+"––"+")");

        // für jede Verbindung
        try{
            for(Socket socketAusgang : socketListe){  
                anwendungsschicht.VerbindungsabbauREQ(new ICI(socketAusgang), sdu); // fordert den Verbindungsabbau an          
            } 
        }catch(Exception e){
            e.printStackTrace();
        }

        close();
    }

    /**
     * Beendet alle Prozesse des Chatservers
     */
    private synchronized void close(){
        isActive = false;
        anwendungsschicht.close();
        System.out.println("Server-Programm beendet."); // Beachte: andere Threads können später beendet sein
    }

}

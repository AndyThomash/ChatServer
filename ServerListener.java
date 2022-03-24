import java.io.BufferedReader; 
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.io.PrintStream; 
import java.net.ServerSocket; 
import java.net.Socket; 
import java.util.*;

/**
 * Ein ServerListener soll einen Port durchgängig abhorchen und für jeden Verbindugsaufbau einen ClientHandler erzeugen.
 * 
 * Die Anwendungsschicht wird über einen erfolgreichen Verbindungsaufbau informiert.
 * 
 * @author LK
 * @version 2021-11-17
 */
class ServerListener extends Thread  
{ 

    // Instanzattribute
    private ServerSocket server; // ServerSocket bestehen nur aus der lokalen IP-Adresse und dem lokalen Port
    private ArrayList<ConnectionHandler> connectionHandlerThreadListe = new ArrayList<ConnectionHandler>(); // Liste aller geöffneter ConnectionHandler
    private boolean isActive = true; // true, solange der ServerListern aktiv ist
    private ChatAnwendungsschicht anwendungsschicht; 

    /**
     * Erzeugt einen ServerListener.
     * @param port Port, auf dem gehorcht werden soll
     * @param anwendungsschicht Anwendungsschicht, an die raportiert wird
     */
    public ServerListener(int port, ChatAnwendungsschicht anwendungsschicht) {
        try{
            server = new ServerSocket(port); 
        } catch (IOException e) { 
            e.printStackTrace(); 
            server = null; 
        } 
        this.anwendungsschicht = anwendungsschicht;
    }

    /**
     * Diese Methode nicht aufrufen. Sie wird über start() gestartet (s. Thread).
     * <p>
     * <b>Horcht auf der ServerSocket und erzeugt neue ConnectionHandler.</b> 
     * 
     */
    @Override
    public void run()  
    { 
        // solange der ServerListener aktiv ist
        while (isActive) { 

            Socket socket = null; // belege socket leer vor, damit sie nicht mit alten Sockets verwechselt wird 
            try { 
                socket = server.accept(); // die Serversocket server hat eine neue Verbindung, merke die neue socket
                socket.setSoLinger(true,0); // setze Parameter: Warte bei einem close() (true) für 0s (0) auf die Antwort bevor die Socket geschlossen wird

                ConnectionHandler t = new ConnectionHandler(socket); // erzeuge einen neuen ConnactionHnadler
                connectionHandlerThreadListe.add(t); // füge diesen zur Liste aller ConnectionHandler hinzu
                t.start();      
                try{
                    anwendungsschicht.VerbindungsaufbauIND(new ICI(socket), new SDU("")); // teile der Anwendungsschicht die neue Socket mit
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) { 
                //e.printStackTrace(); 
            }     
        }
        
        // die Schleife wird verlassen, wenn der ServerListener nicht mehr aktiv ist
        
        System.out.println("ServerListener Thread stopped.");
    }

    /**
     * Schließt den ServerListener und alle ConnectionHandler.
     */
    public void close(){
        isActive = false;
        
        // für alle ConnectionHandler
        for(ConnectionHandler t:connectionHandlerThreadListe){
            System.out.println("ServerListener: ein ConnectionHandler wird geschlossen.");
            try {
                t.close(); // schließe den ConnectionHandler
            } catch (Exception e) { 
                e.printStackTrace(); 
            }     

        }

        try { 
            if (server!=null) {
                server.close(); // schließe die ServerSocket
                System.out.println("ServerListener: Server socket closed.");
            } else {
                System.out.println("ServerListener: Server socket == null");
            }

        } catch (IOException e) { 
            e.printStackTrace(); 
        }     
    }

}
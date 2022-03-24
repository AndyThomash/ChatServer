import java.net.Socket; 
import java.io.OutputStream; 
import java.io.PrintStream; 
import java.io.IOException; 
import java.net.UnknownHostException; 

/**
 * Eine ClientVerbindung stellt die Verbindung zu einem Server her und startet einen ConnectionHandler für diese Verbindung.
 * 
 * @author LK
 * @version 2021-11-17
 */
public class ClientVerbindung 
{
    // instance variables
    private Socket mySocket;

    private ConnectionHandler connectionHandler;

    /**
     * Constructor for objects of class Verbindung
     */
    public ClientVerbindung(){}

    /**
     * Stellt eine Verbindung zu einem Server her. Startet einen ConnectionHandler.
     * @param ipAdresse IP-Adresse des Servers
     * @param port Port des Servers
     * @return Socket der neuen Verbindung
     */
    public Socket openConnection(ICI ici){
        String ipAdresse = ici.ip;
        int port = ici.port;
        try{
            mySocket = new Socket(ipAdresse, port);
        } catch (UnknownHostException e) { 
            System.out.println("Client: Unknown Host..."); 
            e.printStackTrace(); 
        } catch (IOException e) { 
            System.out.println("Client: IOProbleme (Socket) ..."); 
            e.printStackTrace(); 
        } 

        // wenn die Verbindung erfolgreich hergestellt wurde und nicht geschlossen ist
        if (mySocket!=null && !mySocket.isClosed()){

            connectionHandler = new ConnectionHandler(mySocket); // erzeuge einen ConnectionHandler 
            connectionHandler.start();  // starte den ConnectionHandler (run-Methode als Thread)
            System.out.println("ClientVerbindung("+mySocket.toString()+") gestartet.");
        }
        else
        {
            System.out.println("Client: Socket is closed.");
        }

        return mySocket;

    }

    /**
     * Schließt die Verbindung und den ConnectionHandler.
     */
    public void closeConnection(){
        System.out.println("ClientVerbindung("+mySocket.toString()+"): closeConnection().");
        try{
            connectionHandler.close();
        } catch (Exception e){}

    }

}


/**
 * Der Zustand TCPGetrennt gilt nachdem die TCP-Verbindung abgebaut wurde.
 * <p>
 * Es können in diesem Zustand keine Methoden ausgeführt werden.
 * <p>
 * Es gibt nur eine Instanz dieser Klasse, die mit dem Singelton-Pattern verwaltet wird.
 * 
 * @author LK
 * @version 2021-11-17
 */
public class TCPGetrennt extends ChatAnwendungsschichtZustand
{
    // Klassenvariablen
    private static TCPGetrennt singelton;
    
    // Klassenmethode
    /**
     * Gibt das Singelton zurück. Ein Singelton wird beim ersten Aufruf erzeugt.
     * @return Singelton
     */
    public static  TCPGetrennt getSingelton(){
        if (singelton == null){
            singelton = new TCPGetrennt();
        }
        return singelton;
    }


    // Instanzvariablen
    // keine
   

    /**
     * Konstruktor für Objekte der Klasse TCPGetrennt
     */
    public TCPGetrennt()
    {
        super("TCPGetrennt"); // Aufruf des Konstruktors der ChatAnwendungsschichtZustand-Klasse
                              // Der Aufruf der Konstruktormethode des Oberklasse muss in der ersten Zeile des Konstruktors stehen.
    }

    
}

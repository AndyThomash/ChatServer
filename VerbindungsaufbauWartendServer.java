
/**
 * Der Zustand VerbindungsaufbauWartendServer gilt bevor die TCP-Verbindung aufgebaut wurde.
 * <p>
 * Es können in diesem Zustand folgende Methoden ausgeführt werden: <p>
 * Server:
 * <ul>
 * <li> VerbindungsaufbauRESP
 * </ul>
 * <p>
 * Es gibt nur eine Instanz dieser Klasse, die mit dem Singelton-Pattern verwaltet wird.
 * 
 * @author LK
 * @version 2021-11-17
 */
public class VerbindungsaufbauWartendServer extends ChatAnwendungsschichtZustand
{
    // Klassenvariablen
    private static VerbindungsaufbauWartendServer singelton;
    
    // Klassenmethode
    /**
     * Gibt das Singelton zurück. Ein Singelton wird beim ersten Aufruf erzeugt.
     * @return Singelton
     */
    public static VerbindungsaufbauWartendServer getSingelton(){
        if (singelton == null){
            singelton = new VerbindungsaufbauWartendServer();
        }
        return singelton;
    }
    // Instanzvariablen     
    // keine

    /**
     * Konstruktor für Objekte der Klasse VerbindungsaufbauWartendServer
     */
    private VerbindungsaufbauWartendServer()
    {
        super("VerbindungsaufbauWartendServer"); // Aufruf des Konstruktors der ChatAnwendungsschichtZustand-Klasse
                              // Der Aufruf der Konstruktormethode des Oberklasse muss in der ersten Zeile des Konstruktors stehen.
    }
    
    /**
     * VerbindungsaufbauRESP
     */
    public synchronized void VerbindungsaufbauRESP(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.out.println("Server: VerbindungsaufbauWartendServer -> TCPVerbundenServer");
        kontext.nextState(ici,VerbundenServer.getSingelton());
        kontext.VerbindungsaufbauRESPDO(ici,sdu);
    }
}

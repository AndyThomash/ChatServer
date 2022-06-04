
/**
 * Der Zustand AbfrageUserdataServer gilt beim Server nachdem die TCP-Verbindung aufgebaut wurde.
 * <p>
 * Es können in diesem Zustand folgende Methoden ausgeführt werden: <p>
 * Server:
 * <ul>
 * <li> VerbindungsabbauAnfrageIND
 * <li> VerbindungsabbauREQ
 * <li> TextREQ
 * <li> TextAnmeldenIND
 * </ul>
 * <p>
 * Es gibt nur eine Instanz dieser Klasse, die mit dem Singelton-Pattern verwaltet wird.
 * 
 * @author LK
 * @version 2021-11-17
 */
public class AbfrageUserdataServer extends ChatAnwendungsschichtZustand
{
    // Klassenvariablen
    private static AbfrageUserdataServer singelton;
    
    // Klassenmethode
    /**
     * Gibt das Singelton zurück. Ein Singelton wird beim ersten Aufruf erzeugt.
     * @return Singelton
     */
    public static  AbfrageUserdataServer getSingelton(){
        if (singelton == null){
            singelton = new AbfrageUserdataServer();
        }
        return singelton;
    }
    // Instanzvariablen     
    // keine
        
    /**
     * Konstruktor für Objekte der Klasse TCPVerbundenClient
     */
    private AbfrageUserdataServer()
    {
        super("AbfrageUserdataServer");  // Aufruf des Konstruktors der ChatAnwendungsschichtZustand-Klasse
                                      // Der Aufruf der Konstruktormethode des Oberklasse muss in der ersten Zeile des Konstruktors stehen.
    }
    /**
     * NickNameIND
     */
    public synchronized void NickNameIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.NickNameINDDO(ici,sdu);
    }
    /**
     * NickNameRESP
     */
    public synchronized void NickNameRESP(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.NickNameRESPDO(ici,sdu);
        if (sdu.text == "ACCEPTED"){
            kontext.nextState(ici,VerbundenServer.getSingelton());
            System.out.println("Server: AbfrageUserdataServer -> VerbundenServer");
        }
    }
}

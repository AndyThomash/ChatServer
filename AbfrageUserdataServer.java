
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
     * VerbindungsabbauAnfrageIND
     */
    public synchronized void VerbindungsabbauAnfrageIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.VerbindungsabbauAnfrageINDDO(ici,sdu);
    }
    /**
     * VerbindungsabbauREQ
     */
    public synchronized void VerbindungsabbauREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.VerbindungsabbauREQDO(ici,sdu);
        System.out.println("Server: AbfrageUserdataServer -> VerbundenServer");
        kontext.nextState(ici,VerbundenServer.getSingelton());
    }
    /**
     * TextREQ
     */
    public synchronized void TextREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.TextREQDO(ici,sdu);
    }
    /**
     * TextAnmeldenIND
     */
    public synchronized void TextAnmeldenIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.TextAnmeldenINDDO(ici,sdu);
    }
}

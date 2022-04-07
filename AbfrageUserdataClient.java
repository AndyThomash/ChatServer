
/**
 * Der Zustand AbfrageUserdataClient gilt beim Client nachdem die TCP-Verbindung aufgebaut wurde.
 * <p>
 * Es können in diesem Zustand folgende Methoden ausgeführt werden: <p>
 * Client:
 * <ul>
 * <li> VerbindungsabbauAnfrageREQ
 * <li> VerbindungsabbauIND
 * <li> TextAnmeldenREQ
 * <li> TextIND
 * </ul>
 * <p>
 * Es gibt nur eine Instanz dieser Klasse, die mit dem Singelton-Pattern verwaltet wird.
 * 
 * @author LK
 * @version 2021-11-17
 */
public class AbfrageUserdataClient extends ChatAnwendungsschichtZustand
{
    // Klassenvariablen
    private static AbfrageUserdataClient singelton;
    
    // Klassenmethode
    /**
     * Gibt das Singelton zurück. Ein Singelton wird beim ersten Aufruf erzeugt.
     * @return Singelton
     */
    public static  AbfrageUserdataClient getSingelton(){
        if (singelton == null){
            singelton = new AbfrageUserdataClient();
        }
        return singelton;
    }
    // Instanzvariablen     
    // keine
    
    
    /**
     * Konstruktor für Objekte der Klasse AbfrageUserdataClient
     */
    private AbfrageUserdataClient()
    {
        super("AbfrageUserdataClient"); // Aufruf des Konstruktors der ChatAnwendungsschichtZustand-Klasse
                                     // Der Aufruf der Konstruktormethode des Oberklasse muss in der ersten Zeile des Konstruktors stehen.
    }

    /**
     * VerbindungsabbauAnfrageREQ
     */
    public synchronized void VerbindungsabbauAnfrageREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.VerbindungsabbauAnfrageREQDO(ici,sdu);
    }
    /**
     * VerbindungsabbauIND
     */
    public synchronized void VerbindungsabbauIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.VerbindungsabbauINDDO(ici,sdu);
        System.out.println("Client: AbfrageUserdataClient -> TCPGetrennt");
        kontext.nextState(ici,TCPGetrennt.getSingelton());
    }
    /**
     * TextAnmeldenREQ
     */
    public synchronized void TextAnmeldenREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.TextAnmeldenREQDO(ici,sdu);
    }     
    /**
     * TextIND
     */
    public synchronized void TextIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.TextINDDO(ici,sdu);
    }
}

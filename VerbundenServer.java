
/**
 * Der Zustand VerbundenServer gilt beim Server nachdem die Userdaten angenommen wurden.
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
 * @author LK, Leo G., Marika K. Dave P. Lando A.
 * @version 2022-06-08
 */
public class VerbundenServer extends ChatAnwendungsschichtZustand
{
    // Klassenvariablen
    private static VerbundenServer singelton;
    
    // Klassenmethode
    /**
     * Gibt das Singelton zurück. Ein Singelton wird beim ersten Aufruf erzeugt.
     * @return Singelton
     */
    public static  VerbundenServer getSingelton(){
        if (singelton == null){
            singelton = new VerbundenServer();
        }
        return singelton;
    }
    // Instanzvariablen     
    // keine
        
    /**
     * Konstruktor für Objekte der Klasse TCPVerbundenClient
     */
    private VerbundenServer()
    {
        super("VerbundenServer");  // Aufruf des Konstruktors der ChatAnwendungsschichtZustand-Klasse
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
        System.out.println("Server: VerbundenServer -> TCPGetrennt");
        kontext.nextState(ici,TCPGetrennt.getSingelton());
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
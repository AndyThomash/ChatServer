
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
     * NickNameREQ
     */
    public synchronized void NickNameREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.NickNameREQDO(ici,sdu);
    }
    
    /**
     * NickNameCONF
     */
    public synchronized void NickNameCONF(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        kontext.NickNameCONFDO(ici,sdu);
        if(sdu.text=="NOT_ACCEPTED"){
            kontext.VerbindungsabbauAnfrageIND(ici,sdu);
        }else{
            System.out.println("Client: AbfrageUserdataClient -> VerbundenClient");
            kontext.nextState(ici,VerbundenClient.getSingelton());
        }
    }
}

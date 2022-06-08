
/**
 * Dieser Datentyp defniert die Methoden, die von der ChatAnwendungsschicht bei einem Client aufgerufen werden.
 * 
 * @author LK, Leo G., Marika K. Dave P. Lando A.
 * @version 2022-06-08
 */

public interface ClientType
{
    /**
     * Der Verbindungsaufbau wird bestätigt.
     */
    public void VerbindungsaufbauCONF(ICI ici, SDU sdu);

    /**
     * Ein neuer Text kommt vom Server an.
     */
    public void TextIND(ICI ici, SDU sdu);

    /**
     * Der Verbindungsabbau wird angezeigt.
     */
    public void VerbindungsabbauIND(ICI ici, SDU sdu);
    
    /**
     * 
     */
    public  void NickNameCONF(ICI ici,SDU sdu);
}
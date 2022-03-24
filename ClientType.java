
/**
 * Dieser Datentyp defniert die Methoden, die von der ChatAnwendungsschicht bei einem Client aufgerufen werden.
 * 
 * @author LK
 * @version 2021-09-30
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
}
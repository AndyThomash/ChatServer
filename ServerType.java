
/**
 * Dieser Datentyp defniert die Methoden, die von der ChatAnwendungsschicht bei einem Server aufgerufen werden.
 * 
 * @author LK
 * @version 2021-09-30
 */

public interface ServerType
{
    /** 
     * Eine neue verbindung zum Server wurde hergestellt.
     */
    public void VerbindungsaufbauIND(ICI ici,SDU sdu);
    /**
     * Ein neuer Text wird zum Weiterverteilen angezeigt.
     */
    public void TextAnmeldenIND(ICI ici, SDU sdu);
    /**
     * Ein Verbindungsabbau wird angezeigt.
     */
    public void VerbindungsabbauAnfrageIND(ICI ici, SDU sdu);
    /**
     * Ein Verbindungsabbau wird angezeigt.
     */
    public void NickNameIND(ICI ici, SDU sdu);
}

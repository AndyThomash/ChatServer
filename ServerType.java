
/**
 * Dieser Datentyp defniert die Methoden, die von der ChatAnwendungsschicht bei einem Server aufgerufen werden.
 * 
 * @author LK, Leo G., Marika K. Dave P. Lando A.
 * @version 2022-06-08
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
     * Server überprüft Nickname.
     */
    public void NickNameIND(ICI ici, SDU sdu);
}

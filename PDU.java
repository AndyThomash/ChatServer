
/**
 * Eine Protocol Data Unit (PDU) wird zwischen den Partnerinstanzen ausgetauscht. 
 * Sie enthält strukturiert einen Header und die Nutzlast (SDU).
 * 
 * Der Header speichert die Kontrollinformationen für die aktuelle Schicht.
 * 
 * Eine PDU kann die Struktur auch aufgeben und Headeer und SDU in einer Zeichenkette kodieren.
 * Bei der Erzeugung kann aus einer unstrukturierten Zeichenkette die Darstellung in Header und SDU erstellt werden.
 * 
 * @author LK, Leo G., Marika K. Dave P. Lando A.
 * @version 2022-06-08
 */
public class PDU
{

    // Instanzvariablen
    public String header;
    public String sdu;

    /**
     * Konstruktor für Objekte der Klasse PDU
     * @param header Header der Nachricht
     * @param sdu Datenteil der Nachricht
     */
    //public PDU(String header, String sdu)
    //{
    //   // Instanzvariable initialisieren
    //    this.header = header;
    //    
    //    //Farben müssen hier behandelt werden
    //    this.sdu = sdu;
    //}

    /**
     * Konstruktor für Objekte der Klasse PDU
     * @param header Header der Nachricht
     * @param sdu Datenteil der Nachricht
     */
    public PDU(String header, SDU sdu)
    {
        // Instanzvariable initialisieren
        this.header = header;
        sdu.toText();
        this.sdu = sdu.text;
    }

    /**
     * Konstruktor für Objekte der Klasse PDU
     * Die Daten werden als Header und SDU gespeichert.
     * @param pdu gesamte PDU der Nachricht mit Header und Datenteil, getrennt von einem Doppelpunkt
     */
    public PDU(String pdu)
    {
        String[] strArray = pdu.split(":",2);

        // Instanzvariable initialisieren
        if (strArray.length == 2) {
            this.header = strArray[0];
            this.sdu    = strArray[1];
        } else {
            this.header = "HEADER-ERROR";
            this.sdu    = "SDU-ERROR";
        }
    }

    /**
     * Wandelt den gespeicherten Header und SDU in eine PDU um. 
     * Die PDU besteht aus dem Header gefolgt von einem Doppelpunkt und schließt mir der SDU ab.
     * return PDU
     */
    String getPDU(){
        return header+":"+sdu;
    }

}

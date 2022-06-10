/**
 * Abstrakte Oberklasse für alle Zustände. 
 * Es werden alle möglichen Methoden in der Implementierung abgelehnt, 
 * d.h. die Methoden werden für die Durchführung blockiert und werfen eine Ausnahme. 
 * <p>
 * Die Unterklassen müssen gezielt die in den Zuständen erlaubten Methoden öffnen.
 * <p>
 * <i>Hinweis: Von abstrakten Klassen gibt es keine Instanzen.</i>
 * @author LK, Leo G., Marika K. Dave P. Lando A.
 * @version 2022-06-08
 */
public abstract class ChatAnwendungsschichtZustand
{
    String name = "ChatAnwendungsschichtZustand"; // Name des Zustands, wird von den Objekten der Unterklassen individuell gesetzt.
    
    public ChatAnwendungsschichtZustand(String name){
        this.name = name;
    }

    /**
     * ListenREQ
     */
    public synchronized void ListenREQ(ChatAnwendungsschicht kontext, ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: ListenREQ nicht möglich.");
        throw new ZustandException();

    }

    /**
     * VerbindungsaufbauREQ
     */
    public synchronized void VerbindungsaufbauREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: VerbindungsaufbauREQ nicht möglich.");
        throw new ZustandException();
    }

    /**
     * VerbindungsaufbauIND
     */
    public synchronized void VerbindungsaufbauIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: VerbindungsaufbauIND nicht möglich.");
        throw new ZustandException();
    }

    /**
     * VerbindungsaufbauRESP
     */
    public synchronized void VerbindungsaufbauRESP(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: VerbindungsaufbauRESP nicht möglich.");
        throw new ZustandException();
    }
    
    /**
     * VerbindungsaufbauCONF
     */
    public synchronized void VerbindungsaufbauCONF(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: VerbindungsaufbauCONF nicht möglich.");
        throw new ZustandException();
    }
    
    /**
     * NickNameREQ
     */
    public synchronized void NickNameREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: NickNameREQ nicht möglich.");
        throw new ZustandException();
    }
    
    /**
     * NickNameIND
     */
    public synchronized void NickNameIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: NickNameIND nicht möglich.");
        throw new ZustandException();
    }
    
    /**
     * NickNameRESP
     */
    public synchronized void NickNameRESP(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: NickNameRESP nicht möglich.");
        throw new ZustandException();
    }
    
    /**
     * NickNameCONF
     */
    public synchronized void NickNameCONF(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: NickNameCONF nicht möglich.");
        throw new ZustandException();
    }
    
    /**
     * VerbindungsabbauAnfrageREQ
     */
    public synchronized void VerbindungsabbauAnfrageREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: VerbindungsabbauAnfrageREQ nicht möglich.");
        throw new ZustandException();
    }

    /**
     * VerbindungsabbauAnfrageIND
     */
    public synchronized void VerbindungsabbauAnfrageIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: VerbindungsabbauAnfrageIND nicht möglich.");
        throw new ZustandException();
    }

    /**
     * VerbindungsabbauREQ
     */
    public synchronized void VerbindungsabbauREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: VerbindungsabbauREQ nicht möglich.");
        throw new ZustandException();
    }

    /**
     * VerbindungsabbauIND
     */
    public synchronized void VerbindungsabbauIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: VerbindungsabbauIND nicht möglich.");
        throw new ZustandException();
    }

    /**
     * TextREQ
     */
    public synchronized void TextREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: TextREQ nicht möglich.");
        throw new ZustandException();
    }

    /**
     * TextAnmeldenREQ
     */
    public synchronized void TextAnmeldenREQ(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: TextAnmeldenREQ nicht möglich.");
        throw new ZustandException();
    }

    /**
     * TextIND
     */
    public synchronized void TextIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: TextIND nicht möglich.");
        throw new ZustandException();
    }

    /**
     * TextAnmeldenIND
     */
    public synchronized void TextAnmeldenIND(ChatAnwendungsschicht kontext,ICI ici,SDU sdu) throws Exception
    {
        System.err.println("ChatAnwendungsschicht –"+name+"–: TextAnmeldenIND nicht möglich.");
        throw new ZustandException();
    }

}

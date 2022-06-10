import java.net.Socket;
import java.util.*;

import java.util.Set;

/**
 * Die Chatanwendungsschicht soll die Chatanwendnung 
 * bei der Übertragung der Nachrichten und 
 * der Steuerung der Kommunikation unterstützen.
 * <p>
 * Es werden 6 Dienste angeboten: 
 * <ul>
 * <li>Listen (REQ,CONF)
 * <li>Verbindungsaufbau (REQ,IND,RESP,CONF)
 * <li>VerbindungsabbauAnfrage (REQ,IND)
 * <li>Verbindungsabbau (REQ,IND)
 * <li>NickName (REQ,IND,RESP,CONF)
 * <p>
 * <li>TextAnmeldung (REQ,IND)
 * <li>Text (REQ,IND)
 * </ul>
 * Es gibt mehrere Dienstprimitive je Dienst:
 * <p>
 * <ul>
 * <li>ListenREQ: Server horcht den TCP-Port ab (EINFACH)
 * <li>VerbindungsaufbauIND: Server hat eine Verbindung zum Client hergestellt (MEHRFACH)
 * <p>
 * <li>VerbindungsaufbauREQ: Client wünscht Verbindung zu einer IP-Adresse
 * <li>VerbindungsaufbauIND: Client hat eine Verbindung zum Server hergestellt
 * <li>VerbindungsaufbauRESP: Server antwortet dem Client
 * <li>VerbindungsaufbauCONF: Client empfängt Antwort vom Server
 * <p>
 * <li>VerbindungsabbauAnfrageREQ: Client wünscht Verbindungsabbau
 * <li>VerbindungsabbauAnfrageIND: Server erhält den Verbindungsabbauwunsch des Client
 * <p>
 * <li>VerbindungsabbauREQ: Server baut alle Clientverbindungen ab (EINFACH)
 * <li>VerbindungsabbauIND: Verbindungsabbau bei jedem Client
 * <p>
 * <li> NickNameREQ: Client sendet den Username.
 * <li> NickNameIND: Server überprüft den Username und speichert ihn wenn dieser nicht doppelt ist.
 * <li> NickNameRESP: Server antwortet dem Clienten
 * <li> NickNameCONF: Client erhält antwort, gegebenenfalls wird eine erneute Eingabe angefordert.
 * <p>
 * <li>TextAnmeldungREQ: Client meldet Textwunsch an
 * <li>TextAnmeldungIND: Server erhält Textwunsch vom CLient
 * <p>
 * <li>TextREQ: Server schickt Text an alle Clients
 * <li>TextIND: jeder Client erhält den Text
 * </ul>
 * @author LK, Leo G., Marika K. Dave P. Lando A.
 * @version 2022-06-08
 */
public class ChatAnwendungsschicht extends Thread
{

    // Instanzvariablen 
    private boolean isActive = true; // true, solange die Anwendungsschicht laufen soll

    ServerListener serverListener;             // Serverfall: Thread der auf einem Port horcht und je Socket einen ClientHandler-Thread startet
    private ClientVerbindung clientVerbindung; // Clientfall: Thread zur Behandlung der TCP-Verbindung

    private ArrayList<SocketZustand> socketListe = new ArrayList<SocketZustand>(); // Liste aller offenen Sockets zusammen mit ihrem aktuellen Zustand

    // mein Dienstbenutzer / die Anwendung
    ServerType server; // im Serverfall
    ClientType client; // im Clientfall

    /**
     * Serverfall: Konstruktor für Objekte der Klasse ChatAnwednungsschicht
     * @param server Dienstbenutzer
     */
    public ChatAnwendungsschicht(ServerType server)
    {
        System.out.println("ChatAnwendungsschicht: Servertyp");
        // Instanzvariable initialisieren
        this.server = server;
        this.client = null;
        this.start();
    }

    /**
     * Clientfall: Konstruktor für Objekte der Klasse ChatAnwednungsschicht
     * @param client Dienstbenutzer
     */
    public ChatAnwendungsschicht(ClientType client)
    {
        System.out.println("ChatAnwendungsschicht: Clienttyp");
        // Instanzvariable initialisieren
        this.server = null;
        this.client = client;
        this.start();
    }

    // holt für eine Socket den Socketzustand
    // falls dieser nicht existiert, so wird der Zustand Unverbunden zurückgegeben
    private synchronized SocketZustand getSocketZustand(Socket socket){
        SocketZustand found = null;

        for(SocketZustand aktuell: socketListe){
            if (socket == aktuell.socket){
                found = aktuell;
            }
        }    
        // wenn kein Zustand gefunden wurde, muss der Zustand Unverbunden sein.
        // dies gilt insbesondere für socket == null
        if (found == null) {
            found =  new SocketZustand(socket,Unverbunden.getSingelton());
            socketListe.add(found);
        }
        return found;
    }

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Setzt für eine Verbindung den nächsten Zustand.
     * @param ici aktuelle Verbindung
     * @param nextZustand nächster Zustand
     */
    public synchronized void nextState(ICI ici, ChatAnwendungsschichtZustand nextZustand){
        getSocketZustand(ici.socket).nextState(nextZustand);
    }

    // Setzt für eine Verbindung, bei der die Socket noch nicht bekannt war, eine neue Socket
    private synchronized void iciUpdateSocket(ICI ici, Socket socket){
        if (ici.socket == null) ici.update(socket);     
        System.out.println("ChatAnwendungsschicht: iciUpdateSocket: Länge:"+socketListe.size()+" All:"+toString());
    }

    /**
     * Schließt die Anwendungsschicht und alle von ihr gestarteten Threads.
     */
    public synchronized void close(){
        System.out.println("ChatAnwendungsschicht: close()");

        try{sleep(50000);} catch(Exception e){}

        if (server !=null){
            if (serverListener!= null)
                serverListener.close();
        }
        if (client != null){
            clientVerbindung.closeConnection();
        }
    }

    /**
     * Fordert den Server auf in den Listenzustand zu gehen.
     * @param ici Portangabe in <code>ici.port</code>
     * @param sdu wird nicht genutzt
     */
    public synchronized void ListenREQ(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.ListenREQ(this,ici,sdu);
    }

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Fordert den Server auf in den Listenzustand zu gehen.
     * @param ici Portangabe in <code>ici.port</code>
     * @param sdu wird nicht genutzt
     */
    public synchronized void ListenREQDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: ListenREQ("+ici.toString()+","+"––"+")");

        if(server  != null){
            int port = ici.port; // port auf dem gehorcht wird

            serverListener = new ServerListener(port,this);

            serverListener.start(); // startet den ServerListener-Thread 
        } else {
            System.err.println("VerbindungsaufbauIND: kein Server");
        }
    }

    /**
     * Fordert den Client auf eine Verbindung zum Server aufzubauen.
     * @param ici IP-Adresse in <code>ici.ip</code> und Portangabe in <code>ici.port</code>
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsaufbauREQ(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.VerbindungsaufbauREQ(this,ici,sdu);
    }    

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Fordert den Client auf eine Verbindung zum Server aufzubauen.
     * @param ici IP-Adresse in <code>ici.ip</code> und Port <code>ici.port</code> des Servers
     * @param sdu wird nicht genutzt
     */
    public  synchronized void VerbindungsaufbauREQDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: VerbindungsaufbauREQ("+ici.toString()+","+"––"+")");
        if(client  != null){
            this.clientVerbindung = new ClientVerbindung();

            // öffnet eine neue Verbindung und erzeugt dabei einen neuen Connection Handler-Thread
            Socket socket = clientVerbindung.openConnection(ici);

            // wenn der Verbindungsaufbau erfolgreich war
            if (socket != null){
                System.out.print("ChatAnwendungsschicht: VerbindungsaufbauREQ: add("+socket.toString()+") ");
                System.out.println(" socketList="+socketListe.toString());
                getSocketZustand(socket); // socket wird so socketList eingefügt
                //client.VerbindungsaufbauCONF(ici, null);
                System.out.println("ClientType: "+ client); //TEST
                // lösche in der ici ip und port UND setzte stattdessen die socket
                iciUpdateSocket(ici,socket); 

            }else{
                // Verbindungsaufbau fehlgeschlagen
                throw new ConnectionException();
                //VerbindungsaufbauCONF(new ICI(null,StatusTyp.REJECT), null);
            }
            // verständige den Dienstbenutzer über einen erfolgreichen Verbindungsaufbau ici.socket gesetzt 
            // ODER
            // verständige den Dienstbenutzer über einen erfolglosen Verbindungsaufbau ici.socket nicht gesetzt (ici.socket == null) 

        }else {
            System.err.println("VerbindungsaufbauREQ: kein Client");
        }
    }

    /**
     * Rücksprungmethode, wenn der ServerListerner eine Verbindung aufgebaut hat.
     * Wenn für <code>ici.socket</code> kein Zustand existiert, wird der Zustand Unverbunden erzeugt.
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsaufbauIND(ICI ici,SDU sdu)  throws Exception
    {
        getSocketZustand(ici.socket).zustand.VerbindungsaufbauIND(this,ici,sdu);
    }    

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Rücksprungmethode, wenn der ServerListerner eine Verbindung aufgebaut hat.
     * Wenn für <code>ici.socket</code> kein Zustand existiert, wird der Zustand Unverbunden erzeugt.
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsaufbauINDDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: VerbindungsaufbauIND("+ici.toString()+","+"––"+")");
        if(server  != null){
            server.VerbindungsaufbauIND(ici,sdu);
            Socket newSocket = ici.socket;
            if (newSocket != null){
                //socketListe.add(new SocketZustand(newSocket,TCPVerbundenServer.getSingelton()));
            }
        } else {
            System.err.println("VerbindungsaufbauIND: kein Server");
        }
    }

    /**
     * Server antwortet dem Client
     * @param ici IP-Adresse in <code>ici.ip</code> und Portangabe in <code>ici.port</code>
     * @param sdu wird an Partnerinstanz übertragen
     */
    public synchronized void VerbindungsaufbauRESP(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.VerbindungsaufbauRESP(this,ici,sdu);
    }   

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Server antwortet dem Client
     * @param ici IP-Adresse in <code>ici.ip</code> und Portangabe in <code>ici.port</code>
     * @param sdu wird an Partnerinstanz übertragen
     */
    public  synchronized void VerbindungsaufbauRESPDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: VerbindungsaufbauRESP("+ici.toString()+","+sdu.text+")");
        if(server  != null){
            PDU pdu = new PDU("VerbindungsaufbauAntwort",sdu); // erzeuge eine PDU mit dem Header "Text" und einen Datenteil mit sdu.text
            send(ici,pdu); // verschicke die PDU für diese Verbindung 
        }else {
            System.err.println("VerbindungsaufbauRESP: kein Server");
        }
    }

    /**
     * Bestätigt den Verbindungsaufbau des Client dem Dienstbenutzer (Client).
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsaufbauCONF(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: VerbindungsaufbauCONF aufgerufen");
        getSocketZustand(ici.socket).zustand.VerbindungsaufbauCONF(this,ici,sdu);
    }    

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Meldet den Verbindungsabbauwunsch beim Server an.
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsaufbauCONFDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: VerbindungsaufbauCONF("+ici.toString()+","+"––"+")");
        if(client  != null){
            client.VerbindungsaufbauCONF(ici,sdu);
        }else {
            System.err.println("VerbindungsaufbauCONF: kein Client");
        }
    }

    /**
     * Meldet den Verbindungsabbauwunsch beim Server an.
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsabbauAnfrageREQ(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.VerbindungsabbauAnfrageREQ(this,ici,sdu);
    }        

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Meldet den Verbindungsabbauwunsch beim Server an.
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsabbauAnfrageREQDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: VerbindungsabbauAnfrageREQ("+ici.toString()+","+"––"+")");
        if(client  != null){
            PDU pdu = new PDU("VerbindungsabbauAnfrage",sdu); // erzeuge eine PDU mit dem Header "VerbindungsabbauAnfrage"

            send(ici,pdu); // verschicke die PDU für diese Verbindung 
        }else {
            System.err.println("VerbindungsabbauREQ: kein Client");
        }
    }

    /**
     * Meldet den Verbindungsabbauwunsch des Client dem Dienstbenutzer (Server).
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsabbauAnfrageIND(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.VerbindungsabbauAnfrageIND(this,ici,sdu);
    }        

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Meldet den Verbindungsabbauwunsch des Client dem Dienstbenutzer (Server).
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsabbauAnfrageINDDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: VerbindungsabbauAnfrageIND("+ici.toString()+","+"––"+")");
        if(server  != null){
            server.VerbindungsabbauAnfrageIND(ici,sdu); // meldet dem server den Verbindungsabbauwunsch des Clients
        }else {
            System.err.println("VerbindungsabbauAnfrageIND: kein Server");
        }
    }

    /**
     * Fordert einen Verbindungsabbau an.
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsabbauREQ(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.VerbindungsabbauREQ(this,ici,sdu);
    }        

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Fordert einen Verbindungsabbau an.
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsabbauREQDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: VerbindungsabbauREQ("+ici.toString()+","+"––"+")");
        if(server != null){
            PDU pdu = new PDU("Verbindungsabbau",sdu); // erzeuge eine PDU mit dem Header "Verbindungsabbau"

            send(ici,pdu); // verschicke die PDU für diese Verbindung 
        }else {
            System.err.println("VerbindungsabbauREQ: kein Server");
        }
    }

    /**
     * Zeigt einen Verbindungsabbau an und gibt diesen an den Dienstbenutzer (Client) weiter.
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsabbauIND(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.VerbindungsabbauIND(this,ici,sdu);
    }        

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Zeigt einen Verbindungsabbau an und gibt diesen an den Dienstbenutzer (Client) weiter.
     * @param ici Verbindung
     * @param sdu wird nicht genutzt
     */
    public synchronized void VerbindungsabbauINDDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: VerbindungsabbauIND("+ici.toString()+","+"––"+")");
        if(client  != null){
            client.VerbindungsabbauIND(ici,sdu); // gibt die Verbindungsabbau-Anzeige an den client weiter
        }else {
            System.err.println("VerbindungsabbauIND: kein Client");
        }
    }

    //---
    /**
     * Fordert die Übertragung des NickNames an den Client an.
     * @param ici Verbindung
     * @param sdu NickName
     */
    public synchronized void NickNameREQ(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.NickNameREQ(this,ici,sdu);
    }

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Fordert die Übertragung des NickNames an den Client an.
     * @param ici Verbindung
     * @param sdu NickName
     */
    public synchronized void NickNameREQDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: NickNameREQ("+ici.toString()+","+sdu.text+")");
        if(client != null){
            PDU pdu = new PDU("NickNameAnfrage",sdu); // erzeuge eine PDU mit dem Header "NickNameAnfrage" und einen Datenteil mit sdu.text

            send(ici,pdu); // verschicke die PDU für diese Verbindung 
        }else {
            System.err.println("NickNameREQDO: kein Client");
        }
    }

    /**
     * Fordert die überprüfung des NickNames an
     * @param ici Verbindung
     * @param sdu NickName
     */
    public synchronized void NickNameIND(ICI ici,SDU sdu)  throws Exception
    {
        getSocketZustand(ici.socket).zustand.NickNameIND(this,ici,sdu);
    }

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Meldet den NickName dem Server zur Überprüfung.
     * @param ici Verbindung
     * @param sdu NickName
     */
    public synchronized void NickNameINDDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: NickNameIND("+ici.toString()+","+"––"+")");
        if(server != null){
            server.NickNameIND(ici,sdu); // meldet dem server Namenswunsch des Clients
        }else {
            System.err.println("NickNameIND: kein Server");
        }
    }

    /**
     * Fordert die Übertragung von der Annahme/Ablehnung des NickNames an den Client an.
     * @param ici Verbindung
     * @param sdu Information ob doppelt
     */
    public synchronized void NickNameRESP(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.NickNameRESP(this,ici,sdu);
    }

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Annahme/Ablehnung des NickNames
     * @param ici Verbindung
     * @param sdu Information ob doppelt
     */
    public synchronized void NickNameRESPDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: TextREQ("+ici.toString()+","+sdu.text+")");
        if(server  != null){
            PDU pdu = new PDU("NickNameAntwort",sdu); // erzeuge eine PDU mit dem Header "Text" und einen Datenteil mit sdu.text

            send(ici,pdu); // verschicke die PDU für diese Verbindung 
        }else {
            System.err.println("NickNameRESPDO: kein Server");
        }
    }

    /**
     * Fordert die Meldung der Ergebnisse dem Dienstbenutzer (Client) an.
     * @param ici Verbindung
     * @param sdu Information ob doppelt
     */
    public synchronized void NickNameCONF(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.NickNameCONF(this,ici,sdu);
    }

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Meldet das Ergebniss dem Dienstbenutzer (Client).
     * @param ici Verbindung
     * @param sdu Information ob doppelt
     */
    public synchronized void NickNameCONFDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: NickNameCONF("+ici.toString()+","+"––"+")");
        if(client  != null){
            client.NickNameCONF(ici,sdu); // meldet dem Clienten die Antwort des Servers
        }else {
            System.err.println("NickNameCONFDO: kein Client");
        }
    }

    /**
     * Fordert die Übertragung von Text an den Client an.
     * @param ici Verbindung
     * @param sdu Text, der übertragen wird
     */
    public synchronized void TextREQ(ICI ici,SDU sdu)  throws Exception
    {
        getSocketZustand(ici.socket).zustand.TextREQ(this,ici,sdu);
    }        

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Fordert die Übertragung von Text an den Client an.
     * @param ici Verbindung
     * @param sdu Text, der übertragen wird
     */
    public synchronized void TextREQDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: TextREQ("+ici.toString()+","+sdu.text+")");
        if(server  != null){
            PDU pdu = new PDU("Text",sdu); // erzeuge eine PDU mit dem Header "Text" und einen Datenteil mit sdu.text

            send(ici,pdu); // verschicke die PDU für diese Verbindung 
        }else {
            System.err.println("TextREQ: kein Server");
        }
    }

    /**
     * Fordert den Übertragungswunsch für einen Text beim Server an.
     * @param ici Verbindung
     * @param sdu Text, der übertragen werden soll
     */
    public synchronized void TextAnmeldenREQ(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.TextAnmeldenREQ(this,ici,sdu);
    }        

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Fordert den Übertragungswunsch für einen Text beim Server an.
     * @param ici Verbindung
     * @param sdu Text, der übertragen werden soll
     */
    public synchronized void TextAnmeldenREQDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: TextAnmeldenREQ("+ici.toString()+","+sdu.text+")");
        if(client  != null){
            PDU pdu = new PDU("TextAnmeldung",sdu); // erzeuge eine PDU mit dem Header "TextAnmeldung" und einen Datenteil mit sdu.text
            System.out.println("ChatAnwendungsschicht: TextAnmeldenREQDO: Rot: "+sdu.red+", Gruen: "+sdu.green+", Blau:"+sdu.blue);
            send(ici,pdu); // verschicke die PDU für diese Verbindung 
        }else {
            System.err.println("TextAnmeldenREQ: kein Client");
        }
    }

    /**
     * Zeigt eine Übertragung von Text an und gibt diesen an den Dienstbenutzer (Client) weiter.
     * @param ici Verbindung
     * @param sdu Text, der übertragen wurde
     */
    public synchronized void TextIND(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.TextIND(this,ici,sdu);
    }        

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Zeigt eine Übertragung von Text an und gibt diesen an den Dienstbenutzer (Client) weiter.
     * @param ici Verbindung
     * @param sdu Text, der übertragen wurde
     */
    public synchronized void TextINDDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: TextIND("+ici.toString()+","+sdu.text+")");
        if(client  != null){
            client.TextIND(ici,sdu); // gibt die Text-Anzeige an den client weiter
            System.out.println(sdu.red);
            System.out.println(sdu.green);
            System.out.println(sdu.blue);
        }else {
            System.err.println("TextIND: kein Client");
        }
    }

    /**
     * Zeigt Übertragungswunsch für einen Text an und gibt diesen an den Dienstbenutzer (Server) weiter.
     * @param ici Verbindung
     * @param sdu Text, der übertragen werden soll
     */
    public synchronized void TextAnmeldenIND(ICI ici,SDU sdu) throws Exception
    {
        getSocketZustand(ici.socket).zustand.TextAnmeldenIND(this,ici,sdu);
    }        

    /**
     * Wird von einem Zustand aufgerufen.
     * <p>
     * Zeigt Übertragungswunsch für einen Text an und gibt diesen an den Dienstbenutzer (Server) weiter.
     * @param ici Verbindung
     * @param sdu Text, der übertragen werden soll
     */
    public synchronized void TextAnmeldenINDDO(ICI ici,SDU sdu) throws Exception
    {
        System.out.println("ChatAnwendungsschicht: TextAnmeldenIND("+ici.toString()+","+sdu.text+")");
        if(server  != null){
            server.TextAnmeldenIND(ici,sdu); // gibt die Textwunsch-Anzeige an den server weiter
        }else {
            System.err.println("TextAnmeldenIND: kein Server");
        }
    }

    /**
     * Diese Methode nicht aufrufen. Sie wird über start() gestartet (s. Thread).
     * <p>
     * <b>Die Bearbeitung der eintreffenden Nachrichten als eigener Thread </b>
     * <p>
     * Kommen Nachrichten vom entfernten System auf dieser Seite an, 
     * so werden diese vom ConnectionHandler in einen Kommunikationspuffer geschoben.
     * Aus diesem Kommunikationspuffer müssen die Nachrichten gelesen werden und an die entsprechenden Methoden weitergeleitet werden.
     * Über die Header in den Nachrichten werden die Methoden ausgewählt.
     */
    @Override
    public void run() { 
        String inhalt = null;
        int anzahlPunkte = 0;
        while(isActive){
            synchronized(this)
            {
                // wenn Sockets geöffnet sind
                if (socketListe.size()>0)    
                // für jede Socket in der Liste
                    for(SocketZustand socketZustand : socketListe){
                        // bestimme die Socket
                        Socket socket = socketZustand.socket;
                        if (socket != null){
                            Kommunikationspuffer pufferEin =  Kommunikationspuffer.getEingang(socket); // bestimme den Socket-spezifischen Eingangspuffer

                            // wenn der Eingangspuffer nicht leer ist
                            if(pufferEin.size()>0){
                                inhalt = pufferEin.get(); // hole den inhalt des ersten Elements
                            }

                            // wenn der inhalt (des ersten Elements) nicht leer ist
                            if (inhalt != null){
                                System.out.println("ChatAnwendungsschicht: run: "+inhalt);

                                ICI ici = new ICI(socket); // erzeuge eine Verbindungsidentifikation
                                PDU pdu = new PDU(inhalt); // zerlege den inhalt nach Header und Datenteil (pdu.header und pdu.sdu)
                                try{
                                    SDU sdu = new SDU(pdu.sdu); // speichere den Datenteil als sdu
                                    // verzweige nach dem Header der Nachricht in die entsprechenden Methoden
                                    if (pdu.header.equalsIgnoreCase("VerbindungsaufbauAntwort")){VerbindungsaufbauCONF(ici,sdu);}
                                    if (pdu.header.equalsIgnoreCase("Text")){TextIND(ici,sdu);}
                                    if (pdu.header.equalsIgnoreCase("TextAnmeldung")){TextAnmeldenIND(ici,sdu);}
                                    if (pdu.header.equalsIgnoreCase("VerbindungsabbauAnfrage")){VerbindungsabbauAnfrageIND(ici,sdu);}
                                    if (pdu.header.equalsIgnoreCase("Verbindungsabbau")){VerbindungsabbauIND(ici,sdu);}
                                    if (pdu.header.equalsIgnoreCase("NickNameAnfrage")){NickNameIND(ici,sdu);}
                                    if (pdu.header.equalsIgnoreCase("NickNameAntwort")){NickNameCONF(ici,sdu);}

                                } catch(Exception e){
                                    e.printStackTrace();
                                }

                                inhalt = null; // lösche den inhalt, damit er nicht nochmals benutzt wird

                            }
                        } else {
                            //System.err.println("ChatAnwendungsschicht: Socket null.");
                        }
                    }
            }
        }

        System.out.println("ChatAnwendungsschicht beendet.");

    }

    /**
     * Lege die Nachricht im Ausgangspuffer für diese Verbindung ab.
     * <p>
     * Der ConnectionHandler-Thread (für diese Verbindung) wird diese Nachricht aus dem Puffer abholen und an den Empfänger schicken.
     * @param ici Verbindung
     * @param pdu PDU, die übertragen werden soll
     */
    private synchronized void send(ICI ici,PDU pdu){
        System.out.println("ChatAnwendungsschicht: send("+pdu.getPDU()+")");

        Kommunikationspuffer.getAusgang(ici.socket).add(pdu.getPDU()); 

    }    

}

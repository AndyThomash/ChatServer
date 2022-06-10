import java.net.Socket;

/**
 * Eine Information Control Interface wird nicht über das Netzwerk übertragen sondern steuert die Bearbeitung des Dienstes.
 * <p>
 * Es gibt die Belegungen:
 * <ul>
 * <li> port : Die ServerSocket soll initialisert werden.
 * <li> ip, port : Eine Verbindung zum Server soll hergestellt werden.
 * <li> socket : Die Verbindung ist hergestellt.
 * </ul>
 * <p>
 * Die update-Methode wechselt von der Belegung (ip,port) zu (socket).
 * 
 * @author LK, Leo G., Marika K. Dave P. Lando A.
 * @version 2022-06-08
 */
public class ICI
{
    public static final int NULLPORT = -1;

    // public-Attribute erlauben den direkten Zugriff von außen
    public Socket socket; // Socket-Objekte dienen als Identifikatoren für Verbindungen
    public String ip;  // IP-Adresse des Servers
    public int port; // Port des Servers
    public StatusTyp status;

    /**
     * Konstruktor für Objekte der Klasse ICI
     * @param socket Socket der Verbindung
     */
    public ICI(Socket socket)
    {
        // Instanzvariable initialisieren
        this.socket = socket;
        this.ip = null;
        this.port = NULLPORT;
        this.status = StatusTyp.ACCEPT;
        //this.broadcast = false;
    }

    /**
     * Konstruktor für Objekte der Klasse ICI
     * @param ip IP-Adresse des Servers
     * @param port Port des Servers
     */
    public ICI(String ip, int port)
    {
        // Instanzvariable initialisieren
        this.socket = null;
        this.ip = ip;
        this.port = port;
        this.status = StatusTyp.ACCEPT;
        //this.broadcast = false;
    }

    /**
     * Konstruktor für Objekte der Klasse ICI
     * @param port Port der ServerSocket des Servers
     */
    public ICI(int port)
    {
        // Instanzvariable initialisieren
        this.socket = null;
        this.ip = null;
        this.port = port;
        this.status = StatusTyp.ACCEPT;
        //this.broadcast = false;
    }

    public ICI(Socket socket, StatusTyp status)
    {
        // Instanzvariable initialisieren
        this.socket = socket;
        this.ip = null;
        this.port = NULLPORT;
        this.status = status;
    }

    /**
     * Wechselt von der ip,port-Belegung in die socket-Belegung.
     * @param socket Socket der Verbindung
     * @param status Status des Dienstes
     */
    public void update(Socket socket){
        if (ip != null && port != NULLPORT&&socket != null){
            this.socket = socket;
            this.ip = null;
            this.port = NULLPORT;    
        } else {
            System.err.println("ICI: update-Error");
        }
    }

    /**
     * Ausgabe des Inhalts eines ICI als String.
     * Überschreibt die toString()-Methode von Object.
     * @return Ausgabetext des Inhalts der Struktur
     */
    @Override
    public String toString(){

        String iciString = "";

        if (socket!=null){
            iciString = socket.toString();
        } else {
            if (ip!=null && !ip.equalsIgnoreCase("")){
                iciString = "("+ip+","+port+")";
            } else {
                iciString = ""+port;
            }

        }

        if (status != StatusTyp.NO_STATUS){
            iciString = iciString + status.toString();
        }

        return iciString;

    }

}

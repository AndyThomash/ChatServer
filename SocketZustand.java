import java.net.Socket;

/**
 * Beschreiben Sie hier die Klasse SocketZustand.
 * 
 * @author LK, Leo G., Marika K. Dave P. Lando A.
 * @version 2022-06-08
 */
public class SocketZustand
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    public Socket socket;
    public ChatAnwendungsschichtZustand zustand;

    /**
     * Konstruktor für Objekte der Klasse SocketZustand
     */
    public SocketZustand(Socket socket, ChatAnwendungsschichtZustand zustand)
    {
        this.socket = socket;
        this.zustand = zustand;
    }

    /**
     * 
     */
    public void nextState(ChatAnwendungsschichtZustand zustand)
    {
        this.zustand = zustand;
    }

    /**
     * 
     */
    public String toString(){
        String socketString;
        String zustandString = zustand.name;
        if (socket == null){
            socketString = "nullSocket";
        } else {
            socketString = socket.toString();
        }

        return socketString +" : "+ zustandString;

    }
}

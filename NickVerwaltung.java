import java.net.Socket;
import java.util.*;

/**
 * Verwaltet die Nicknames Serverseitig mithilfe einer noch
 * nicht existierenden HashMap.
 */

public class NickVerwaltung
{
    // Creating an empty HashMap
    HashMap<Socket, String> nickname = new HashMap<Socket, String>();

    public NickVerwaltung() {

    }

    /**
     * Methode zur Verarbeitung des eingegebenen Nicknames.
     * @param name Nickname
     * @param socket Socket
     * @return booloan 
     */
    public boolean putNickname(Socket socket, String name) {
        Collection<String> nicks  = nickname.values();

        boolean notfound = true;
        for(String nick : nicks){
            if(nick.equals(name)){
                notfound = false;
            }
        }

        if (notfound) {
            nickname.put(socket,name);
        }
        return notfound;
    }

    /**
     * Methode zum Herausfinden des Nicknames.
     * @param socket socket
     * @return Socket or Null
     */
    public String getNickname(Socket socket){
        return nickname.get(socket);
    }

}

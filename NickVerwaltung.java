import java.net.Socket;

/**
 * Verwaltet die Nicknames Serverseitig mithilfe einer noch
 * nicht existierenden HashMap.
 */

public class NickVerwaltung
{

 public NickVerwaltung() {

 }


    /**
     * Methode zur Verarbeitung des eingegebenen Nicknames.
     * @param name Nickname
     * @param socket Socket
     * @return Name und Socket
     */
 public String putNickname(String name, Socket socket) {
     return "test";
 }


    /**
     * Methode zum Herausfinden des Nicknames.
     * @param socket socket
     * @return Socket or Null
     */
 public String getNickname(Socket socket){
     return null;
 }


}

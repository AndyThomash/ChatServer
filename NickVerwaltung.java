import java.net.Socket;
import java.util.*;

/**
 * Verwaltet die Nicknames Serverseitig mithilfe einer noch
 * nicht existierenden HashMap.
 */

public class NickVerwaltung
{

    public static void main(String[] args)
    {

        // Creating an empty HashMap
        HashMap<Socket, String> nickname = new HashMap<Socket, String>();
        //**
        // Mapping string values to int keys
        //nickname.put(10, "Geeks");
        //nickname.put(15, "4");
        //nickname.put(20, "Geeks");
        //nickname.put(25, "Welcomes");
        //nickname.put(30, "You");

        // Displaying the HashMap
        System.out.println("Initial Mappings are: " + nickname);

        // Inserting existing key along with new value
        //String returned_value = (String)nickname.put(20, "All");

        // Verifying the returned value
        //System.out.println("Returned value is: " + returned_value);

        // Displaying the new map
        System.out.println("New map is: " + nickname);
    }


     public NickVerwaltung() {
         
     }
     /**
     * Methode zur Verarbeitung des eingegebenen Nicknames.
     * @param name Nickname
     * @param socket Socket
     * @return Name und Socket
     */
     public String putNickname(String name, Socket socket) {
         return "acc & decl";
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

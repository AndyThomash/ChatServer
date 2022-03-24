import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 

/**
 * Ein ClientHandler ist für genau eine Verbindung (Socket) zuständig.
 * Er soll alle eingehenden TCP-Pakete unverändert in den Eingangspuffer einfügen.
 * Alle Pakte im Ausgangspuffer werden über TCP verschickt.
 * 
 * @author LK
 * @version 2021-11-17
 */
class ConnectionHandler extends Thread  
{ 
    // Instanzvariablen
    
    // TCP-Richtung 
    Socket mySocket; // Socket für die Verbindung
    InputStream is;  // Eingabebytestrom
    BufferedReader buff; // puffert den Eingabestrom, so dass zeilenweise gelesen werden kann 
    PrintStream myPrintStream; // Ausgabestrom mit print- und println-Methoden (wie bei System.out)

    // Anwendungsschicht-Richtung
    Kommunikationspuffer eingang; // Nachrichtenpuffer für die Nachrichten, die zur Anwendungsschicht transportiert werden
    Kommunikationspuffer ausgang; // Nachrichtenpuffer für die Nachrichten, die von der Anwednungsschicht kommen

    boolean isActive = true; // true, solange die Verbindung aktiv ist

    public ConnectionHandler(Socket s)  
    { 
        // wenn s eine Socket ist
        if (s!=null){
            this.mySocket = s; 

            try{
                is = mySocket.getInputStream();
                buff = new BufferedReader(new InputStreamReader(is));
                myPrintStream = new PrintStream(mySocket.getOutputStream(),true);            
            } catch (IOException e) { 
                e.printStackTrace(); 
                isActive = false;
            } 

            eingang=Kommunikationspuffer.getEingang(mySocket);
            ausgang=Kommunikationspuffer.getAusgang(mySocket);
        } else {
            isActive = false;
        }

    }

    /**
     * Diese Methode nicht aufrufen. Sie wird über start() gestartet (s. Thread).
     * <p>
     * <b>Die Bearbeitung der eintreffenden Nachrichten als eigener Thread</b> 
     * <p>
     * Kommen Nachrichten vom entfernten System über den TCP-Eingabestrom auf dieser Seite an, 
     * so werden diese hier gelesen und in einen Eingangskommunikationspuffer für die Anwendungsschicht geschoben.
     * <p>
     * Werden Nachrichten von der Anwendunsgschicht in den Ausgangskommunikationspuffer geschrieben, so werde diese hier gelesen und an den Ausgabestrom von TCP weitergereicht. 
     */
    @Override
    public void run()  
    { 

        if (isActive) System.out.println("ConnectionHandler("+this.mySocket.toString()+"): Just started.");

        while (isActive) {

            try { 
                synchronized(this)
                {
                    // wenn etwas im buff steht
                    if (buff.ready()) { 
                        String gelesen = buff.readLine(); // lies eine Zeile
                        eingang.add(gelesen);  // schiebe das gelesene in den Eingangspuffer
                        //System.out.println("ConnectionHandler("+this.mySocket.toString()+"): input: "+gelesen);
                    }
                }
            } catch (IOException e) { 
                System.out.println("ConnectionHandler: IOProbleme beim Lesen."); 
                e.printStackTrace(); 
            } 
            synchronized(this)
            {
                String sendung = ausgang.get(); // hole sendung aus dem Ausgangspuffer
                // wenn das sendung nicht leer ist
                if (sendung != null){
                    myPrintStream.println(sendung); // schreibe die sendung in den Ausgabestrom
                    //System.out.println("ConnectionHandler("+this.mySocket.toString()+"): output: "+sendung);
                }
            }

        }
        
        // die Schleife wird verlassen, wenn der ConnectionHandler nicht mehr aktiv ist
        
        if (mySocket!=null) System.out.println("ConnectionHandler("+this.mySocket.toString()+"): Thread am Ende.");
        else System.out.println("ConnectionHandler(null): Thread am Ende.");
    }

    /**
     * Schließt die Verbindung.
     * Die zur Verbindung gehörende Socket wird ebenfalls geschlossen.
     */
    public void close(){
        isActive = false; // Die Dauerschleife des Thread soll verlassen werden.
               
        if (mySocket != null){
            System.out.println("ConnectionHandler("+this.mySocket.toString()+"): close()");
            
            try{ sleep(10000); } catch(Exception ie) { ie.printStackTrace(); } // schlafe 10000 ms

            synchronized(this)
            {
                try { 
                    mySocket.close(); // schließ die mySocket
                } catch (IOException e) { 
                    e.printStackTrace(); 
                }  
            }
        } else {
            System.out.println("ConnectionHandler(null): close()");
        }

    }

}
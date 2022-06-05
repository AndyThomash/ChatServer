import javax.swing.*;
import java.awt.Color;
import java.util.concurrent.TimeUnit;

/**
 * In einem Eingabefenster kann ein Zeile für die Chatkonferenz eingegeben werden.
 * Nach dem Abschicken wird diese an alle anderen Konferenzteilnehmer verteilt.
 * 
 * Da ein Schließen des Fensters die Zeichenkette "null" sendet, wurde die Ausgabe von "null" abgfangen.
 * "null" wird nicht dargestellt (Groß- und Kleinschreibung wird nicht beachtet).
 * 
 * @author LK
 * @version 2021-09-30
 */
public class Eingabefenster extends Thread {

    // Instanzvariable
    private JFrame fenster = new JFrame();

    private boolean isActive = true; // true, solange das Eingabefenster aktiv ist
    
    private boolean FrageNachNickname = true;
    private boolean FrageNachColor = false;
    
    private ChatClient client;  // Client, an den alle Texte weitergereicht werden

    public Eingabefenster(ChatClient client) {
        this.client = client;
    }

    /**
     * Zeigt den Dialog an.
     * @return eingegebener Text oder "null", wenn das Fenster mit Cancel geschlossen wird
     */
    public String ask(){
        String text=JOptionPane.showInputDialog(fenster,"Eingabe");
        return text;
    }

    /**
     * Diese Methode nicht aufrufen. Sie wird über start() gestartet (s. Thread).
     * <p> 
     * <b> Fragt nach Nickname wenn Nickname noch nicht festgelegt f</b>
     * <p> 
     * <b>Wiederholtes Anzeigen des Dialogfensters</b>
     * <b></b>
     * 
     */
    @Override
    public void run() { 
        System.out.println("Eingabefenster-Thread gestartet.");
        
        // solange die Eingabe aktiv sein soll
        while(isActive){
            if (FrageNachNickname){
                String username = JOptionPane.showInputDialog(fenster,"Bitte geben Sie einen Benutzernamen ein.");
                client.sendUserdata(username);
                try
                {
                    TimeUnit.SECONDS.sleep(5);
                }
                catch (InterruptedException ie)
                {
                    ie.printStackTrace();
                }
            }else if(FrageNachColor){
                String color = JOptionPane.showInputDialog(fenster,"Bitte geben Sie eine Farbe als RGB-Wert an. Beispiel: 255,255,255. Die Farbe kann jederzeit mit CHANGE_COLOR geändert werden.");
                String[] colorArray = color.split(",",3);
                try{
                    int red = Integer.parseInt(colorArray[0]);
                    int green = Integer.parseInt(colorArray[1]);
                    int blue = Integer.parseInt(colorArray[2]);
                    if (red >= 0 && red <= 255 && green >= 0 && green <= 255 && blue >= 0 && blue <= 255){
                        client.setColor(red,green,blue);
                        try
                        {
                            TimeUnit.SECONDS.sleep(5);
                        }
                        catch (InterruptedException ie)
                        {
                            ie.printStackTrace();
                        }
                    }else{
                        System.out.println("Bitte geben Sie 3 Zahlen zwischen 0 und 255 ein!");
                    }
                }
                catch (NumberFormatException ex){
                    System.out.println("Bitte geben Sie 3 Zahlen zwischen 0 und 255 ein!");
                }
            }else{
                String antwort = ask(); // ruf den Dialog auf
                // wenn es eine gültige antwort gibt
                if (antwort != null && !antwort.equalsIgnoreCase("null")){
                    client.send(antwort); // schicke die antwort an den client
                }
                try
                        {
                            TimeUnit.SECONDS.sleep(5);
                        }
                        catch (InterruptedException ie)
                        {
                            ie.printStackTrace();
                        }
            }
        }
        
        // die Schleife wird verlassen, wenn das Eingabefenster nicht mehr aktiv ist
        
        System.out.println("Eingabefenster wurde beendet.");
    }

    /**
     * Schließt das Eingabefenster.
     */
    public void close(){
        System.out.println("Eingabefenster: close().");
        isActive = false;
        fenster.dispose(); // schließe das Fenster

    }
    
    /**
     * Set Methode, um von der Frage nach dem Nicknamen zur normalen Eingabe zu  wechseln.
     */
    public void unactiveFrageNachNickname(){
        FrageNachNickname = false;
    }
    
    /**
     * Set-Methode, um zwischen Farbeingabe und normaler Eingabe zu welchseln.
     */
    public void frageNachColor(boolean bool){
        FrageNachColor = bool;
    }
}
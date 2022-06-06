import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;

/**
 * Im Anzeigefenster werden die Zeilen des Chats angezeigt.
 * 
 * Sind alle Zeilen belegt, beginnt die Anzeige der nächsten Zeile wieder mit der obersten Zeile.
 * 
 * @author LK
 * @version 2021-09-30
 */
public class Anzeigefenster extends JFrame {

    // Instanzvariablen
    JLabel[] labelArr = new JLabel[20]; // 20 Zeilen, die als Ringpuffer implementiert werden.
                                        // Ringpuffer: Wenn die letzte Zeile erreicht ist, wird bei der ersten wieder begonnen. 
                                        //             Die restlichen Zeilen bleiben dabei erhalten.
    int aktpos = 0; // aktuelle Zeile wird initialisiert mit 0 (1. Zeile)

    /**
     * Erzeugt das Anzeigefenster.
     */
    public Anzeigefenster() {
        setLayout(null);
        setVisible(true);
        setSize(800, 430);
        setTitle("Chat Client");
        setResizable(false);
        //setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        //setBackground(bgColor);
        
        // zeige alle Zeilen an.
        for (int i=0;i<labelArr.length;i++){
            labelArr[i] = new JLabel("");
            anzeigen(labelArr[i],i);
        }
    }
    
    // zeige eine Zeile an
    // label: Inhalt der Zeile
    // pos: Position der Zeile (zwischen 0 und 19)
    private void anzeigen(JLabel label, int pos){       
        label.setBounds(5,20*pos,790,18);
        add(label);
    }
    
    /**
     * Schreibe den Text in die aktuelle Zeile.
     * Die aktuelle Zeile wechselt bei einem Überlauf wieder in die erste Zeile.
     * @param text Text für die aktuelle Zeile
     */
    public void show(SDU sdu){
        int rot = sdu.red;
        int gruen = sdu.green;
        int blau = sdu.blue;
        
        labelArr[aktpos].setForeground(new Color(rot, gruen, blau));
        labelArr[aktpos].setText(sdu.text);
        aktpos++;                   // erhöhe die aktuelle Zeile um 1
        aktpos = aktpos % 20;       // berechne die neue Zeile modulo 20 
    }
    
    /**
     * Schließt das Anzeigefenster.
     */
    public void close(){
        System.out.println("Ausgabefenster: close()");
        dispose(); // schließt das Anzeigefenster
    }
    
}
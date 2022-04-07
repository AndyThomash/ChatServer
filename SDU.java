
/**
 * Service Data Unit sind Inhalte, die über das Netzwerk transportiert werden sollen.
 * Diese sind (bei uns) strukturiert in Text und RGB-Werten.
 * 
 * Die RGB-Werte können nicht gesetzt sein.
 * 
 * Eine SDU kann die Struktur in Text und RGB-Werte auch aufheben und die RGB-Werte im Text kodieren und dekodieren.
 * 
 * @author LK
 * @version 2021-09-30
 */
public class SDU
{
    public static final int NULLCOLOR = -1;

    // Text
    public String text;
    // Farbwerte oder (-1,-1,-1) als Null-Wert (kein Wert)
    public int red;
    public int green;
    public int blue;

    /**
     * Konstruktor für Objekte der Klasse SDU
     */
    public SDU(String text)
    {
        String[] strArray = text.split("µ~#§$",4);
        
        if(strArray.length == 1){
            this.text = text;
            
            this.red   =NULLCOLOR;
            this.green =NULLCOLOR;
            this.blue  =NULLCOLOR;
        }else{
            toColor();
        }
    }

    public SDU(String text, int red, int green, int blue)
    {
        // Instanzvariable initialisieren
        this.text   = text;
        this.red    = red;
        this.green  = green;
        this.blue   = blue;
    }

    public void toText(){
        if (red != NULLCOLOR){
            this.text = text+"µ~#§$"+red+"µ~#§$"+green+"µ~#§$"+blue;
            this.red   =NULLCOLOR;
            this.green =NULLCOLOR;
            this.blue  =NULLCOLOR;
        }
    }

    public void toColor(){
        String[] strArray = text.split("µ~#§$",4);

        if (red == NULLCOLOR && strArray.length == 4){
            this.text  = strArray[0];
            this.red   = Integer.parseInt(strArray[1]);
            this.green = Integer.parseInt(strArray[2]);
            this.blue  = Integer.parseInt(strArray[3]);
        }
    }
}
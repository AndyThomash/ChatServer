
/**
 * Service Data Unit sind Inhalte, die über das Netzwerk transportiert werden sollen.
 * Diese sind (bei uns) strukturiert in Text und RGB-Werten.
 * 
 * Die RGB-Werte können nicht gesetzt sein.
 * 
 * Eine SDU kann die Struktur in Text und RGB-Werte auch aufheben und die RGB-Werte im Text kodieren und dekodieren.
 * 
 * @author LK, Leo G., Marika K. Dave P. Lando A.
 * @version 2022-06-08
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
     * @para text
     */
    public SDU(String text)
    {
        String[] strArray = text.split("###",4);

        if(strArray.length == 1){
            this.text = text;

            this.red   =NULLCOLOR;
            this.green =NULLCOLOR;
            this.blue  =NULLCOLOR;
        }else if(strArray.length == 4){
            this.text = strArray[0];
            this.red   = Integer.parseInt(strArray[1]);
            this.green = Integer.parseInt(strArray[2]);
            this.blue  = Integer.parseInt(strArray[3]);
        }else{
            toColor();
        }
    }

    /**
     * Konstruktor für Objekte der Klasse SDU
     * @para text
     * @para rot 
     * @para grün
     * @para blau
     */
    public SDU(String text, int red, int green, int blue)
    {
        // Instanzvariable initialisieren
        this.text   = text;
        this.red    = red;
        this.green  = green;
        this.blue   = blue;
    }

    /**
     * Fügt den Text und die Farben zu einem String zusammen.
     */
    public void toText(){
        if (red != NULLCOLOR){
            this.text = text+"###"+red+"###"+green+"###"+blue;
            this.red   =NULLCOLOR;
            this.green =NULLCOLOR;
            this.blue  =NULLCOLOR;
        }
    }

    /**
     * Trennt den Text und die Farben.
     */
    public void toColor(){
        String[] strArray = text.split("###",4);

        if (red == NULLCOLOR && strArray.length == 4){
            this.text  = strArray[0];
            this.red   = Integer.parseInt(strArray[1]);
            this.green = Integer.parseInt(strArray[2]);
            this.blue  = Integer.parseInt(strArray[3]);
        }
    }
}
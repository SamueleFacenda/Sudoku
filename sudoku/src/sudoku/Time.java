package sudoku;

/**
 * classe che fa da cronometro
 * @author Samuele Facenda
 */
public class Time {
    double time;
    public Time(){}

    /**
     * avvia il cronometro
     */
    public void start(){
        time=System.currentTimeMillis();
    }
    /**
     * @return il valore attuale del cronometro in millisecondi
     */
    public int get(){
        return (int) ((System.currentTimeMillis()-time)/1);
    }

    /**
     * riavvia il cronometro
     */
    public void restart(){
        time=System.currentTimeMillis();
    }
}

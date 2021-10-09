package sudoku;
/**
 * @author Samuele Facenda
 * @version 1.0
* classe per la memorizzazione di una valore e di possibili valori
* i valori vengono trattati come una specie di ArrayList
* si effetuano controlli in ogni metodo per prevenire azioni non volute
* su celle assegnate
 */
public class Cell {
    private int dimL,val;
    private int[] poss;
    private static int len;//la lunghezza é uguale per tutte le celle, così non va riassegnata ogni volta

    /**
     * @param val il valore della cella
     */
    public Cell(int val){
        this.val=val;
    }

    /**
     * @param len la lunghezza é un valore uguale per tutte le celle
     */
    public static void setLen(int len){Cell.len=len;}

    /**
     * genera una cella vuota con tutti i valori possibili
     */
    public Cell(){
        poss=new int[len];
        for(int i=0;i<len;i++)
            poss[i]=i+1;
        dimL=len;
        val=0;
    }

    /**
     * construttore copia-incolla
     * @param dimL il numero di possibilitá
     * @param val il valore della cella
     * @param poss le possibilitá
     */
    public Cell(int dimL,int val,int[] poss){
        this.dimL=dimL;
        this.val=val;
        this.poss=new int[dimL];
        for(int i=0;i<dimL;i++)
            this.poss[i]=poss[i];
    }
    /**
    *rimuove una possibilità dalla cella, se ne rimane solo una assegna il valore
    *e ritorna true, se il valore è già assegnato non fa niente
     * @return true se é rimasta una sola possiblitá e il valore é cosí stato asseganto
     * @param num la possiblitá da rimuovere
     */
    public boolean removePoss(int num){
        if(val==0) {
            int i = 0;
            //trovo il valore
            while (i<dimL&&poss[i] != num)
                i++;
            if(i<dimL) {
                dimL--;
                //shifto l'array
                while (i < dimL) {
                    poss[i] = poss[i + 1];
                    i++;
                }
                //se era l'ultimo
                if (dimL == 1) {
                    val = poss[0];
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return se la cella ha un valore assegnato
     */
    public boolean isAssigned(){return val!=0;}
    /**
     * @return il numero di possiblitá che ha la cella
     */
    public int getNumPoss(){
        return val==0?dimL:0;
    }
    /**
    *ritorna una copia delle possibilitá
    * se il valore non é ancora stato assegnato
     * @return array copia delle possibilitá
     */
    public int[] getPoss(){
        if(val==0) {
            int[] out = new int[dimL];
            for (int i = 0; i < dimL; i++)
                out[i] = poss[i];
            return out;
        }else
            return null;
    }

    /**
     * @return il valore della cella, 0 se non é stato assegnato
     */
    public int getVal(){return val;}

    /**
     * @param num il valore da assegnare alla cella, se non ne aveva uno
     */
    public void setVal(int num){
        if(hasPoss(num))val=num;
    }
    /**
    *controlla se il numero é presente tra le possibilitá
     * @param num il numero da controllare se é presente nelle possiblitá
     * @return se il numero é presente tra le possibilitá
     */
    public boolean hasPoss(int num){
        if(val==0) {
            int i = 0;
            while (i < dimL && poss[i] != num)
                i++;
            return i != dimL;
        }else
            return false;
    }

    /**
     * @return copia della cella
     */
    public Cell getCopy(){
        return new Cell(dimL,val,poss);
    }
    /**
    *controlla se due celle hanno le stesse possibilitá
     * @return se le due celle hanno le stesse possiblitá identiche
     * @param in la cella da confrontare con questa
     */
    public boolean hasSamePoss(Cell in){
        if(val==0&&in.val==0&&dimL==in.dimL){
            int i=0;
            while(i<dimL&&poss[i]==in.poss[i])
                i++;
            return i==dimL;
        }else
            return false;
    }
}

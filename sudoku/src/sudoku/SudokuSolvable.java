package sudoku;
/**
 *la classe SudokuSolvable é un'upgrade della classe sudoku
 * che rende più facile la risoluzone, invece di una matrice di int ha
 * una matrice di celle con memorizzate anche le possibilità della cella
 * per poi poter eseguire più celermente controlli su righe, colonne e quadrati
 * @author Samuele Facenda
 * @version 1.0
 * @since 2021-10-05
 */
public class SudokuSolvable {
    private Cell[][] grid;
    private final Cell[][] possSquare;
    private final int len,total,sqrtLen;
    private int filled;//il numero di celle riempite
    private final Cell[] possCol,possRow;

    /**
     * @param grid la griglia con i numeri delle celle
     */
    public SudokuSolvable(int[][] grid){
        len=grid.length;
        Cell.setLen(len);
        sqrtLen=(int)Math.sqrt(len);
        possCol=new Cell[len];
        possRow=new Cell[len];
        possSquare=new Cell[sqrtLen][sqrtLen];
        for(int e=0;e<len;e++){
            possRow[e]=new Cell();
            possCol[e]=new Cell();
        }
        for(int e=0;e<sqrtLen;e++){
            for(int i=0;i<sqrtLen;i++)
                possSquare[e][i]=new Cell();
        }
        total=len*len;
        filled=0;
        this.grid=new Cell[len][len];
        for(int i=0;i<len;i++){
            for(int e=0;e<len;e++){
                if(grid[i][e]!=0) {
                    this.grid[i][e]=new Cell(grid[i][e]);
                    possRow[i].removePoss(grid[i][e]);
                    possCol[e].removePoss(grid[i][e]);
                    possSquare[i/sqrtLen][e/sqrtLen].removePoss(grid[i][e]);
                    filled++;
                }else
                    this.grid[i][e]=new Cell();
            }
        }
    }
    /**
     * Costruttore con tutti gli attributi
     * @param grid la griglia di celle
     * @param len lunghezza di un lato
     * @param filled il numero di celle riempite
     * @param total il numero totale di celle
     * @param possRow le possibilitá di ogni riga
     * @param possCol le possibilitá di ogni colonna
     * @param possSquare le possibilitá di ogni quadrato
     */
    public SudokuSolvable(Cell[][] grid,int len,int filled,int total,Cell[] possRow,Cell[] possCol,Cell[][] possSquare) {
        Cell.setLen(len);
        this.filled = filled;
        this.total = total;
        sqrtLen=(int)Math.sqrt(len);
        this.possCol=new Cell[len];
        this.possRow=new Cell[len];
        this.possSquare=new Cell[sqrtLen][sqrtLen];
        this.len = len;for(int e=0;e<len;e++){
            this.possRow[e]=possRow[e].getCopy();
            this.possCol[e]=possCol[e].getCopy();
        }
        for(int e=0;e<sqrtLen;e++){
            for(int i=0;i<sqrtLen;i++)
                this.possSquare[e][i]=possSquare[e][i].getCopy();
        }
        this.grid = new Cell[len][len];
        for (int i = 0; i < len; i++){
            for (int e = 0; e < len; e++)
                this.grid[i][e] = grid[i][e].getCopy();
        }
    }
    /**
     * cerca di risolvere il sudoku, viene richiamato un metodo per togliere le
     * possibilità all'inizio, poi continua a richiamare diverse tecniche risolutive
     * finché non riempie tutte le caselle o fa piú di un giro a voto, qindi non riesce a risolverlo
     * o é impossibili. prova come ultima tecnica quella dei tentativi, che è meno efficiente
     * in termini di memoria e cpu, soprattutto con sudoku di dimensioni maggiore, come i 16x16,
     * ma se c'é una soluzione la trova
     * ritorna la risoluzione o meno del sudoku
     * @return se é riuscito a risolvere il sudoku o no
     */
    public boolean solveAndIsSolvable(){
        firstChoice();
        boolean worked=true;
        while(filled<total&&worked){
            worked=false;
            while(filled<total&&simpleSolve())
                worked=true;
            if(!worked&&rowInSquare())
                worked=true;
            else if(twoCellTwoPoss())
                worked=true;/*
            else if(xWing())
                worked=true;
            else if(xyWing())
                worked=true;
            else if(remoteParis())
                worked=true;
            else if(univocita())
                worked=true;
            else if(colori())
                worked=true;//*/
        }
        if(filled<total&&isCorrect())
            trySolve();
        return filled==total;
    }
    /*
    * rimuove una possibilitá da una cella, se cosí viene
    * assegnato un valore, aggiorna la griglia
    */
    private void removePossCell(int row, int col, int num){
        if(grid[row][col].removePoss(num)) {
            filled++;
            update(row, col, grid[row][col].getVal());
        }
    }
    private void assign(int row,int col,int val){
        if(canCol(val,col)&&canRow(val,row)&&canSquare(val,row,col)){
            grid[row][col].setVal(val);
            filled++;
            update(row,col,val);
        }
    }
    /*
    * aggiorna la griglia quando una cella viene risolta:
    * toglie da tutta la riga,colonna e quadrato il valore e
    * controlla se così può risolvere cella lì intorno
     */
    private void update(int row,int col,int val){
        removePossGlobal(row,col,val);
        checkAround(row,col);
    }
    /*
    * metodo che viene chiamato una sola volta, all'inizio:
    * controlla tutte le celle e se hanno un valore rimuove
    * la possibilitá dalle celle intorno
     */
    private void firstChoice(){
        for(int i=0;i<len;i++){
            for(int e=0;e<len;e++){
                if(grid[i][e].isAssigned())
                    removePossGlobal(i, e, grid[i][e].getVal());
            }
        }
    }
    /*
    * controlla se puó inserire nuovi valori nella riga,
    * colonna e quadrato di una cella
     */
    private void checkAround(int row,int col){
        chekRow(row);
        chekCol(col);
        chekSquare(row,col);
    }
    /*
    * rimuove la possibilitá di val dalla riga,colonna e quadrato
     */
    private void removePossGlobal(int row,int col,int val){
        possRow[row].removePoss(val);
        possCol[col].removePoss(val);
        possSquare[row/sqrtLen][col/sqrtLen].removePoss(val);
        for(int i=0;i<len;i++)
            removePossCell(row,i,val);
        for(int i=0;i<len;i++)
            removePossCell(i,col,val);
        int leng=sqrtLen,rowSquare=row-row%leng,colSquare=col-col%leng;
        for(int i=0;i<leng;i++){
            for(int e=0;e<leng;e++)
                removePossCell(rowSquare+i,colSquare+e,val);
        }
    }
    /*
    * metodo che controlla se puó inserire valori in una riga
    * controllando per ogni valore se puó andare in una
    * sola cella, se é cosí lo assegna
     */
    private void chekRow(int row){
        int poss,i,num=0;
        //ripete per ogni valore
        for(int e=0;e<len;e++){
            if(canCol(e,row)){
                poss=0;
                i=0;
                while(i<len&&poss<2&&grid[row][i].getVal()!=e){
                    if(grid[row][i].hasPoss(e)){
                        poss++;
                        num=i;
                    }
                    i++;
                }
                if(poss==1)
                    assign(row,num,e);
            }
        }
    }
    /*
     * metodo che controlla se puó inserire valori in una colonna
     * controllando per ogni valore se puó andare in una
     * sola cella, se é cosí lo assegna
     */
    private void chekCol(int col){
        int poss,i,num=0;
        for(int e=0;e<len;e++){
            if(canCol(e,col)){
                poss=0;
                i=0;
                while(i<len&&poss<2&&grid[i][col].getVal()!=e){
                    if(grid[i][col].hasPoss(e)){
                        poss++;
                        num=i;
                    }
                    i++;
                }
                if(poss==1)
                    assign(num,col,e);
            }
        }
    }
    /*
     * metodo che controlla se puó inserire valori in un quadrato
     * controllando per ogni valore se puó andare in una
     * sola cella, se é cosí lo assegna
     */
    private void chekSquare(int row,int col){
        int leng=sqrtLen,rowSquare=row-row%leng,colSquare=col-col%leng,poss,i,h,rowPoss=0,colPoss=0;
        for(int e=0;e<len;e++){
            if(canSquare(e,row,col)) {
                poss = 0;
                i = 0;
                h = leng;
                while (i < leng && poss < 2 && h == leng) {
                    h = 0;
                    while (h < leng && poss < 2 && grid[rowSquare + i][colSquare + h].getVal() != e) {
                        if (grid[rowSquare + i][colSquare + h].hasPoss(e)) {
                            poss++;
                            rowPoss = i + rowSquare;
                            colPoss = h + colSquare;
                        }
                        h++;
                    }
                    i++;
                }
                if (poss == 1)
                    assign(rowPoss, colPoss, e);
            }
        }
    }
    /*
    * tecnica risolutiva semplice:
    * controlla tutte le righe,colonne e quadrati,
    * ritorna true se ha fatto qualcosa
     */
    private boolean simpleSolve(){
        int fil=filled;
        for(int i=0;i<len;i++)
            chekCol(i);
        for(int i=0;i<len;i++)
            chekCol(i);
        int leng=sqrtLen;
        for(int squareRow=0;squareRow<len;squareRow+=leng){
            for(int squareCol=0;squareCol<len;squareCol+=leng){
                chekSquare(squareRow,squareCol);
            }
        }
        return fil!=filled;
    }
    /*
    * tecnica risolutiva avanzata per la rimosione di possibilitá:
    * controlla in ogni quadratato se un valore puó essere solo in una
    * riga o colonna all'interno del quadrato, se é cosí rimuove quella ]
    * possibilitá dalla riga o colonna
     */
    private boolean rowInSquare(){
        int rowPoss,colPoss,po;
        boolean worked=false;
        //controlla in ogni quadrato
        for(int squareRow=0;squareRow<len;squareRow+=sqrtLen){
            for(int squareCol=0;squareCol<len;squareCol+=sqrtLen){
                //controlla per ogni valore
                for(int num=0;num<len;num++){
                    //lo fa solo se il numero non é giá presente nel quadrato
                    if(canSquare(num,squareRow,squareCol)){
                        //-1 é non assegnato, -2 é non applicabile
                        rowPoss=-1;
                        colPoss=-1;
                        //controlla per ogni cella del quadrato
                        for(int row=squareRow;row<squareRow+sqrtLen;row++){
                            for(int col=squareCol;col<squareCol+sqrtLen;col++){
                                if(grid[row][col].hasPoss(num)){
                                    //controlla se é applicabile, se lo é controlla se ha giá valore diverso che non sia il non assegnato e in caso lo assegna
                                    if(rowPoss!=-2){
                                        if(rowPoss!=row&&rowPoss!=-1)
                                            rowPoss=-2;
                                        else
                                            rowPoss=row;
                                    }
                                    if(colPoss!=-2){
                                        if(colPoss!=col&&colPoss!=-1)
                                            colPoss=-2;
                                        else
                                            colPoss=col;
                                    }
                                }
                            }
                        }
                        //controlla se il metodo é applicabile, poi rimuove la possibilitá dalla riga o colonna
                        if(colPoss>=0){
                            for(int i=0;i<len;i++){
                                //controlla che la cella non sia una del quadrato o giá asseganta
                                if((i<squareRow||i>=squareRow+sqrtLen)&&!grid[i][colPoss].isAssigned()) {
                                    po=grid[i][colPoss].getNumPoss();
                                    removePossCell(i, colPoss, num);
                                    if(po!=grid[i][colPoss].getNumPoss())worked=true;
                                }
                            }
                        }
                        if(rowPoss>=0){
                            for(int i=0;i<len;i++){
                                if((i<squareCol||i>=squareCol+sqrtLen)&&!grid[rowPoss][i].isAssigned()) { po=grid[rowPoss][i].getNumPoss();
                                    po=grid[rowPoss][i].getNumPoss();
                                    removePossCell(rowPoss, i, num);
                                    if(po!=grid[rowPoss][i].getNumPoss())worked=true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return worked;
    }
    /*
    * metodo per la risoluzione per tentativi:
    * cerca la cella con meno possibilitá, dopodiché per ogni possibilitá
    * crea un nuovo sudoku con la cella assegnata e prova a risolverlo,
    * finché non trova una soluzione, quindi lo ricopia in questo sudoku
     */
    private void trySolve(){
        int minPoss=len,minPossCol=0,minPossRow=0,row=0,col;
        Cell cel;
        //cerca la cella con meno possibilitá
        while(row<len&&minPoss!=2){
            col=0;
            while(col<len&&minPoss!=2){
                cel=grid[row][col];
                if(!cel.isAssigned()&&cel.getNumPoss()<minPoss){
                    minPoss=cel.getNumPoss();
                    minPossRow=row;
                    minPossCol=col;
                }
                col++;
            }
            row++;
        }
        int i=0;
        int[] poss=grid[minPossRow][minPossCol].getPoss();
        SudokuSolvable prova;
        //prova con ogni valore
        while(i<minPoss&&filled<=total){
            prova=new SudokuSolvable(grid,len,filled,total,possRow,possCol,possSquare);
            prova.assign(minPossRow,minPossCol,poss[i]);
            if(prova.solveAndIsSolvable() && prova.isCorrect()){
                grid=prova.grid;
                filled=prova.filled;
            }
            i++;
        }
    }
    /*
    * tecnica avanzata per la rimosione di possibilitá:
    * controlla se all'interno di un quadrato ci sono due celle con solo due possibilitá,
    * uguali. se cosí fosse rimuoverebbe qui due numeri dalle possibilitá di tutto il quadrato
     */
    private boolean twoCellTwoPoss(){
        int row2,col2,numPoss;
        boolean worked=false;
        //per ogni quadrato
        for(int squareRow=0;squareRow<len;squareRow+=sqrtLen){
            for(int squareCol=0;squareCol<len;squareCol+=sqrtLen){
                //per ogni cella del quadrato
                for(int row=squareRow;row<squareRow+sqrtLen;row++){
                    for(int col=squareCol;col<squareCol+sqrtLen;col++){
                        //se ha solo due possibilitá
                        if(grid[row][col].getNumPoss()==2){
                            //cerca un'altra cella con le stesse possibilitá
                            row2=row;
                            do{
                                col2=squareCol;
                                while(col2<squareCol+sqrtLen && !(grid[row2][col2].hasSamePoss(grid[row][col])&&(col2!=col||row2!=row)))
                                    col2++;
                                row2++;
                            }while(row2<squareRow+sqrtLen && col2==squareCol+sqrtLen);
                            row2--;
                            //se l'ha trovata
                            if(col2!=squareCol+sqrtLen){
                                int[] poss=grid[row][col].getPoss();
                                //rimuove le possibilitá dal quadrato
                                for(int i=squareRow;i<squareRow+sqrtLen;i++){
                                    for(int e=squareCol;e<squareCol+sqrtLen;e++){
                                        //se non é una delle due celle
                                        if((i!=row2||e!=col2)&&(i!=row||e!=col)&&!grid[i][e].isAssigned()){
                                            numPoss=grid[i][e].getNumPoss();
                                            removePossCell(i,e,poss[0]);
                                            removePossCell(i,e,poss[1]);
                                            if(numPoss!=grid[i][e].getNumPoss()) worked=true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return worked;
    }
    private boolean xWing(){return false;}
    private boolean xyWing(){return false;}
    private boolean remoteParis(){return false;}
    private boolean univocita(){return false;}
    private boolean colori(){return false;}
    @Override
    public String toString(){
        String out="";
        for(int i=0;i<len;i++){
            for(int e=0;e<len;e++){
                out+=grid[i][e].getVal()+" ";
            }
            out+='\n';
        }
        return out;
    }
    /*
    * ritorna true se quel numero puó andare in quella colonna
     */
    private boolean canRow(int num,int row){
        return possRow[row].hasPoss(num);
    }
    /*
     * ritorna true se quel numero puó andare in quella riga
     */
    private boolean canCol(int num,int col){
        return possCol[col].hasPoss(num);
    }
    /*
     * ritorna true se quel numero puó andare in quel quadrato
     */
    private boolean canSquare(int num,int row,int col){
        return possSquare[row/sqrtLen][col/sqrtLen].hasPoss(num);
    }
    private int indexOfRow(int col, int val){
        int i=0;
        while(i<len&&grid[i][col].getVal()!=val)
            i++;
        return i;
    }
    private int indexOfCol(int row,int val){
        int i=0;
        while(i<len&&grid[row][i].getVal()!=val)
            i++;
        return i;
    }
    private int[] indexOfSquare(int row,int col,int val){
        int i=0,e=sqrtLen,rowSquare=row-row%sqrtLen,colSquare=col-col%sqrtLen;
        while(i<sqrtLen&&e==sqrtLen){
            e=0;
            while(e<sqrtLen&&grid[i+rowSquare][e+colSquare].getVal()!=val)
                e++;
            i++;
        }
        return new int[]{i+rowSquare-1,e+colSquare};
    }
    private boolean checkIfTrue(int row,int col, int val){
        int[] squareIndex=indexOfSquare(row,col,val);
        return indexOfCol(row,val)==col && indexOfRow(col,val)==row && squareIndex[0]==row && squareIndex[1]==col;
    }
    /*
    * controlla ogni cella, se é assegnata controlla se quel numero puó stare
    * lí ho é giá presente sulla riga o colonna
    * se é cosí ritorna falso
     */
    public boolean isCorrect(){
        boolean out=true;
        int num,row=0,col=0;
        while(row<len&&out){
            col=0;
            while(col<len&&out){
                num=grid[row][col].getVal();
                if (num != 0) out=checkIfTrue(row,col,num);
                col++;
            }
            row++;
        }
        return out;
    }
}

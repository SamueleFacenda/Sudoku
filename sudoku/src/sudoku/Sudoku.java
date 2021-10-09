package sudoku;

public class Sudoku {
    private int[][] grid;
    private int len,filled,total;
    public Sudoku(int[][] grid){
        len=grid.length;
        total=len*len;
        filled=0;
        this.grid=new int[len][len];
        for(int i=0;i<len;i++){
            for(int e=0;e<len;e++){
                this.grid[i][e]=grid[i][e];
                if(grid[i][e]!=0)
                    filled++;
            }
        }
    }
    public void setGrid(int[][] grid){
        len=grid.length;
        this.grid=grid;
    }
    public int[][] getGrid(){
        return grid;
    }
    public boolean solveAndIsPossible(){
        int i;
        do{
            i=0;
            while(filled<total && simpleSolve())
                i++;
        }while(filled<total && i!=0);
        System.out.println(filled);
        if(filled<total)
            trySolve();
        return filled==total;
    }
    private boolean simpleSolve(){
        boolean out=false;
        int poss,num=0,i;
        for(int row=0;row<len;row++){
            for(int col=0;col<len;col++){
                if (grid[row][col] == 0) {
                    poss = 0;
                    i=1;
                    while(i<=len && poss<2){
                        if (canRow(i, row) && canCol(i, col) && canSquare(i, row, col)) {
                            num = i;
                            poss++;
                        }
                        i++;
                    }
                    if(poss==1){
                        grid[row][col]=num;
                        filled++;
                        out=true;
                    }
                }
            }
        }
        return out;
    }
    private boolean canRow(int num,int row){
        int i=0;
        while(i<len&&grid[row][i]!=num)
            i++;
        return i==len;
    }
    private boolean canCol(int num,int col){
        int i=0;
        while(i<len&&grid[i][col]!=num)
            i++;
        return i==len;
    }
    private boolean canSquare(int num,int row,int col){
        int leng=(int)Math.sqrt(len),rowSquare=row-row%leng,colSquare=col-col%leng,co,ro=0;
        do{
            co=0;
            while(co<leng&&grid[rowSquare+ro][colSquare+co]!=num)
                co++;
            ro++;
        }while(ro<leng&&co==leng);
        return co==leng;
    }
    private void trySolve(){
        int poss,minCol=0,minRow=0,minPoss=len+1,row=0,col;
        boolean[] numPoss=new boolean[len];
        while(row<len&&minPoss!=2){
            col=0;
            while(col<len&&minPoss!=2){
                if(grid[row][col]==0) {
                    poss = 0;
                    for (int i = 0; i < len; i++)
                        numPoss[i] = false;
                    for (int i = 1; i <= len; i++) {
                        if (canRow(i, row) && canCol(i, col) && canSquare(i, row, col)) {
                            poss++;
                            numPoss[i-1] = true;
                        }
                    }
                    if (poss != 0 && poss < minPoss) {
                        minPoss = poss;
                        minCol = col;
                        minRow = row;
                    }
                }
                col++;
            }
            row++;
        }
        int i=0;
        Sudoku prova;
        while(i<len&&filled!=total){
            if(numPoss[i]){
                grid[minRow][minCol]=i+1;
                prova=new Sudoku(grid);
                if(prova.solveAndIsPossible()){
                    grid=prova.grid;
                    filled=prova.filled;
                }
            }
            i++;
        }
    }
    @Override
    public String toString(){
        String out="";
        for(int i=0;i<len;i++){
            for(int e=0;e<len;e++){
                out+=grid[i][e]+" ";
            }
            out+='\n';
        }
        return out;
    }
    public static int[][] stringToSudoku(String in){
        String[] divided=in.split(" ");
        int le=divided.length;
        int[][] out=new int[le][le];
        for(int i=0;i<le;i++){
            for(int e=0;e<le;e++){
                out[i][e]=divided[i].charAt(e)-48;
            }
        }
        return out;
    }
    public boolean isCorrect(){
        boolean out=true;
        int num,row=0,col;
        while(row<len&&out){
            col=0;
            while(col<len&&out){
                num=grid[row][col];
                if (num != 0) {
                    grid[row][col]=0;
                    if (!canRow(num, row) || !canCol(num, col) || !canSquare(num, row, col))
                        out=false;
                    grid[row][col]=num;
                }
                col++;
            }
            row++;
        }
        return out;
    }
}

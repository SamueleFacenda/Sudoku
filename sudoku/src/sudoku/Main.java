package sudoku;

public class Main {
    public static void main(String[] args){
        Time ti=new Time();
        ti.start();
        //Sudoku su=new Sudoku(Sudoku.stringToSudoku("900500700 020000940 000008016 800905100 000070000 009603007 590300000 086000020 007006004"));
        //System.out.println(su2);
        SudokuSolvable sud=new SudokuSolvable(new int[][]{
                {13,0,0,3,0,0,0,5,0,0,0,0,11,16,0,14},
                {0,9,0,16,8,0,1,0,5,14,7,0,15,2,12,0},
                {0,0,1,0,0,6,14,11,0,10,0,0,0,0,9,7},
                {7,5,0,0,0,0,0,12,2,4,0,0,0,0,6,13},
                {0,8,0,0,0,0,0,0,0,1,13,0,0,0,0,0},
                {0,0,6,0,0,0,0,0,12,7,8,4,0,0,1,0},
                {0,11,4,0,0,0,0,13,9,5,2,10,12,8,14,0},
                {1,0,2,5,0,0,8,0,0,6,16,0,7,0,13,0},
                {0,12,0,9,0,14,11,0,0,13,0,0,2,3,0,5},
                {0,7,14,2,1,10,15,6,3,0,0,0,0,11,8,0},
                {0,1,0,0,13,16,9,2,0,0,0,0,0,10,0,0},
                {0,0,0,0,0,5,12,0,0,0,0,0,0,0,16,0},
                {2,16,0,0,0,0,10,7,13,0,0,0,0,0,5,11},
                {12,14,0,0,0,0,5,0,7,11,6,0,0,13,0,0},
                {0,3,7,10,0,9,13,14,0,2,0,1,4,0,15,0},
                {15,0,5,6,0,0,0,0,10,0,0,0,16,0,0,8}
        });
        //SudokuSolvable su2=new SudokuSolvable(su.getGrid());
        /*if(su.isCorrect()) {
            if (!su.solveAndIsPossible())
                System.out.printf("sudoku impossibile\n");
            else
                System.out.println(su.isCorrect()?"sudoku risolto e corretto":"sudoku risolto ma sbagliato");
            System.out.println(su);
            System.out.println(ti.get());
        }else
            System.out.println("sudoku incorretto");
        System.out.println("\n\n\n\n");*/
        if(!sud.solveAndIsSolvable())
            System.out.println("non funziono");
        if(!sud.isCorrect())
            System.out.println("sono sbagliato");
        System.out.println(sud);
        System.out.println(ti.get());
    }
}

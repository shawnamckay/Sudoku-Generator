


import java.util.Random;

/**
 * Program first creates a valid puzzle using a very simple algorithm:
 *  1. Generate one random row of a sudoku puzzle with the numbers 1-9
 *  2. Repeat the row's order using addition to transform the position of the numbers in the column
 *      (ex: number in (0,0) reappears in (1,3), (2, 6), (3, 7), (4,1), (5,4), (6,5), (7,8), (8,2) )
 *      Please see getIndex method for details on the addition algorithm
 *  3. Then transforms the puzzle to reduce some of the pattern repetition in the board
 *      Sudoku transformation algorithm from: https://dryicons.com/blog/2009/08/14/a-simple-algorithm-for-generating-sudoku-puzzles
 *          Swap cells (rows) horizontally: rotating the grid around the fourth row, the first row swaps with the last,
 *             the second swaps with the eighth and the third swaps with the fifth, no change to the fourth row
 *          Swap cells (columns) vertically: rotating the grid around the fourth column, the first column swaps with the last,
 *              the second swaps with the eighth and the third swaps with the fifth, no change to the fourth column
 *          Swap cells around the main diagonal: (left top corner to right bottom corner)
 *
 * Then the program removes 55 squares at random(using utils.Random),then uses the backTracking class to check
 * that the board is still solvable every time it removes a square.
 *
 * The program then tests that there is only one solution to the game (naive solution).
 */

public class Main {
    private int[][] gameBoard= new int[9][9];

    public static void main(String[] args) {
        Main m = new Main();

        while(!m.runTests()){
             m.runTests();

        }
        m.printBoard(m.gameBoard);

    }


    /**
     * Simple way to test if only one solution to the sudoku board
     * Tests if the solution found is the same to the filled in board
     * Then tests if the solution finding algorithm applied in reverse order also has the same solution
     * @return true - if both solutions = starting board
     */
    public boolean runTests(){
        int[][] endGame=generateGameBoard();
        gameBoard = copyArray(endGame);
        gameBoard=removeRandomCell(gameBoard, 54);
        BackTracking b = new BackTracking();
        int[][] testRun = copyArray(gameBoard);
        b.testIfSolvable(testRun);
        if(compareBoards(endGame, testRun)){
            b.testBackwardsSolvable(testRun);
            if(compareBoards(endGame, testRun)){
                return true;
            }
        }
        return false;
    }



    /**
     * runs the routine to create the solution game board
     * @return filled in game board
     */
    public int[][] generateGameBoard(){
        int[][] endGame =generateBasicEndGame();
        //printBoard(endGame);
        endGame=transformRows(endGame);
        //printBoard(endGame);
        endGame= transformCols(endGame);
        //printBoard(endGame);
        endGame=transformMainDiagonal(endGame);
        //printBoard(endGame);
        return endGame;
    }


    /**
     * Iterates through rows and columns to output board to console
     * @param endGame the current gameboard
     */
    public void printBoard(int[][] endGame){
        for(int row=0; row<9; row++){
            System.out.println("-------------------------------------");
            for(int col=0; col<9; col++){
                if(endGame[row][col]==0){
                    System.out.print("|   ");
                }
                else{
                    System.out.print("| "+ endGame[row][col] +" ");
                }
            }
            System.out.println("|");
        }
        System.out.println("-------------------------------------");
        System.out.println();
    }

    /**
     * Creates the basic array for the first iteration of the endGame board
     * @return endGame - the basic 9 x 9 array valid sudoku puzzle
     */
    public int[][] generateBasicEndGame(){
        int[][] endGame = new int[9][9];
        endGame=generateRandomRow(endGame);
        endGame=scrambleRow(endGame);
        return endGame;
    }


    /**
     * Generates a random-ish number between 1-9
     * @return num - random number between 1-9
     */
    public int generateRandomNum(){
        Random rand = new Random();
        int num = 1 + rand.nextInt(9);
        return num;
    }

    /**
     * Checks that a number is not repeated within the row
     * @param num the number to test
     * @param endGame the array for the sudoku board
     * @return boolean - true if the number has not been repeated
     */
    public boolean checkNotRepeated( int num, int[][] endGame){
        for(int i=0;i<9; i++){
            if(endGame[0][i]==num)
                return false;
        }
        return true;
    }


    /**
     * Generates the first row of the board using random numbers between 1-9
     * @param endGame blank 9 by 9 array
     * @return endGame array with the first row filled in
     */
    public int[][] generateRandomRow(int[][] endGame){
        for(int col=0; col<9; col++) {
                int num = generateRandomNum();
                while (!checkNotRepeated(num, endGame)) {
                    num = generateRandomNum();
                }
                endGame[0][col] = num;
            }
        return endGame;
    }

    /**
     * Iterates through the 9 by 9 grid to fill in values using the first row of the board
     * @param endGame the current 9 by 9 grid with the first row filled in
     * @return endGame the filled in 9 by 9 array with a valid sudoku board
     */
    public int[][] scrambleRow(int[][] endGame){
        for(int i =0; i<9; i++){
          int index=i;
          int num=endGame[0][i];

          for(int j=1; j<9; j++){
             index=getIndex(j, index);
             endGame[j][index]=num;
          }
        }
        return endGame;
    }


    /**
     * Addition algorithm to ensure the sudoku board is valid (pattern= +3, +3, +1)
     * @param j the column of the Array
     * @param index the row value of the Array
     * @return index- the updated row value of the Array
     */
    public int getIndex(int j, int index){
        if(j%3==0){
            index=index+1;
        }
        else{
            index=index+3;
        }
        index=index%9;
        return index;
    }


    /**
     * Transforms the rows of the game board:
     *  Swap cells (rows) horizontally: rotating the grid around the fourth row, the first row swaps with the last,
     *  the second swaps with the eighth and the third swaps with the fifth, no change to the fourth row
     * @param endGame the current game board to be transformed
     * @return endGame the game board after the series of transformations
     */
    public int[][] transformRows(int[][] endGame){
        int[] tempRow= new int[9];
        int rowToSwap=8;

        for(int row=0; row<4; row++){
            tempRow=copyRow(endGame, row);
            endGame=swapRow(endGame,rowToSwap,row);
            endGame=replaceWithTempRow(endGame,rowToSwap,tempRow);
            rowToSwap--;
        }
        return endGame;
    }


    /**
     * copies a row from the game board and saves it in tempRow
     * @param endGame the current game board
     * @param row the row to be copied
     * @return tempRow - single row of the gameboard copied
     */
    public int[] copyRow(int[][] endGame, int row){
        int[] tempRow= new int[9];
        for(int i=0; i<9; i++){
            tempRow[i]=endGame[row][i];
        }
        return tempRow;
    }


    /**
     * Replaces row in the game board with another row
     * @param endGame the current gameboard
     * @param rowToSwap the row that will be copied
     * @param originalRow the row that will be replaced
     * @return endGame - the update gameboard with the row copied
     */
    public int[][] swapRow(int[][] endGame, int rowToSwap, int originalRow){
        for(int i=0; i<9; i++){
            endGame[originalRow][i]=endGame[rowToSwap][i];
        }
        return endGame;
    }

    /**
     * Updates the gameboard using the row that had been copied
     * @param endGame gameboard to be updated
     * @param rowToSwap row that is updated
     * @param tempRow row that had been copied
     * @return endGame updated board
     */
    public int[][] replaceWithTempRow(int[][] endGame, int rowToSwap, int[] tempRow){
        for(int i=0; i<9; i++){
            endGame[rowToSwap][i]=tempRow[i];
        }
        return endGame;
    }


    /**
     * Transforms the columns of the game board:
     * Swap cells (columns) vertically: rotating the grid around the fourth column,
     *   the first column swaps with the last, the second swaps with the eighth and the
     *   third swaps with the fifth, no change to the fourth column
     * @param endGame the current game board to be transformed
     * @return endGame the game board after the series of transformations
     */
    public int[][] transformCols(int[][] endGame){
        int[] tempCol= new int[9];
        int colToSwap=8;

        for(int col=0; col<4; col++){
            tempCol=copyCol(endGame, col);
            endGame=swapCol(endGame,colToSwap,col);
            endGame=replaceWithTempCol(endGame,colToSwap,tempCol);
            colToSwap--;
        }
        return endGame;
    }

    /**
     * copies a column from the game board and saves it in tempCol
     * @param endGame the current game board
     * @param col the column to be copied
     * @return tempCol - single column of the gameboard copied
     */
    public int[] copyCol(int[][] endGame, int col){
        int[] tempCol= new int[9];
        for(int i=0; i<9; i++){
            tempCol[i]=endGame[i][col];
        }
        return tempCol;
    }



    /**
     * Replaces column in the game board with another row
     * @param endGame the current gameboard
     * @param colToSwap the column that will be copied
     * @param originalCol the column that will be replaced
     * @return endGame - the updated gameboard with the column copied
     */
    public int[][] swapCol(int[][] endGame, int colToSwap, int originalCol){
        for(int i=0; i<9; i++){
            endGame[i][originalCol]=endGame[i][colToSwap];
        }
        return endGame;
    }


    /**
     * Updates the gameboard using the column that had been copied
     * @param endGame gameboard to be updated
     * @param colToSwap column that is updated
     * @param tempCol column that had been copied
     * @return endGame updated board
     */
    public int[][] replaceWithTempCol(int[][] endGame, int colToSwap, int[] tempCol){
        for(int i=0; i<9; i++){
            endGame[i][colToSwap]=tempCol[i];
        }
        return endGame;
    }


    /**
     * Transforms the game board along the main diagonal (top left corner to bottom right corner)
     * @param endGame the current game board
     * @return the updated game board
     */
    public int[][] transformMainDiagonal(int[][] endGame){
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                if(row<=col){ //prevents transformation from doing the same cells twice
                    int tempValue= endGame[row][col];
                    endGame[row][col]=endGame[col][row];
                    endGame[col][row]=tempValue;
                }
            }
        }

        return endGame;
    }

    /**
     * Transforms the game board along the minor diagonal (top right corner to bottom left corner)
     * @param endGame the current game board
     * @return the updated game board
     */
    public int[][] transformMinorDiagonal(int[][] endGame){
        int maxCol=8;
        for(int row=0; row<9; row++){
            int adder=8-row;
            for(int col=0; col<9; col++){
                if(col<=maxCol){ //prevents transformation from doing the same cells twice
                    int tempValue= endGame[row][col];
                    endGame[row][col]=endGame[row+adder][col+adder];
                    endGame[row+adder][col+adder]=tempValue;
                    adder--;
                }
            }
            maxCol--;
        }
        return endGame;
    }


    /**
     * Removes cells one at a time recursively then checks that the game is still solvable
     * @param gameBoard board that needs blank spaces added
     * @param counter the amount of spaces that still need to be removed
     * @return gameBoard - board with all the spaces removed
     */
    public int[][] removeRandomCell(int[][] gameBoard, int counter){
        BackTracking b = new BackTracking();
        Random rand= new Random();
        if(counter==0){
            return gameBoard;
        }
        int row, col;
        do{
            row = rand.nextInt(9);
            col = rand.nextInt(9);
        }
        while(gameBoard[row][col]==0);
        int tempValue= gameBoard[row][col];
        gameBoard[row][col]=0;
        if(b.testIfSolvable(copyArray(gameBoard))){
            counter--;
            gameBoard=removeRandomCell(gameBoard, counter);
        }
        else{ //Game has become unsolvable, reset and try again.
            gameBoard[row][col]= tempValue;
            gameBoard=removeRandomCell(gameBoard, counter);
        }
        return gameBoard;
    }


    /**
     * Used to prevent memory leak of board being changed
     * @param array input gameboard
     * @return copy of the array at a different memory location
     */
    public int[][] copyArray(int[][] array){
        int[][] copy = new int[9][9];
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                copy[i][j]=array[i][j];
            }
        }
        return copy;
    }


    /**
     * Compares two 9 by 9 arrays to confirm all values are the same
     * @param endGame original solution to the game
     * @param testSolution solution found by the backtracking algorithm
     * @return true- if all values are the same
     */
    public boolean compareBoards(int[][] endGame, int[][] testSolution){
        for(int i=0;i<9; i++){
            for(int j=0;j<9;j++){
                if(endGame[i][j]!=testSolution[i][j]){
                    return false;
                }

            }
        }
        return true;
    }




}

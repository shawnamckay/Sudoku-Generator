

/**
 * Entirely based on: https://www.geeksforgeeks.org/backtracking-set-7-suduku
 * Class that takes a sudoku board then uses a backtracking method to find the board's solution
 */
public class BackTracking {
    private int row;
    private int col;


    /**
     * Solves the sudoku puzzle recursively filling with larger values first
     * @param gameBoard the current board being solved
     * @return true if game is solvable
     */
    public boolean testBackwardsSolvable(int[][] gameBoard){
        if(!findOpenValue(gameBoard)) {
            return true;
        }
        for(int i=9; i<=1; i--){
            if(validNumber(gameBoard, i)){

                gameBoard[row][col]=i;
                if(testIfSolvable(gameBoard)){
                    return true;
                }
                gameBoard[row][col]=0;
            }
        }
        return false;
    }




    /**
     * Test if there are any squares remaining on the gameboard
     * @param gameBoard the current gameBoard being solved
     * @return true if there still is a value=0 on board
     */
    public boolean findOpenValue(int[][] gameBoard){
        for(int row=0; row<9; row++) {
            for (int col = 0; col < 9; col++) {
                if (gameBoard[row][col] == 0) {
                    setRow(row);
                    setCol(col);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Solves the sudoku puzzle recursively filling with smaller values first
     * @param gameBoard the current board being solved
     * @return true if the game is solvable
     */
    public boolean testIfSolvable(int[][] gameBoard){
       // Main m = new Main();
       // m.printBoard(gameBoard);
        if(!findOpenValue(gameBoard)) {
            return true;
        }
        for(int i=1; i<=9; i++){
            if(validNumber(gameBoard, i)){

                gameBoard[row][col]=i;
                if(testIfSolvable(gameBoard)){
                    return true;
                }
                gameBoard[row][col]=0;
            }
        }
        return false;
    }

    /**
     * Checks that the proposed number can be placed in this row
     * @param gameBoard the current solution created on the game
     * @param num the current proposed number
     * @return true if this number is not repeated in this row
     */
    public boolean checkRow(int[][] gameBoard, int num){
        for(int col=0;col<9;col++){
            if(gameBoard[row][col]==num)
                return false;
        }
        return true;
    }


    /**
     * Checks that the proposed number can be placed in this column
     * @param gameBoard the current solution created on the game
     * @param num the current proposed number
     * @return true if this number is not repeated in this column
     */
    public boolean checkCol(int[][] gameBoard, int num){
        for(int row=0;row<9;row++){
            if(gameBoard[row][col]==num)
                return false;
        }
        return true;
    }

    /**
     * Checks that the proposed number can be placed in the 3 by 3 square
     * @param gameBoard the current solution created on the game
     * @param num the current proposed number
     * @return true if the number is not repeated in 3 by 3 square
     */
    public boolean checkBox(int[][] gameBoard, int num){
        int baseRow= (row/3) * 3;
        int baseCol= (col/3) * 3;

        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(gameBoard[baseRow+i][baseCol+j]==num){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Runs the test to check if the number can be placed in the proposed position
     * @param gameBoard the current solution created on the game
     * @param num the current proposed number
     * @return true if all tests pass
     */
    public boolean validNumber(int[][] gameBoard, int num){

        if(checkRow(gameBoard,num) && checkCol(gameBoard, num) && checkBox(gameBoard, num))
            return true;
        return false;
    }


    /**
     * Setter for row variable
     * @param row x value
     */
    public void setRow(int row) {
        this.row = row;
    }


    /**
     * Setter for col variable
     * @param col y value
     */
    public void setCol(int col) {
        this.col = col;
    }



}

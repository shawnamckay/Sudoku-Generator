# Sudoku-Generator
Creates a sudoku board to be solved. 
Program first creates a valid puzzle using a very simple algorithm:
  1. Generate one random row of a sudoku puzzle with the numbers 1-9
  2. Repeat the row's order using addition to transform the position of the numbers in the column
    (ex: number in (0,0) reappears in (1,3), (2, 6), (3, 7), (4,1), (5,4), (6,5), (7,8), (8,2) )
    Please see getIndex method for details on the addition algorithm
  3. Then transforms the puzzle to reduce some of the pattern repetition in the board
      Sudoku transformation algorithm from: https://dryicons.com/blog/2009/08/14/a-simple-algorithm-for-generating-sudoku-puzzles
      Swap cells (rows) horizontally: rotating the grid around the fourth row, the first row swaps with the last,
        the second swaps with the eighth and the third swaps with the fifth, no change to the fourth row
      Swap cells (columns) vertically: rotating the grid around the fourth column, the first column swaps with the last,
        the second swaps with the eighth and the third swaps with the fifth, no change to the fourth column
      Swap cells around the main diagonal: (left top corner to right bottom corner)
      
Then the program removes 55 squares at random(using utils.Random),then uses the backTracking class to check
  that the board is still solvable every time it removes a square.

The program then tests that there is likely only one solution to the game.

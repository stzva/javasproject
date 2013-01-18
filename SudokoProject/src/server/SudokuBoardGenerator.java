package server;

import java.util.Random;

public class SudokuBoardGenerator {

    private int board[][]; //table 9x9
    public int[] permut_array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private Random r = new Random();

    public SudokuBoardGenerator() {
        board = new int[9][9];
    }

    public SudokuBoardGenerator(SudokuBoardGenerator s) {
        board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = s.board[i][j];
            }
        }
    }

    public int[][] getBoard() {

        int result[][] = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                result[i][j] = board[i][j];
            }
        }
        return result;
    }

    public int getField(int x, int y) {
        return board[y][x];
    }

    public void put(int x, int y, int number) {
        board[y][x] = number;
    }

    //checks if number can be placed on a field(x,y)
    public boolean valid(int x, int y, int number) {

        for (int i = 0; i < 9; i++) {
            //in row y
            if (board[y][i] == number) {
                return false;
            }
            //in column x
            if (board[i][x] == number) {
                return false;
            }
        }
        // int the square
        int qx = (x / 3) * 3;
        int qy = (y / 3) * 3;
        for (int i = qx; i < qx + 3; i++) {
            for (int j = qy; j < qy + 3; j++) {
                if (board[j][i] == number) {
                    return false;
                }
            }
        }

        return true;
    }

    //Recursive method that goes through each field of the table row bye row.
    //For every field it generates a permutation array of the array {1,2,3,4,5,6,7,8,9}
    //and tries successively to fill in the field with a number of the array.
    //until there is a valid number. This number is set to the field and we move on to
    //the next field calling the same method. If the method returns true and the board was
    //filled in successfully we stop. If the method returns false on the current step we try
    //with the next number of the permutation array. If no number is valid, we can not fill
    //in the board.
    boolean fillBoard(SudokuBoardGenerator board, int x, int y) {
        //check if reached the end of the board
        if (x == 0 && y == 9) {
            return true;
        }
        //jumps over already filled in fields
        while (!board.empty(x, y)) {
            if (x == 8) {
                y++;
                x = 0;
            } else {
                x++;
            }
            if (x == 0 && y == 9) {
                return true;
            }
        }
        permute();
        int[] copy = new int[9];
        System.arraycopy(permut_array, 0, copy, 0, permut_array.length);
        //tries to find a valid number for the current field
        for (int i = 0; i < 9; i++) {
            if (valid(x, y, copy[i])) {
                board.put(x, y, copy[i]);
                if (fillBoard(board, incrX(x), incrY(x, y))) {
                    return true;
                }
            }
        }
        board.put(x, y, 0);
        return false;
    }

    //updates the x coordinate when moving to the next field
    private int incrX(int x) {
        if (x == 8) {
            return 0;
        }
        return x + 1;
    }

    //updates the y coordinate when moving to the next field
    private int incrY(int x, int y) {
        if (x == 8) {
            y++;
        }
        return y;
    }

    //empties the board
    private boolean empty(int x, int y) {
        if (board[y][x] == 0) {
            return true;
        }
        return false;
    }

    //81 - n fields empty
    private void leave(int n) {
        for (int j = 0; j < 81 - n; j++) {
            int x = r.nextInt(9);
            int y = r.nextInt(9);

            if (board[x][y] == 0) {
                j--;
                continue;
            }
            board[x][y] = 0;

        }
    }

    //permutes the array
    public void permute() {

        for (int i = 1; i < 2 + r.nextInt(7); i++) {
            int i1 = r.nextInt(9);
            int i2 = r.nextInt(9);
            if (i1 == i2) {
                continue;
            }
            //swaps i1 and i2;
            permut_array[i1] = permut_array[i1] ^ permut_array[i2];
            permut_array[i2] = permut_array[i1] ^ permut_array[i2];
            permut_array[i1] = permut_array[i1] ^ permut_array[i2];
        }

    }


    private void clear() {
        board = new int[9][9];
    }


    //generates a new sudoku (tries 100 times)
    public void generate(int filled) {
        clear();
        int i = 0;
        while (i < 100) {
            if (fillBoard(this, 0, 0)) {
                break;
            }
            clear();
            i++;
        }
        leave(filled);
    }
}

package server;

/*
 * class that holds a sudoku boarda and validates moves 
 */
public class SudokuBoard {

    private int board[][];

    public SudokuBoard() {
        board = new int[9][9];
    }

    public void setBoard(int[][] board) {
        this.board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    public boolean valid(int x, int y, int number) {

        for (int i = 0; i < 9; i++) {
            if (board[y][i] == number) {
                return false;
            }
            if (board[i][x] == number) {
                return false;
            }
        }
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

    public int getField(int x, int y) {
        return board[y][x];
    }

    public void put(int x, int y, int number) {
        board[y][x] = number;
    }

    public int populatedFields() {
        int c = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    c++;
                }
            }
        }
        return c;
    }
}

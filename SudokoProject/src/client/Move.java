
package client;

/*
 * keeps what number is put on the field x,y
 */

public class Move {
    
    private int x,  y, number;
    
    public Move(int x, int y, int number) {
        setX(x);
        setY(y);
        setNumber(number);
    }

    public Move(Move move) {
        this(move.getX(), move.getY(), move.getNumber());
    } 
    
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    
}

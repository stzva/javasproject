
package server;

import java.io.Serializable;

public class GameResult implements Serializable {

    private boolean win; //outcome - won or lost
    private int timePlayed; 
    private int difficulty; // 1 - easy, 2 - medium, 3 - hard
    
    public GameResult(int difficulty) {
        win = false;
        timePlayed = 0;
        this.difficulty = difficulty;
    }
    
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}

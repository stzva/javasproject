
package server;

import java.io.Serializable;
import java.util.Vector;

/*
 * keeps statistics (game results for all games) for a certain player
 */

public class PlayerStatistics implements Serializable{
    
    private String name; //username of a player
    private Vector<GameResult> games; // game results

    public PlayerStatistics(String name) {
        this.name = name;
        games = new Vector<GameResult>();
    }
    
    // sets the outcome and the time of the last game played
    public void updateLastGame(boolean win, int timePlayed) {
      GameResult g = games.lastElement();
      g.setWin(win);
      g.setTimePlayed(timePlayed);
    }
    
    // adds new game result with a certain difficulty
    // default outcome - lost, time - 0
    public void addGame(int difficulty) {
        games.add(new GameResult(difficulty));
    }
        
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        for(GameResult res : games) {
            String outcome = res.isWin() ? "won" : "lost";
            sb.append("game difficulty: ").append(res.getDifficulty()).append(" outcome: ").append(outcome).append(" time played: ");
            sb.append(res.getTimePlayed()).append(" sec\n");
        }
        return sb.toString();
    }
    
}

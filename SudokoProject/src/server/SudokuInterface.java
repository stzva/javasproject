
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface SudokuInterface extends Remote {

    int [][] newGame(String difficulty, String username) throws RemoteException;
    void addScore(String username, int timePlayed, boolean win) throws RemoteException;
}

package server;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SudokuServer extends UnicastRemoteObject implements SudokuInterface {

    private int thisPort;
    private String thisAddress;
    private Registry registry;
    private SudokuBoardGenerator generator;

    //loads a new game
    //returns a sudoku board as int[9][9] with a chosen difficulty
    //choice is the number of the populated fields in the board according to the difficulty
    //gets the statistics for the player or creates one if there isn't
    //adds the new game to the statistics
    public int[][] newGame(String difficulty, String username) {
        int choice = 40;
        int level = 1;
        if (difficulty.equals("easy")) {
            choice = 79;
            level = 1;
        } else if (difficulty.equals("medium")) {
            choice = 70;
            level = 2;
        } else if (difficulty.equals("difficult")) {
            choice = 40;
            level = 3;
        }
        PlayerStatistics st = Statistics.getPlayerStatistics(username);
        if (st == null) {
            st = new PlayerStatistics(username);
            Statistics.addPlayerStatistics(st);
        }
        st.addGame(level);

        generator.generate(choice);
        return generator.getBoard();
    }

    //adds the game result of the last game to the player statistics
    @Override
    public void addScore(String username, int timePlayed, boolean win) {
        PlayerStatistics st = Statistics.getPlayerStatistics(username);
        if (st == null) {
            return;
        }
        st.updateLastGame(win, timePlayed);
        Statistics.save();
    }

    public SudokuServer() throws RemoteException {

        try {

            thisAddress = (InetAddress.getLocalHost()).toString();

        } catch (Exception e) {
            throw new RemoteException("can't get inet address.");

        }
        thisPort = 3232;  // this port(registry’s port)
        System.out.println("this address=" + thisAddress + ",port=" + thisPort);
        try {
            registry = LocateRegistry.createRegistry(thisPort);
            registry.rebind("rmiServer", this);
        } catch (RemoteException e) {
            throw e;
        }

        generator = new SudokuBoardGenerator();

    }

    static public void main(String args[]) {

        try {
            SudokuServer s = new SudokuServer();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);

        }
    }
}


package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Statistics {

    private static final File filename = new File("gamestats.bin"); //file that keeps the statistics
    private static HashMap<String, PlayerStatistics> stats; //hashmap that keeps statstics for each player
    private static ObjectOutputStream output;
    private static ObjectInputStream input;

    //static block that creates the hashmap stats the first time class Statistics is used
    // intializes stats if there is already information in the file
    static {
        stats = new HashMap<String, PlayerStatistics>();
        FileInputStream finput = null;
        try {
            finput = new FileInputStream(filename);
            input = new ObjectInputStream(finput);
            try {
                stats = (HashMap<String, PlayerStatistics>) input.readObject();
            } catch (IOException ex) {
                System.out.println("IOException reading hashmap from file");
            } catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFoundException");
            }
            input.close();
            finput.close();
        } catch (FileNotFoundException ex) {
            stats = new HashMap<String, PlayerStatistics>();
            save();
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }
    
   // returns the statistics of a player
    public static PlayerStatistics getPlayerStatistics(String username) {
        return stats.get(username);
    }

    //add stitistics for a player
    public static void addPlayerStatistics(PlayerStatistics st) {
        stats.put(st.getName(), st);
        save();
    }

    //saves the hashmap stats to the file
    public synchronized static void save() {
        FileOutputStream foutput = null;
        try {
            foutput = new FileOutputStream(filename);
            output = new ObjectOutputStream(foutput);
            output.writeObject(stats);
            output.flush();
            output.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                foutput.close();
            } catch (IOException ex) {
                Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //test - reading from the file
    static public void main(String args[]) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("gamestats.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            HashMap<String, PlayerStatistics> stats1 = (HashMap<String, PlayerStatistics>) ois.readObject();
            for(String key : stats1.keySet()) {
                PlayerStatistics ps = stats1.get(key);
                System.out.printf("%s\n", ps.toString());
            }
            ois.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
        } catch (IOException ex) {
            System.out.println("IOException");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException");
        }
    }
}

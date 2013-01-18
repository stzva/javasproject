/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author silvia
 */
import server.SudokuServer;
import client.SudokuClient;
import javax.swing.JFrame;
public class Sudoku {
   static public void main(String args[]) {

        try {
            SudokuServer s = new SudokuServer();
            SudokuClient client = null;
        if (args.length == 0) {
            client = new SudokuClient("localhost", "3232");
        } else {
            client = new SudokuClient(args[0], args[1]);
        }
        client.setResizable(false);
        client.setLocation(450, 150);
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setSize(330, 400);
        client.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);

        }
         
    }
        
}

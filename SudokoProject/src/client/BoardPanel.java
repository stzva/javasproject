/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import server.SudokuBoard;
import server.SudokuInterface;

/**
 *
 * @author Silvia
 */
public class BoardPanel extends JPanel {

    SudokuInterface rmiServer;
    private TimeCounter tCounter;
    private SudokuBoard board;
    private String username;
    GridLayout layouts[];
    private JTextField enterFields[][];
    JPanel panels[];
    private JTextField error;
    private int counter;
    private int[] move; // x,y,number
    private Vector<Move> moves = new Vector<Move>();
    private Vector<Move> movesToBeRedone = new Vector<Move>();

     //listens for a change in a field and validates
     class MyDocumentListener implements DocumentListener {
        private int x, y;

        private MyDocumentListener(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public void insertUpdate(DocumentEvent e) {
            String filledNumber = enterFields[y][x].getText();
            int number = 0;
            if (filledNumber == null || filledNumber.equals("")) {
                return;
            }
            try {
                number = Integer.parseInt(filledNumber);
                
                if (number < 0 || number > 9) {
                    error.setText(filledNumber + " is not between 1 and 9.");
                    return;
                }
                if (board.valid(x, y, number)) {
                    board.put(x, y, number);
                    error.setText("");
                    moves.add(new Move(x, y, number));
                    counter++;
                    //enterFields[y][x].setEditable(false);
                    System.out.println(counter);
                    if (counter == 81) {
                        error.setText("Congratulations!");
                        tCounter.stopCount();
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                enterFields[i][j].setEditable(false);
                            }
                        }
                        tCounter.stopCount();
                        try {
                            rmiServer.addScore(username, tCounter.getTime(), true);
                        } catch (RemoteException ex) {
                            Logger.getLogger(BoardPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        moves.clear();
                    }
                } else {
                    error.setText(filledNumber + " can't be placed hare.");
                    return;
                }

            } catch (NumberFormatException nfe) {
                error.setText(filledNumber + " is not a number.");
                return;
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
     }
    

    public BoardPanel(SudokuInterface rmiServer, JTextField errorM,
            TimeCounter timeCounter, SudokuBoard board, String username) {

        tCounter = timeCounter;
        tCounter.startCount();
        this.rmiServer = rmiServer;
        this.board = board;
        this.username = username;
        this.error = errorM;
        counter = board.populatedFields();
        move = new int[3];

        GridLayout layout = new GridLayout(3, 3, 4, 4);
        setLayout(layout);
        float r = (float) 0.1;
        float g = (float) 0.8;
        float b = (float) 0.3;
        float alpha = (float) 0.5;
        setBackground(new Color(r, g, b, alpha));
        layouts = new GridLayout[9];
        panels = new JPanel[9];
        for (int i = 0; i < 9; i++) {
            layouts[i] = new GridLayout(3, 3, 0, 0);
            panels[i] = new JPanel();
            panels[i].setLayout(layouts[i]);
            add(panels[i]);
        }
        enterFields = new JTextField[9][9];
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                enterFields[y][x] = new JTextField(2);
                enterFields[y][x].setFont(new Font(TOOL_TIP_TEXT_KEY, 0, 18));
                enterFields[y][x].setHorizontalAlignment(JTextField.CENTER);
                int c = (y / 3) * 3 + x / 3;
                panels[c].add(enterFields[y][x]);

                int number = 0;

                number = board.getField(x, y);

                if (number != 0) {
                    enterFields[y][x].setText(Integer.toString(number));
                    enterFields[y][x].setEditable(false);
                } else {
                    enterFields[y][x].setForeground(Color.BLUE);
                    MyDocumentListener doclis = new MyDocumentListener(x, y);
                    enterFields[y][x].getDocument().addDocumentListener(doclis);
                }

            }
        }

        setSize(300, 300);
        setVisible(true);
    }

    //undoes a valid move
    public void undo() {
        error.setText("");
        if (moves.isEmpty()) {
            error.setText("You can't undo.");
        } else {
            Move move = moves.remove(moves.size() - 1);
            System.out.println(move.getX() + " " + move.getY() + " " + move.getNumber() + " undone");
            //moveToBeRedone = new Move(move);
            movesToBeRedone.add(new Move(move));
            int x = move.getX();
            int y = move.getY();
            enterFields[y][x].setText("");
            board.put(x, y, 0);
            counter--;
            enterFields[y][x].setEditable(true);
        }
    }

    //redoes a valid move
    public void redo() {
        error.setText("");
        if (movesToBeRedone.isEmpty()) {
            error.setText("You can't redo.");
        } else {
            Move redone = movesToBeRedone.remove(movesToBeRedone.size() - 1);
            int x = redone.getX();
            int y = redone.getY();
            int number = redone.getNumber();
            enterFields[y][x].setText(Integer.toString(number));

            board.put(x, y, number);
            //moves.add(new Move(x, y, number));
            System.out.println(x + " " + y + " " + number + " added from redo");
        }
    }

    //checks if a board is fiiled
    public boolean isReady() {
        return counter == 81;
    }

    public void setEditableFalse() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                enterFields[i][j].setEditable(false);
            }
        }
        moves.clear();
    }
}

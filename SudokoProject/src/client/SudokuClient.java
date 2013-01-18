package client;

import java.awt.event.WindowEvent;
import server.SudokuBoard;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import server.SudokuBoard;
import server.SudokuInterface;

public class SudokuClient extends JFrame {

    private static SudokuInterface rmiServer;
    private SudokuBoard board;
    private String username;
    private TimeCounter timeCounter;
    private JMenuBar bar;
    private JMenu menu;
    private JTextField errorMessage;
    private JTextField time;
    private BoardPanel boardPanel;
    private final JMenuItem undo, redo, newGame, exit;
    private String possibilities[] = {"easy", "medium", "difficult"};

    public SudokuClient(String serverAddress, String serverPort) {
        super("Sudoku");

        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(
                    serverAddress,
                    (new Integer(serverPort)).intValue());
            rmiServer =
                    (SudokuInterface) (registry.lookup("rmiServer"));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        setLayout(new FlowLayout());

        board = new SudokuBoard();
        username = (String) JOptionPane.showInputDialog(null, "Please choose username");
        if (username == null || username.trim().equals("")) {
            username = "anonymous";
        }

        String difficulty = (String) JOptionPane.showInputDialog(null, "Please choose difficulty",
                "New game", JOptionPane.PLAIN_MESSAGE,
                null, possibilities, "easy");

        try {
            board.setBoard(rmiServer.newGame(difficulty, username));
        } catch (RemoteException ex) {
            Logger.getLogger(SudokuClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        errorMessage = new JTextField(20);
        errorMessage.setEditable(false);
        errorMessage.setForeground(Color.red);
        errorMessage.setHorizontalAlignment(JTextField.CENTER);

        time = new JTextField(15);
        time.setEditable(false);
        time.setHorizontalAlignment(JTextField.CENTER);
        timeCounter = new TimeCounter(time);
        boardPanel = new BoardPanel(rmiServer, errorMessage, timeCounter, board, username);

        bar = new JMenuBar();
        setJMenuBar(bar);
        menu = new JMenu("Options");
        menu.setSize(20, 10);
        //menu.setMnemonic(KeyEvent.VK_A);
        newGame = new JMenuItem("New game");
        menu.add(newGame);
        newGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String difficulty = (String) JOptionPane.showInputDialog(null,
                        "Please choose difficulty", "New game",
                        JOptionPane.PLAIN_MESSAGE, null, possibilities, "");
                if (difficulty == null || difficulty.trim().equals("")) {
                    return;
                }
                errorMessage.setText("");
                time.setText("");
                boolean outcome = boardPanel.isReady();
                if (!outcome) {
                    timeCounter.stopCount();
                    try {
                        rmiServer.addScore(username, timeCounter.getTime(), outcome);
                    } catch (RemoteException ex) {
                        Logger.getLogger(SudokuClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                boardPanel.setVisible(false);
                remove(boardPanel);
                try {
                    board.setBoard(rmiServer.newGame(difficulty, username));
                } catch (RemoteException ex) {
                    Logger.getLogger(SudokuClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                timeCounter = new TimeCounter(time);
                boardPanel = new BoardPanel(rmiServer, errorMessage, timeCounter, board, username);
                add(boardPanel);
                boardPanel.setVisible(true);
            }
        });

        undo = new JMenuItem("Undo");
        menu.add(undo);
        undo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.undo();
            }
        });
        redo = new JMenuItem("Redo");
        redo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.redo();
            }
        });
        menu.add(redo);

        exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!boardPanel.isReady()) {
                    timeCounter.stopCount();
                    try {
                        rmiServer.addScore(username, timeCounter.getTime(), false);
                    } catch (RemoteException ex) {
                        Logger.getLogger(SudokuClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.exit(0);
            }
        });
        menu.add(exit);
        bar.add(menu);
        add(time);
        add(errorMessage);
        add(boardPanel);
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (!boardPanel.isReady()) {
                    timeCounter.stopCount();
                    try {
                        rmiServer.addScore(username, timeCounter.getTime(), false);
                    } catch (RemoteException ex) {
                        Logger.getLogger(SudokuClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }
            @Override
            public void windowIconified(WindowEvent e) {
            }
            @Override
            public void windowDeiconified(WindowEvent e) {
            }
            @Override
            public void windowActivated(WindowEvent e) {
            }
            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    static public void main(String args[]) {

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
    }
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;

public class GameWindow {
    
    public static JFrame _frame = new JFrame("CS1530 Poker Game");
    public static Game   _game  = null;

    public static void main(String[] args) {
        JMenuBar  _menuBar  = new JMenuBar();
        JMenu     _menuFile = new JMenu("File");
        JMenu     _menuGame = new JMenu("Game");
        JMenuItem _newGame  = new JMenuItem("New Game");

        ActionListener newGameListener = new NewGameListener();
        

        new GameWindow();

        _frame.setSize(800, 600);
        // _frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        _frame.setVisible(true);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setLayout(new GridLayout(0, 1));

        _newGame.addActionListener(newGameListener);

        _menuGame.add(_newGame);

        _menuBar.add(_menuFile);
        _menuBar.add(_menuGame);
        _frame.setJMenuBar(_menuBar);
    }

    private static class NewGameListener implements ActionListener {
    
        public void actionPerformed(ActionEvent e) {
            _game = new Game();
            
            _frame.add(_game, BorderLayout.NORTH);
            _frame.validate();
            _frame.repaint();
        }
    }
}
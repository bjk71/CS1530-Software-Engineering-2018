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
        JMenuItem _saveGame = new JMenuItem("Save");
        JMenuItem _fileExit = new JMenuItem("Exit");
        JMenuItem _newGame  = new JMenuItem("New Game");
        JMenuItem _loadGame = new JMenuItem("Load Game");
        JMenuItem _exitGame = new JMenuItem("Exit Game");

        ActionListener newGameListener  = new NewGameListener();
        ActionListener exitGameListener = new ExitGameListener();
        

        new GameWindow();

        _frame.setSize(800, 600);
        // _frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        _frame.setVisible(true);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setLayout(new GridLayout(0, 1));

        _newGame.addActionListener(newGameListener);
        _exitGame.addActionListener(exitGameListener);

        _menuFile.add(_saveGame);
        _menuFile.add(_fileExit);

        _menuGame.add(_newGame);
        _menuGame.add(_loadGame);
        _menuGame.add(_exitGame);

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

    private static class ExitGameListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            _frame.remove(_game);
            _frame.validate();
            _frame.repaint();
        }
    }
}
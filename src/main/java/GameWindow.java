import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;

public class GameWindow {
    
    public static void main(String[] args) {
        System.out.println("GameWindow");
        JFrame    _frame    = new JFrame("CS1530 Poker Game");
        JButton[] _buttons  = new JButton[9];
        JPanel    _ttt      = new JPanel();
        JPanel    _newPanel = new JPanel();

        JMenuBar  _menuBar  = new JMenuBar();
        JMenu     _menuFile = new JMenu("File");
        JMenu     _menuGame = new JMenu("Game");

        JMenuItem _newGame  = new JMenuItem("New Game");

        new GameWindow();

        _frame.setSize(800, 600);
        _frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        _frame.setVisible(true);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _ttt.setLayout(new GridLayout(3, 3));
        _newPanel.setLayout(new FlowLayout());
        _frame.add(_ttt, BorderLayout.NORTH);
        _frame.add(_newPanel, BorderLayout.SOUTH);

        _menuGame.add(_newGame);

        _menuBar.add(_menuFile);
        _menuBar.add(_menuGame);
        _frame.setJMenuBar(_menuBar);
    }

}
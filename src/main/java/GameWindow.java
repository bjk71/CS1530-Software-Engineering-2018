import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class GameWindow {
    
    public static JFrame     _frame   = new JFrame("CS1530 Poker Game");
    public static Title      _title   = new Title();
    public static Game       _game    = null;
    public static Load       _load    = null;
    public static boolean    gameOpen = false;

    public static void main(String[] args) {
        JMenuBar  _menuBar  = new JMenuBar();
        JMenu     _menuFile = new JMenu("File");
        JMenu     _menuGame = new JMenu("Game");
        JMenuItem _fileExit = new JMenuItem("Exit");
        JMenuItem _newGame  = new JMenuItem("New Game");
        JMenuItem _loadGame = new JMenuItem("Load Game");
        JMenuItem _saveGame = new JMenuItem("Save Game");
        JMenuItem _exitGame = new JMenuItem("Exit Game");

        ActionListener newGameListener  = new NewGameListener();
        ActionListener loadGameListener = new LoadGameListener();
        ActionListener saveGameListener = new SaveGameListener();
        ActionListener exitGameListener = new ExitGameListener();
        
       

        _frame.setSize(1600, 900);
        _frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        _frame.setVisible(true);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setLayout(new GridLayout(0, 1));

        _fileExit.addActionListener(new ExitListener());
        _newGame.addActionListener(newGameListener);
        _loadGame.addActionListener(loadGameListener);
        _saveGame.addActionListener(saveGameListener);
        _exitGame.addActionListener(exitGameListener);

        _menuFile.add(_fileExit);

        _menuGame.add(_newGame);
        _menuGame.add(_loadGame);
        _menuGame.add(_saveGame);        
        _menuGame.add(_exitGame);

        _menuBar.add(_menuFile);
        _menuBar.add(_menuGame);

        _frame.setJMenuBar(_menuBar);
        _frame.add(_title);
        
        _frame.setVisible(true);
    }

    private static class ExitListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private static class NewGameListener implements ActionListener {
    
        public void actionPerformed(ActionEvent e) {
            if(gameOpen == false)
            {
                _load = null;
                _game = new Game();
                
                _frame.remove(_title);
                _frame.add(_game, BorderLayout.NORTH);
                _frame.validate();
                _frame.repaint();

                gameOpen = true;
            } else {
                // TODO
                // Are you sure you want to leave the current game?
                
            }
        }
    }

    private static class LoadGameListener implements ActionListener {
    
        public void actionPerformed(ActionEvent e) {
            if(gameOpen == false)
            {
                _load = new Load(_frame);

                _frame.remove(_title);
                _frame.add(_load, BorderLayout.NORTH);
                _frame.validate();
                _frame.repaint();

                gameOpen = true;
                

            } else {
                // TODO
                // Are you sure you want to leave the current game?
                
            }
        }
    }

    private static class SaveGameListener implements ActionListener {
    
        public void actionPerformed(ActionEvent e) {
            if(gameOpen == true)
            {
                if (_load != null){
                    _game = _load.updateGame();
                }
                
                try {
                    FileOutputStream fos = new FileOutputStream("SaveGame.ser");
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    // write object to file
                    oos.writeObject(_game);
                    // closing resources
                    oos.close();
                    fos.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

            } else {
                // TODO
                // Are you sure you want to leave the current game?
                
            }
        }
    }

    private static class ExitGameListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if(gameOpen == true)
            {
                if (_load != null) {
                    _game = _load.updateGame();
                }
                
                _frame.remove(_game);
                _frame.add(_title);
                _frame.validate();
                _frame.repaint();    

                gameOpen = false;
            }
        }
    }
}
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Driver class for Poker UI Swing Application.
 */
public class GameWindow {
    private static final Color POKER_GREEN = new Color(71, 113, 72);

    public static JFrame  _frame   = new JFrame("CS1530 Poker Game");
    public static Title   _title   = new Title();
    public static Game    _game    = null;
    public static Load    _load    = null;
    public static boolean gameOpen = false;

    public static void main(String[] args) {
        try {
            // Set cross-platform Java L&F (also called "Metal")
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
        } catch (Exception e) {}


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

        _title.add(createGameButtons(), BorderLayout.PAGE_END);

        _frame.setJMenuBar(_menuBar);
        _frame.add(_title);
        
        _frame.setVisible(true);
    }

    /**
     * Create a padded panel and add New/Load Game buttons to it.
     * @return JPanel containing new buttons.
     */
    private static JPanel createGameButtons() {
        JPanel  _thisPanel      = new JPanel();
        JPanel  _buttonPanel    = new JPanel();
        JPanel  _paddingPanel   = new JPanel();
        JButton _newGameButton  = new JButton("New Game");
        JButton _loadGameButton = new JButton("Load Game");
        JButton _extiGameButton = new JButton("Exit");

        _newGameButton.addActionListener(new NewGameListener());
        _newGameButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        _newGameButton.setPreferredSize(new Dimension(250, 100));

        _loadGameButton.addActionListener(new LoadGameListener());
        _loadGameButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        _loadGameButton.setPreferredSize(new Dimension(250, 100));

        _extiGameButton.addActionListener(new ExitListener());
        _extiGameButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        _extiGameButton.setPreferredSize(new Dimension(70, 35));

        _buttonPanel.setBackground(POKER_GREEN);
        _buttonPanel.add(_newGameButton);
        _buttonPanel.add(_loadGameButton);

        _paddingPanel.setBackground(POKER_GREEN);
        _paddingPanel.setPreferredSize(new Dimension(50, 50));
        _paddingPanel.add(_extiGameButton);

        _thisPanel.setBackground(POKER_GREEN);
        _thisPanel.setLayout(new GridLayout(2, 1));

        _thisPanel.add(_buttonPanel);
        _thisPanel.add(_paddingPanel);
        
        return _thisPanel;
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
                    _frame.remove(_load);
                }
                if (_game != null){
                    _frame.remove(_game);
                }
                _frame.add(_title);
                _frame.validate();
                _frame.repaint();    

                gameOpen = false;
            }
        }
    }
}
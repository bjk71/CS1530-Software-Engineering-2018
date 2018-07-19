import java.awt.*;
import javax.swing.*;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import java.awt.event.*;

public class Load extends JPanel {
    private final Color POKER_GREEN  = new Color(71, 113, 72);
    private final Color WHITE        = new Color(255, 255, 255);
    
    private JFrame _gameFrame;
    private JPanel _loadPanel;
    private Game   _game;

    public Load(JFrame frame) {
        this._gameFrame=frame;
        this._loadPanel=this;
        initComponents();
        setVisible(true);
        
    }


    private void initComponents() {
        JPanel            _loadTitle   = new JPanel();
        JPanel            _saveFiles   = new JPanel();
        JPanel            _gamePanel   = new JPanel();
        JPanel            _startButton = new JPanel();

        JLabel            _titleLabel  = new JLabel();
        JButton           _startGame   = new JButton();
        JLabel            _gameLabel   = new JLabel();
        JComboBox<String> _gameID      = new JComboBox<>();

        _saveFiles.setLayout(new FlowLayout(FlowLayout.LEFT));

        setLayout(new GridLayout(3,1));

        _gamePanel.setLayout(new GridLayout(1, 2, 50, 100));

        _loadTitle.setBackground(POKER_GREEN);
        _saveFiles.setBackground(POKER_GREEN);
        _gamePanel.setBackground(POKER_GREEN);
        _startButton.setBackground(POKER_GREEN);

        _titleLabel.setForeground(WHITE);
        
        _titleLabel.setFont(new Font("Courier", Font.PLAIN, 60));
        _titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        _titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        _titleLabel.setText("Load a Previous Game from a Save File!");

        _gameLabel.setForeground(WHITE);
        _gameLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _gameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        _gameLabel.setVerticalAlignment(SwingConstants.TOP);
        _gameLabel.setText("Save Game Name:");

        //_gameID.setModel(new DefaultComboBoxModel<>( loadSaveGames()   ));
        _gameID.setModel(new DefaultComboBoxModel<>( new String[]{"SaveGame.ser"}));        
        _gameID.setPreferredSize(new Dimension( 400, 50 ));
        _gameID.setSelectedItem(4);
        _gameID.setFont(new Font("Courier", Font.PLAIN, 30));
        _saveFiles.add(_gameID);

        _startGame.setText("Start Game");
        _startGame.setFont(new Font("Courier", Font.PLAIN, 30));
        _startGame.setPreferredSize(new Dimension( 300, 100 ));
        _startGame.setVerticalAlignment(SwingConstants.CENTER);
        _startGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    String saveString = (String) _gameID.getSelectedItem();
                    File saveFile = new File(saveString);
                    FileInputStream fis = new FileInputStream(saveFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    //load object from file
                    _game = (Game) ois.readObject();

                    //close resources
                    ois.close();
                    fis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                } catch (ClassNotFoundException cnfe) {
                    cnfe.printStackTrace();
                }
                
                _game.reinitImages();

                removeAll();
                _gameFrame.remove(_loadPanel);
                _gameFrame.add(_game);

                _gameFrame.revalidate();
                _gameFrame.repaint();
                _game.play();

            }
        });

        _loadTitle.add(_titleLabel);
        _gamePanel.add(_gameLabel);
        _gamePanel.add(_saveFiles);
        _startButton.add(_startGame);

        add(_loadTitle);
        add(_gamePanel);
        add(_startButton);

        revalidate();
        repaint();

    }

    public Game updateGame() {
        return this._game;
    }

    /**
     * Helper method that returns a list of all save games
     * in the gamelog directory
     * @return gameNames    String array of all saved games     * 
     */
    private String[] loadSaveGames() {
        ArrayList<String> gameNames = new ArrayList<String>();
        File[] logs = new File("gamelog").listFiles();

        for (File file : logs) {
            if (file.isFile()) {
                gameNames.add(file.getName());
            }
        }

        return (String[]) gameNames.toArray();
    }


}
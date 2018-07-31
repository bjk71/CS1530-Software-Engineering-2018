import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * JPanel for Loading a Poker game from a save file
 */
public class Load extends JPanel {
    private final Color POKER_GREEN  = new Color(71, 113, 72);
    private final Color WHITE        = new Color(255, 255, 255);
    private final Color RED          = new Color(255, 0, 0);
    
    private JFrame _gameFrame;
    private JPanel _loadPanel;
    private Game   _game;

    public Load(JFrame frame) {
        this._gameFrame=frame;
        this._loadPanel=this;
        initComponents();
        setVisible(true);
        
    }

    /**
     * Initalizes Swing UI components
     */
    private void initComponents() {
        JPanel            _loadTitle   = new JPanel();
        JPanel            _saveFiles   = new JPanel();
        JPanel            _gamePanel   = new JPanel();
        JPanel            _startButton = new JPanel();
        JPanel            _error       = new JPanel();

        JLabel            _titleLabel  = new JLabel();
        JButton           _startGame   = new JButton();
        JLabel            _gameLabel   = new JLabel();
        JComboBox<String> _gameID      = new JComboBox<>();

        _saveFiles.setLayout(new FlowLayout(FlowLayout.LEFT));

        setLayout(new GridLayout(4,1));

        _gamePanel.setLayout(new GridLayout(1, 2, 50, 100));

        _loadTitle.setBackground(POKER_GREEN);
        _saveFiles.setBackground(POKER_GREEN);
        _gamePanel.setBackground(POKER_GREEN);
        _startButton.setBackground(POKER_GREEN);
        _error.setBackground(POKER_GREEN);

        _titleLabel.setForeground(WHITE);
        _titleLabel.setFont(new Font("Courier", Font.PLAIN, 60));
        _titleLabel.setHorizontalAlignment(JLabel.CENTER);
        _titleLabel.setVerticalAlignment(JLabel.CENTER);
        _titleLabel.setText("Load a Previous Game!");

        _gameLabel.setForeground(WHITE);
        _gameLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _gameLabel.setHorizontalAlignment(JLabel.RIGHT);
        _gameLabel.setVerticalAlignment(JLabel.TOP);
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
        _startGame.setVerticalAlignment(JLabel.CENTER);
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

                    _game.reinitImages();

                    removeAll();
                    _gameFrame.remove(_loadPanel);
                    _gameFrame.add(_game);
    
                    _gameFrame.revalidate();
                    _gameFrame.repaint();
                    _game.play();

                } catch (IOException ioe) {
                    _error.removeAll();
                    _error.add(initFNFE());
                    revalidate();
                    repaint();
                } catch (ClassNotFoundException cnfe) {
                    cnfe.printStackTrace();
                }

            }
        });

        _loadTitle.add(_titleLabel);
        _gamePanel.add(_gameLabel);
        _gamePanel.add(_saveFiles);
        _startButton.add(_startGame);

        add(_loadTitle);
        add(_gamePanel);
        add(_startButton);
        add(_error);

        revalidate();
        repaint();

    }

    /**
     * Return an updated refrence to the Game that has been loaded from save file
     * @return  Updated <i>Game</i> reference
     */
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

    /**
     * Initalizes the JLabel for a FileNotFound IO exception when loading game
     * @return  JLabel object for errror message
     */
    private JLabel initFNFE() {
        JLabel _eLabel = new JLabel();

        _eLabel.setForeground(RED);
        _eLabel.setFont(new Font("Courier", Font.PLAIN, 48));
        _eLabel.setHorizontalAlignment(JLabel.CENTER);
        _eLabel.setVerticalAlignment(JLabel.CENTER);
        _eLabel.setText("ERROR: Save Game could not be loaded.");

        return _eLabel;
    }

}
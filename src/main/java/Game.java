import java.awt.*;
import javax.swing.*;
import java.io.*;

import javax.imageio.*;
import java.awt.event.*;

public class Game extends JPanel {
    private final Color POKER_GREEN = new Color(71, 113, 72);
    private final Color WHITE       = new Color(255, 255, 255);

    private Player[] players    = null;
    private Deck     deck       = null;
    private Card[]   tableCards = null;
    private Card     cardBack   = null;
    private int      pot        = 0;
    

    public Game() {
        initalizeStartGrid();
        setVisible(true);
    }
    

    public Game(File saveFile) {
      // initialize game settings from save file
    }
    
    
    private void initalizeStartGrid(){    
        JPanel             _startTitle        = new JPanel();
        JPanel             _playerName        = new JPanel();
        JPanel             _numOpponents      = new JPanel();
        JPanel             _startButton       = new JPanel();
        JPanel             _playerNamePanel   = new JPanel();
        JPanel             _numOpponentsPanel = new JPanel();

        JLabel             _titleLabel        = new JLabel();        
        JButton            _startGame         = new JButton();
        JLabel             _playerNameLabel   = new JLabel();
        JTextField         _playerNameInput   = new JTextField();
        JLabel             _numOppLabel       = new JLabel();
        JComboBox<Integer> _numOpp            = new JComboBox<>();

        _playerNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        _numOpponentsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        setLayout(new GridLayout(4,1));
        
        _playerName.setLayout(new GridLayout(1, 2, 50, 100));
        _numOpponents.setLayout(new GridLayout(1, 2, 50, 100));
        
        _startTitle.setBackground(POKER_GREEN);
        _playerName.setBackground(POKER_GREEN);
        _numOpponents.setBackground(POKER_GREEN);
        _startButton.setBackground(POKER_GREEN);
        _playerNamePanel.setBackground(POKER_GREEN);
        _numOpponentsPanel.setBackground(POKER_GREEN);
        
        _titleLabel.setForeground(WHITE);
        _titleLabel.setFont(new Font("Courier", Font.PLAIN, 60));
        _titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        _titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        _titleLabel.setText("Welcome to Pocket Rockets Poker!");
        
        _playerNameLabel.setForeground(WHITE);
        _playerNameLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _playerNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        _playerNameLabel.setVerticalAlignment(SwingConstants.TOP);
        _playerNameLabel.setText("Name:");
        
        _playerNameInput.setPreferredSize(new Dimension( 100, 50 ));
        _playerNameLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _playerNameInput.setHorizontalAlignment(SwingConstants.CENTER);
        _playerNamePanel.add(_playerNameInput);

        _numOppLabel.setForeground(WHITE);
        _numOppLabel.setFont(new Font("Courier", Font.PLAIN, 40));
        _numOppLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        _numOppLabel.setVerticalAlignment(SwingConstants.TOP);
        _numOppLabel.setText("Number of Opponents:");
        
        _numOpp.setModel(new DefaultComboBoxModel<>(new Integer[] { 1, 2, 3, 4, 5, 6, 7 }));        
        _numOpp.setPreferredSize(new Dimension( 100, 50 ));
        _numOpponentsPanel.add(_numOpp);
        
        _startGame.setText("Start Game");
        _startGame.setFont(new Font("Courier", Font.PLAIN, 30));
        _startGame.setPreferredSize(new Dimension( 300, 100 ));
        _startGame.setVerticalAlignment(SwingConstants.CENTER);
        _startGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: REGEX user input
                String name         = _playerNameInput.getText();
                int    numOpponents = (int) _numOpp.getSelectedItem();

                removeAll();
                createNewGame (name, numOpponents);
            }
        });
        
        _startTitle.add(_titleLabel);
        _playerName.add(_playerNameLabel);
        _playerName.add(_playerNamePanel);
        _numOpponents.add(_numOppLabel);
        _numOpponents.add(_numOpponentsPanel);
        _startButton.add(_startGame);
        
        add(_startTitle);
        add(_playerName);
        add(_numOpponents);
        add(_startButton);       
    }
    
    /**
     * Create new game objects and and initialize players.
     * @param userName Player name.
     * @param numOfAI  Number of AI players to initialize.
     */
    private void createNewGame(String userName, int numOfAI) {
        JPanel     _top            = new JPanel();
        JPanel     _bot            = new JPanel();
        JPanel[]   _aiPlayers      = new JPanel[numOfAI];
        JPanel[]   _tableAndUser   = new JPanel[2];
        JPanel[][] _aiPlayersTB    = new JPanel[numOfAI][2];
        JPanel[][] _tableAndUserTB = new JPanel[2][2];

        JLabel     _tableLabel     = new JLabel("Pot: ");
        JLabel     _pot            = new JLabel("");

        GameUtils  utils           = new GameUtils();
        String[]   aiPlayerNames   = utils.getAINames(numOfAI, userName);
        
        
        try {
            Image back = ImageIO.read(this.getClass().getResource("/back.png"));
            cardBack = new Card("Back", back);
        } catch (IOException ioex) {
            System.exit(1);
        }

        deck       = new Deck();
        tableCards = new Card[5];
        players    = new Player[numOfAI + 1];
        
        setLayout(new GridLayout(3, 1));
        _top.setLayout(new GridLayout(1, numOfAI, 50, 100));
        _bot.setLayout(new GridLayout(1, 2, 50, 100));
        
        setBackground(POKER_GREEN);
        _top.setBackground(POKER_GREEN);
        _bot.setBackground(POKER_GREEN);
        
        for(int i = 0; i < numOfAI; i++) {
            _aiPlayers[i] = new JPanel();
            _top.add(_aiPlayers[i]);
            _aiPlayers[i].setBackground(POKER_GREEN);
            _aiPlayers[i].setLayout(new GridLayout(2, 1, 50, 50));
            
            _aiPlayersTB[i][0] = new JPanel();
            _aiPlayersTB[i][1] = new JPanel();
            _aiPlayers[i].add(_aiPlayersTB[i][0]);
            _aiPlayers[i].add(_aiPlayersTB[i][1]);
            _aiPlayersTB[i][0].setBackground(POKER_GREEN);
            _aiPlayersTB[i][1].setBackground(POKER_GREEN);
        }
        
        for(int i = 0; i < 2; i++) {
            _tableAndUser[i] = new JPanel();
            _bot.add(_tableAndUser[i]);
            _tableAndUser[i].setBackground(POKER_GREEN);
            _tableAndUser[i].setLayout(new GridLayout(2, 1, 50, 50));
            
            _tableAndUserTB[i][0] = new JPanel();
            _tableAndUserTB[i][1] = new JPanel();
            _tableAndUser[i].add(_tableAndUserTB[i][0]);
            _tableAndUser[i].add(_tableAndUserTB[i][1]);
            _tableAndUserTB[i][0].setBackground(POKER_GREEN);
            _tableAndUserTB[i][1].setBackground(POKER_GREEN);
        }

        // Make Labels and distribute starting cards
        _tableLabel.setForeground(WHITE);
        _tableLabel.setFont(new Font("Courier", Font.PLAIN, 28));
        _tableAndUserTB[0][0].add(_tableLabel, BorderLayout.NORTH);
        
        // Display the pot value
        _pot.setForeground(WHITE);
        _pot.setText("$" + String.valueOf(pot));
        _pot.setFont(new Font("Courier", Font.PLAIN, 28));
        _tableAndUserTB[0][0].add(_pot, BorderLayout.NORTH);
        
        for(int i = 0; i < 5; i++) {
            tableCards[i] = deck.draw();
            displayCard(_tableAndUserTB[0][1], tableCards[i]);
        }
        
        //For the players section
        for(int i=0; i<players.length; i++){
            Card[] playerHand   = new Card[2];
            JLabel _playerLabel = null;
            JLabel _playerCash  = null;
            int    playerCash   = 0;

            playerHand[0] = deck.draw();
            playerHand[1] = deck.draw();
            //user
            if(i == 0){
                // String yourLabel = userName + ": ";
                _playerLabel = new JLabel(userName + ": ");
                _playerLabel.setForeground(WHITE);
                _playerLabel.setFont(new Font("Courier", Font.PLAIN, 28));
                _tableAndUserTB[1][0].add(_playerLabel, BorderLayout.NORTH);
                
                players[i] = new Player(userName, playerHand, 1000, true);
                
                // Display user's cash
                playerCash   = players[i].getCash();
                _playerCash = new JLabel("");
                _playerCash.setForeground(WHITE);
                _playerCash.setText("$" + String.valueOf(players[i].getCash()));
                _playerCash.setFont(new Font("Courier", Font.PLAIN, 28));
                _tableAndUserTB[1][0].add(_playerCash, BorderLayout.NORTH);
                
                // Display player's cash
                _playerCash.setForeground(WHITE);
                _playerCash.setText("$" + String.valueOf(playerCash));
                _playerCash.setFont(new Font("Courier", Font.PLAIN, 28));
                _tableAndUserTB[1][0].add(_playerCash, BorderLayout.NORTH);
                
                displayCard(_tableAndUserTB[1][1], playerHand[0]);
                displayCard(_tableAndUserTB[1][1], playerHand[1]);
            } else { // Initialize AI player
                String aiName = aiPlayerNames[i - 1];

                players[i]   = new Player(aiName, playerHand, 1000, true);
                _playerLabel = new JLabel(aiName + ": ");
                playerCash   = players[i].getCash();
                _playerCash  = new JLabel("");
                
                _playerLabel.setForeground(WHITE);
                _playerLabel.setFont(new Font("Courier", Font.PLAIN, 28));
                _aiPlayersTB[i-1][0].add(_playerLabel, BorderLayout.NORTH);
                                
                // Display AI's cash
                _playerCash.setForeground(WHITE);
                _playerCash.setText("$" + String.valueOf(playerCash));
                _playerCash.setFont(new Font("Courier", Font.PLAIN, 28));
                _aiPlayersTB[i-1][0].add(_playerCash, BorderLayout.NORTH);
                
                displayCard(_aiPlayersTB[i-1][1], cardBack);
                displayCard(_aiPlayersTB[i-1][1], cardBack);

                //displayCard(_aiPlayersTB[i-1][1], playerHand[0]);
                //displayCard(_aiPlayersTB[i-1][1], playerHand[1]);
            }
        }
        
        // Add panels and repaint
        add(_top);
        add(_bot);

        //TESTING_________________________________________________________________________
        JLabel _results = new JLabel(utils.determineBestHand(players, tableCards, pot));
        _results.setForeground(WHITE);
        _results.setFont(new Font("Courier", Font.PLAIN, 28));
        _results.setHorizontalAlignment(SwingConstants.CENTER);
        add(_results, BorderLayout.SOUTH);
        //________________________________________________________________________________
        
        revalidate();
        repaint();
    }
    
    
    /**
     * Display card in JPanel container.
     * @param _loc Panel to display card in.
     * @param card Card to display.
     */
    private void displayCard(JPanel _loc, Card card){
        Image  resizedCard  = card.getFace().getScaledInstance(75, 105,  java.awt.Image.SCALE_SMOOTH);
        JLabel _cardDisplay = new JLabel(new ImageIcon(resizedCard));

        _loc.add(_cardDisplay, BorderLayout.SOUTH);
    }
    
}
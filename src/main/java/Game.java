import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


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

        String[]   aiPlayerNames   = getAINames(numOfAI, userName);
        GameUtils  utils           = new GameUtils();
        
        try {
            Image back = ImageIO.read(this.getClass().getResource("/back.png"));
            cardBack = new Card("Back", back);
        } catch (IOException ioex) {
            System.exit(1);
        }

        deck       = new Deck();
        tableCards = new Card[5];
        players    = new Player[numOfAI + 1];
        
        setLayout(new GridLayout(2, 1));
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
                // int yourCash = players[i].getCash();
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
                
                //displayCard(_aiPlayersTB[i-1][1], cardBack);
                //displayCard(_aiPlayersTB[i-1][1], cardBack);

                displayCard(_aiPlayersTB[i-1][1], playerHand[0]);
                displayCard(_aiPlayersTB[i-1][1], playerHand[1]);
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

        playGame();
        
    }

    private void playGame() {

        Action minAction = null;
        int i, j, k;
        int playersLeft;

        while (playersLeft >= 2) {                    //play game while at least 2 players have chips
            startHand(); //TODO: Reinit Deck + Give each player Cards
            minAction = null;
            for (i=0; i<4; i++) {                       //pre-flop, flop, turn, river
                if (i==1){           //flop
                    for (k=0; k<3; k++){
                        tableCards[k] = deck.draw();
                    }
                } else if (i==2){    //turn
                    tableCards[3] = deck.draw();
                } else if (i==3) {   //river
                    tableCards[4] = deck.draw();
                }
                for (j=0; j<players.length; j++){    //number of players
                    if (!players[j].isPlayingHand()) continue;   //skip if not in hand (folded)
                    
                    minAction = TurnObj.turn(players[j], minAction); // TODO
                    //Need to update Player.setPlayingHand(false) if they folded
                    //Send Player + Current minimum Action
                    //Return New minimum Action
                }

            }

            WinObj.handCompare(tableCards, players); //TODO: determine who won
            //NEED TO UPDATE playersLeft variable if someone runs out of chips + remove them from players Array
            endHand(); //TODO: update UI, pot, clear community cards, and hands; Reset all players to playing --> Player.setPlayingHand(true) 

        }

    //TODO: DISPLAY WINNER IN SWING

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


    /**
     * Return up to nine random names for AI players. Checks against playerName to ensure uniqueness.
     * @param  numOfAI    The number of AI player names to return.
     * @param  playerName Player name to check AI names against.
     * @return           String array of AI player names.
     */
    private String[] getAINames(int numOfAI, String playerName) {
        String[] namesList  = {"Daniel", "Erik", "Antonio", "Fedor","Phil", "Johnathan", "Scott", "Elton", "Brian"};
        String[] returnList = new String[numOfAI];
        int      listLength = 0;
        int      index      = 0;
        
        while(listLength < numOfAI) {
            if(!namesList[index].equals(playerName)) {
                returnList[listLength] = namesList[index];
                listLength++;
            }
            index++;
        }

        return returnList;
    }

    private String determineBestHand(){
        ArrayList<Player> remainingPlayers = new ArrayList<Player>();
        ArrayList<Player> playersWithBestHand = new ArrayList<Player>();
        int highestHighCard = 0;
        int highestHighCard2 = 0;
        String bestHand;
        String suit;

        for(Player p : players){
            if(p.isPlayingHand()){
                remainingPlayers.add(p);
            }
        }

        //Check if only one player remains
        if(remainingPlayers.size() == 1){
            distributePot(remainingPlayers);
            return remainingPlayers.get(0).getName() + " has won as the last man Standing!";
        }

        //Rather than always having to check to arrays, combine community cards with each players'
        for(Player p : remainingPlayers){
            //TODO: Make sure only non null cards are added to new array
            ArrayList<Card> allAvailableCards = new ArrayList<Card>();
            allAvailableCards.addAll(Arrays.asList(tableCards));
            allAvailableCards.addAll(Arrays.asList(p.getCards()));

            Card[] hand = new Card[allAvailableCards.size()];
            hand = allAvailableCards.toArray(hand);
            p.setCards(hand);
        }

        //Determine best hand
        //Royal/Straight Flush?-------------------------------------------------------------------------
        for(Player p : remainingPlayers){
            suit = containsFlush(p.getCards());
            int highCard = containsStraight(p.getCards());
            if(!suit.equals("") && highCard >= highestHighCard){
                if(highCard > highestHighCard){
                    highestHighCard =  highCard;
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "a Royal Flush!!!";
            } else if(highestHighCard == 13){ //King
                bestHand = "a King's High Straight Flush!!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a Queen's High Straight Flush!!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a Jack's High Straight Flush!!";
            } else {
                bestHand = "a " + highestHighCard + "'s High Straight Flush!!";
            }

            distributePot(playersWithBestHand);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Four of a kind?-------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int highCard = containsNOfAKind(p.getCards(), 4);
            if(highCard >= highestHighCard){
                if(highCard > highestHighCard){
                    highestHighCard =  highCard;
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "four Ace's!!";
            } else if(highestHighCard == 13){ //King
                bestHand = "four Kings!!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "four Queens!!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "four Jacks!!";
            } else {
                bestHand = "four " + highestHighCard + "'s!!";
            }

            distributePot(playersWithBestHand);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Full House?-----------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsFullHouse(p.getCards());
            if(highCards[0] >= highestHighCard){
                if(highCards[0] > highestHighCard){
                    highestHighCard =  highCards[0];
                    highestHighCard2 = highCards[1];
                    playersWithBestHand = new ArrayList<Player>();
                } else if(highCards[1] > highestHighCard2){
                    highestHighCard2 = highCards[1];
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "a full house, Ace's full of ";
            } else if(highestHighCard == 13){ //King
                bestHand = "a full house, Kings full of ";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a full house, Queens full of ";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a full house, Jacks full of ";
            } else {
                bestHand = "a full house, " + highestHighCard + "'s full of ";
            }

            if(highestHighCard2 == 14){ //Ace
                bestHand += "Ace's!!";
            } else if(highestHighCard2 == 13){ //King
                bestHand += "Kings!!";
            } else if(highestHighCard2 == 12){ //Queen
                bestHand += "Queens!!";
            } else if(highestHighCard2 == 11){ //Jack
                bestHand += "Jacks!!";
            } else {
                bestHand += highestHighCard2 + "'s!!";
            }

            distributePot(playersWithBestHand);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Flush?----------------------------------------------------------------------------------------
        highestHighCard = 0;
        suit = "";
        for(Player p : remainingPlayers){
            suit = containsFlush(p.getCards());
            if(!suit.equals("")){
                int highCard = determineHighCard(p.getCards(), suit);
                if(highCard > highestHighCard){
                    highestHighCard =  highCard;
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "an Ace High Flush!";
            } else if(highestHighCard == 13){ //King
                bestHand = "a King's High Flush!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a Queen's High Flush!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a Jack's High Flush!";
            } else {
                bestHand = "a " + highestHighCard + "'s High Flush!";
            }

            distributePot(playersWithBestHand);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Straight?-------------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int highCard = containsStraight(p.getCards());
            if(highCard >= highestHighCard){
                if(highCard > highestHighCard){
                    highestHighCard =  highCard;
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "an Ace High Straight!";
            } else if(highestHighCard == 13){ //King
                bestHand = "a King's High Straight!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a Queen's High Straight!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a Jack's High Straight!";
            } else {
                bestHand = "a " + highestHighCard + "'s High Straight!";
            }

            distributePot(playersWithBestHand);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Three of a Kind?------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int highCard = containsNOfAKind(p.getCards(), 3);
            if(highCard >= highestHighCard){
                if(highCard > highestHighCard){
                    highestHighCard =  highCard;
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "three Ace's!";
            } else if(highestHighCard == 13){ //King
                bestHand = "three Kings!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "three Queens!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "three Jacks!";
            } else {
                bestHand = "three " + highestHighCard + "'s!";
            }

            distributePot(playersWithBestHand);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Two Pair?-------------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsTwoPair(p.getCards());
            if(highCards[0] >= highestHighCard){
                if(highCards[0] > highestHighCard){
                    highestHighCard =  highCards[0];
                    highestHighCard2 = highCards[1];
                    playersWithBestHand = new ArrayList<Player>();
                } else if(highCards[1] > highestHighCard2){
                    highestHighCard2 = highCards[1];
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "a two pair, Ace's over ";
            } else if(highestHighCard == 13){ //King
                bestHand = "a two pair, Kings over ";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a two pair, Queens over ";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a two pair, Jacks over ";
            } else {
                bestHand = "a two pair, " + highestHighCard + "'s over ";
            }

            if(highestHighCard2 == 14){ //Ace
                bestHand += "Ace's.";
            } else if(highestHighCard2 == 13){ //King
                bestHand += "Kings.";
            } else if(highestHighCard2 == 12){ //Queen
                bestHand += "Queens.";
            } else if(highestHighCard2 == 11){ //Jack
                bestHand += "Jacks.";
            } else {
                bestHand += highestHighCard2 + "'s.";
            }

            distributePot(playersWithBestHand);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //One Pair?-------------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int highCard = containsNOfAKind(p.getCards(), 2);
            if(highCard >= highestHighCard){
                if(highCard > highestHighCard){
                    highestHighCard =  highCard;
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "two Ace's.";
            } else if(highestHighCard == 13){ //King
                bestHand = "two Kings.";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "two Queens.";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "two Jacks.";
            } else {
                bestHand = "two " + highestHighCard + "'s.";
            }

            distributePot(playersWithBestHand);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //High Card?------------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int highCard = determineHighCard(p.getCards());
            if(highCard >= highestHighCard){
                if(highCard > highestHighCard){
                    highestHighCard =  highCard;
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "an Ace High.";
            } else if(highestHighCard == 13){ //King
                bestHand = "a King's High.";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a Queen's High.";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a Jack's High.";
            } else {
                bestHand = "a " + highestHighCard + "'s High.";
            }

            distributePot(playersWithBestHand);
            return buildResultsString(playersWithBestHand, bestHand);
        }
        
        return "No Winner!!"; //This shoud be impossible
    }

    private int determineHighCard(Card[] hand){
        return this.determineHighCard(hand, "");
    }

    //Determines highest value within passed in array of cards
    private int determineHighCard(Card[] hand, String suit){
        int highCard = -1;
        for(Card card : hand){
            int val;
            if(card.getValue().equals("A")){
                val = 14;
            } else if(card.getValue().equals("K")){
                val = 13;
            } else if(card.getValue().equals("Q")){
                val = 12;
            } else if(card.getValue().equals("J")){
                val = 11;
            } else {
                val = Integer.parseInt(card.getValue());
            }

            if( val > highCard && (suit.equals(card.getSuit()) || suit.equals("")) ){
                highCard = val;
            }
        }

        return highCard;
    }

    //Determines if passed in array of cards contains a flush, returns suit if it does, empty string if not
    private String containsFlush(Card[] hand){
        //Clubs = 0, Diamonds = 1, Hearts = 2, Spades = 3
        int[] suits = {0,0,0,0};
        for(Card card : hand){
            if(card.getSuit().equals("C")){
                suits[0]++;
            } else if(card.getSuit().equals("D")){
                suits[1]++;
            } else if(card.getSuit().equals("H")){
                suits[2]++;
            } else if(card.getSuit().equals("S")){
                suits[3]++;
            }
        }

        if(suits[0] >= 5){
            return "C";
        } else if(suits[1] >= 5){
            return "D";
        } else if(suits[2] >= 5){
            return "H";
        } else if(suits[3] >= 5){
            return "S";
        }
        return "";
    }

    //Determines if passed in array of cards contains a Straight, returns high card value if it does, -1 if not
    private int containsStraight(Card[] hand){
        int[] values = new int[14];
        int numInArow = 0;
        int returnVal = -2;
        for(Card card : hand){
            if(card.getValue().equals("A")){
                values[0] = 1;
                values[13] = 1;
            } else if(card.getValue().equals("K")){
                values[12] = 1;
            } else if(card.getValue().equals("Q")){
                values[11] = 1;
            } else if(card.getValue().equals("J")){
                values[10] = 1;
            } else {
                values[Integer.parseInt(card.getValue()) - 1] = 1;
            }
        }

        for(int i = 0; i < 14; i++){
            if(values[i] == 1){
                numInArow++;
            } else {
                numInArow = 0;
            }

            if(numInArow >= 5){
                returnVal = i;
            }
        }
        return returnVal + 1; //To offest the array starting at 0, returning a 10 means high card is 10, not jack
    }

    //Determines if passed in array of cards contains a full house, 
    //returns array containing {three of a kind, two of a kind} if it does, {-1, -1} if not
    private int[] containsFullHouse(Card[] hand){
        int[] values = new int[15];
        int[] returnArr = {-1, -1};
        for(Card card : hand){
            if(card.getValue().equals("A")){
                values[14]++;
            } else if(card.getValue().equals("K")){
                values[13]++;
            } else if(card.getValue().equals("Q")){
                values[12]++;
            } else if(card.getValue().equals("J")){
                values[11]++;
            } else {
                values[Integer.parseInt(card.getValue())]++;
            }
        }

        for(int i = 2; i < 15; i++){
            if(values[i] == 3){
                returnArr[0] = i;
            }
        }

        for(int i = 2; i < 15; i++){
            if(values[i] >= 2 && i != returnArr[0]){
                returnArr[1] = i;
            }
        }

        if(returnArr[0] == -1 || returnArr[1] == -1){
            returnArr[0] = -1;
            returnArr[1] = -1;
        }
        return returnArr;
    }

    //Determines if passed in array of cards contains two pairs, 
    //returns array containing {larger pair, smaller pair} if it does, {-1, -1} if not
    private int[] containsTwoPair(Card[] hand){
        int[] values = new int[15];
        int[] returnArr = {-1, -1};
        for(Card card : hand){
            if(card.getValue().equals("A")){
                values[14]++;
            } else if(card.getValue().equals("K")){
                values[13]++;
            } else if(card.getValue().equals("Q")){
                values[12]++;
            } else if(card.getValue().equals("J")){
                values[11]++;
            } else {
                values[Integer.parseInt(card.getValue())]++;
            }
        }

        for(int i = 2; i < 15; i++){
            if(values[i] == 2){
                returnArr[0] = i;
            }
        }

        for(int i = 2; i < 15; i++){
            if(values[i] == 2 && i != returnArr[0]){
                returnArr[1] = i;
            }
        }

        if(returnArr[0] == -1 || returnArr[1] == -1){
            returnArr[0] = -1;
            returnArr[1] = -1;
        }
        return returnArr;
    }

    //Determines if passed in array of cards contains a exactly N of any card, 
    //returns the cards value if it does, -1 if not
    private int containsNOfAKind(Card[] hand, int N){
        int[] values = new int[15];
        int returnVal = -1;
        for(Card card : hand){
            if(card.getValue().equals("A")){
                values[14]++;
            } else if(card.getValue().equals("K")){
                values[13]++;
            } else if(card.getValue().equals("Q")){
                values[12]++;
            } else if(card.getValue().equals("J")){
                values[11]++;
            } else {
                values[Integer.parseInt(card.getValue())]++;
            }
        }

        for(int i = 2; i < 15; i++){
            if(values[i] == N){
                returnVal = i;
            }
        }

        return returnVal;
    }

    //Makes the display string describing the outcome of the game
    private String buildResultsString(ArrayList<Player> winners, String winningHand){
        StringBuilder resultStrBuilder = new StringBuilder();
        String deliminator = "";
        for(int i = 0; i < winners.size(); i++){
            resultStrBuilder.append(deliminator).append(winners.get(i).getName());
            
            deliminator = ", ";
            if(i == winners.size() - 2){
                if(winners.size() == 2){
                    deliminator = " and ";
                } else {
                    deliminator = ", and ";
                }
            }
        }

        if(winners.size() == 1){
            resultStrBuilder.append(" won with ").append(winningHand);
        } else if(winners.size() == 2){
            resultStrBuilder.append(" both won with ").append(winningHand);
        } else {
            resultStrBuilder.append(" all won with ").append(winningHand);
        }

        return resultStrBuilder.toString();
    }

    //Divide up the pot among the winner(s), and set it back to 0
    private void distributePot(ArrayList<Player> winners){
        int winningsAmount = pot/winners.size();
        for(Player p : players){
            if(winners.contains(p)){
                p.adjustCash(winningsAmount);
            }
        }

        pot = 0;
        return;
    }
    private void endHand(){} // TODO

    private void startHand() {} //TODO
    
}
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.*;
import java.awt.event.*;

public class Game extends JPanel {

    private final Color POKER_GREEN = new Color(71, 113, 72);
    private final Color WHITE = new Color (255, 255, 255);
    private Deck theDeck;
    private Card[] communityCards;
    private Player[] thePlayers;
    private int pot = 0;
        
    private Card cardBack;
    
    public Game() {
        initalizeStartGrid();
        setVisible(true);
    }
    
    
    public Game(File saveFile) {
      // initialize game settings from save file
    }
    
    
    
    private void initalizeStartGrid(){    
        //JPanels
        JPanel _startTitle = new JPanel();
        JPanel _playerName = new JPanel();
        JPanel _numOpponents = new JPanel();
        JPanel _startButton = new JPanel();
        
        JPanel _playerNamePanel = new JPanel();
        JPanel _numOpponentsPanel = new JPanel();
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
        
        JLabel _titleLabel = new JLabel();        
        JButton _startGame = new JButton();
        JLabel _playerNameLabel = new JLabel();
        JTextField _playerNameInput = new JTextField();
        JLabel _numOppLabel = new JLabel();
        JComboBox<Integer> _numOpp = new JComboBox<>();
        
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
            public void actionPerformed(ActionEvent evt) {
                //TODO REGEX user input
                String name = _playerNameInput.getText();
                int numOpponents = (int) _numOpp.getSelectedItem();
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
    
    private void createNewGame(String userName, int numOfComputerPlayers){
        //Variables
        theDeck = new Deck();
        communityCards = new Card[5];
        thePlayers = new Player[numOfComputerPlayers + 1];
        
        try {
            Image back = ImageIO.read(this.getClass().getResource("/back.png"));
            cardBack = new Card("Back", back);
        } catch (IOException ioex) {
            System.exit(1);
        }
        
        
        //Panels
        JPanel _top = new JPanel();
        JPanel _bot = new JPanel();
        JPanel[] _aiPlayers = new JPanel[numOfComputerPlayers];
        JPanel[] _tableAndUser = new JPanel[2];
        JPanel[][] _aiPlayersTB = new JPanel[numOfComputerPlayers][2];
        JPanel[][] _tableAndUserTB = new JPanel[2][2];
        
        String[]   aiPlayerNames   = getAINames(numOfComputerPlayers, userName);     
        
        setLayout(new GridLayout(2, 1));
        _top.setLayout(new GridLayout(1, numOfComputerPlayers, 50, 100));
        _bot.setLayout(new GridLayout(1, 2, 50, 100));
        
        setBackground(POKER_GREEN);
        _top.setBackground(POKER_GREEN);
        _bot.setBackground(POKER_GREEN);
        
        for(int i = 0; i<numOfComputerPlayers; i++){
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
        
        for(int i = 0; i<2; i++){
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
        
        
        //Make Labels and distribute starting cards
        
        //For the table section
        String tableLabel = "Pot: ";
        JLabel _tableLabel = new JLabel(tableLabel);
        _tableLabel.setForeground(WHITE);
        _tableLabel.setFont(new Font("Courier", Font.PLAIN, 28));
        _tableAndUserTB[0][0].add(_tableLabel, BorderLayout.NORTH);
        
        //display the pot value
        JLabel _pot = new JLabel("");
        _pot.setForeground(WHITE);
        _pot.setText("$" + String.valueOf(pot));
        _pot.setFont(new Font("Courier", Font.PLAIN, 28));
        _tableAndUserTB[0][0].add(_pot, BorderLayout.NORTH);
        
        for(int i=0; i<5; i++){
            communityCards[i] = theDeck.draw();
            displayCard(_tableAndUserTB[0][1], communityCards[i]);
        }
        
        //For the players section
        for(int i=0; i<thePlayers.length; i++){
            Card[] temp = new Card[5];
            temp[0] = theDeck.draw();
            temp[1] = theDeck.draw();
            //user
            if(i == 0){
                String yourLabel = userName + ": ";
                JLabel _yourLabel = new JLabel(yourLabel);
                _yourLabel.setForeground(WHITE);
                _yourLabel.setFont(new Font("Courier", Font.PLAIN, 28));
                _tableAndUserTB[1][0].add(_yourLabel, BorderLayout.NORTH);
                
                thePlayers[i] = new Player(userName, temp, 1000, true);
                
                //display users cash
                int yourCash = thePlayers[i].getCash();
                JLabel _yourCash = new JLabel("");
                _yourCash.setForeground(WHITE);
                _yourCash.setText("$" + String.valueOf(yourCash));
                _yourCash.setFont(new Font("Courier", Font.PLAIN, 28));
                _tableAndUserTB[1][0].add(_yourCash, BorderLayout.NORTH);
                
                displayCard(_tableAndUserTB[1][1], temp[0]);
                displayCard(_tableAndUserTB[1][1], temp[1]);
            } 
            //AI
            else {
                String aiName   = aiPlayerNames[i - 1];
                JLabel _aiLabel = new JLabel(aiName + ": ");
                
                _aiLabel.setForeground(WHITE);
                _aiLabel.setFont(new Font("Courier", Font.PLAIN, 28));
                _aiPlayersTB[i-1][0].add(_aiLabel, BorderLayout.NORTH);
                
                thePlayers[i] = new Player(aiName, temp, 1000, false);
                
                //display AI cash
                int theirCash = thePlayers[i].getCash();
                JLabel _theirCash = new JLabel("");
                _theirCash.setForeground(WHITE);
                _theirCash.setText("$" + String.valueOf(theirCash));
                _theirCash.setFont(new Font("Courier", Font.PLAIN, 28));
                _aiPlayersTB[i-1][0].add(_theirCash, BorderLayout.NORTH);
                
                displayCard(_aiPlayersTB[i-1][1], cardBack);
                displayCard(_aiPlayersTB[i-1][1], cardBack);
            }
        }
        
        //Display
        add(_top);
        add(_bot);
        revalidate();
        repaint();
        
    }
    
    private void displayCard(JPanel loc, Card card){
        Image  resizedCard = card.getFace().getScaledInstance(75, 105,  java.awt.Image.SCALE_SMOOTH);
        JLabel cardDisplay = new JLabel(new ImageIcon(resizedCard));
        loc.add(cardDisplay, BorderLayout.SOUTH);
    }

    private String[] getAINames(int num, String playerName) {
        String[] namesList  = {"Daniel", "Erik", "Antonio", "Fedor","Phil", "Johnathan", "Scott", "Elton", "Brian"};
        String[] returnList = new String[num];
        int      listLength = 0;
        int      index      = 0;
        
        while(listLength < num) {
            if(!namesList[index].equals(playerName)) {
                returnList[listLength] = namesList[index];
                listLength++;
            }
            index++;
        }

        return returnList;
    }

    private String determineBestHand(){
        //Determine which players to consider
        ArrayList<Player> remainingPlayers = new ArrayList<Player>();
        ArrayList<Player> playersWithBestHand = new ArrayList<Player>();
        int highestHighCard = 0;
        String suit;

        for(Player p : thePlayers){
            if(p.isPlayingHand()){
                remainingPlayers.add(p);
            }
        }

        //Check if only one player remains
        if(remainingPlayers.size() == 1){
            distributePot(remainingPlayers);
            return remainingPlayers.get(0).getName() + " has won as the last man Standing!";
        }

        for(Player p : remainingPlayers){
            ArrayList<Card> allAvailableCards = new ArrayList<Card>();
            allAvailableCards.addAll(Arrays.asList(communityCards));
            allAvailableCards.addAll(Arrays.asList(p.getCards()));
            p.setCards((Card[])allAvailableCards.toArray());
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
            distributePot(playersWithBestHand);

            StringBuilder resultStrBuilder = new StringBuilder();
            String deliminator = "";
            for(int i = 0; i < playersWithBestHand.size(); i++){
                resultStrBuilder.append(deliminator)
                                .append(playersWithBestHand.get(i).getName());
                
                deliminator = ", ";
                if(i == playersWithBestHand.size() - 2){
                    if(playersWithBestHand.size() == 2){
                        deliminator = " and ";
                    } else {
                        deliminator = ", and ";
                    }
                }
            }

            if(playersWithBestHand.size() == 1){
                resultStrBuilder.append(" won with ");
            } else if(playersWithBestHand.size() == 2){
                resultStrBuilder.append(" both won with ");
            } else {
                resultStrBuilder.append(" all won with ");
            }

            if(highestHighCard == 14){ //Ace
                resultStrBuilder.append("a Royal Flush!!!");
            } else if(highestHighCard == 13){ //King
                resultStrBuilder.append("a King High Straight Flush!!");
            } else if(highestHighCard == 12){ //Queen
                resultStrBuilder.append("a Queen High Straight Flush!!");
            } else if(highestHighCard == 11){ //Jack
                resultStrBuilder.append("a Jack High Straight Flush!!");
            } else {
                resultStrBuilder.append("a " + highestHighCard + " High Straight Flush!!");
            }

            return resultStrBuilder.toString();
        }

        //Four of a kind?-------------------------------------------------------------------------------

        //Full House?-----------------------------------------------------------------------------------

        //Flush?----------------------------------------------------------------------------------------
        highestHighCard = 0;
        suit = "";
        for(Player p : remainingPlayers){
            suit = containsFlush(p.getCards());
            int highCard = determineHighCard(p.getCards());
            if(highCard > highestHighCard){
                highestHighCard =  highCard;
                playersWithBestHand = new ArrayList<Player>();
            }

            playersWithBestHand.add(p);
        }

        if(playersWithBestHand.size() > 0){
            distributePot(playersWithBestHand);

            StringBuilder resultStrBuilder = new StringBuilder();
            String deliminator = "";
            for(int i = 0; i < playersWithBestHand.size(); i++){
                resultStrBuilder.append(deliminator)
                                .append(playersWithBestHand.get(i).getName());
                
                deliminator = ", ";
                if(i == playersWithBestHand.size() - 2){
                    if(playersWithBestHand.size() == 2){
                        deliminator = " and ";
                    } else {
                        deliminator = ", and ";
                    }
                }
            }

            if(playersWithBestHand.size() == 1){
                resultStrBuilder.append(" won with ");
            } else if(playersWithBestHand.size() == 2){
                resultStrBuilder.append(" both won with ");
            } else {
                resultStrBuilder.append(" all won with ");
            }

            if(highestHighCard == 14){ //Ace
                resultStrBuilder.append("an Ace High Flush!");
            } else if(highestHighCard == 13){ //King
                resultStrBuilder.append("a King High Flush!!");
            } else if(highestHighCard == 12){ //Queen
                resultStrBuilder.append("a Queen High Flush!!");
            } else if(highestHighCard == 11){ //Jack
                resultStrBuilder.append("a Jack High Flush!!");
            } else {
                resultStrBuilder.append("a " + highestHighCard + " High Flush!!");
            }

            return resultStrBuilder.toString();
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
            distributePot(playersWithBestHand);

            StringBuilder resultStrBuilder = new StringBuilder();
            String deliminator = "";
            for(int i = 0; i < playersWithBestHand.size(); i++){
                resultStrBuilder.append(deliminator)
                                .append(playersWithBestHand.get(i).getName());
                
                deliminator = ", ";
                if(i == playersWithBestHand.size() - 2){
                    if(playersWithBestHand.size() == 2){
                        deliminator = " and ";
                    } else {
                        deliminator = ", and ";
                    }
                }
            }

            if(playersWithBestHand.size() == 1){
                resultStrBuilder.append(" won with ");
            } else if(playersWithBestHand.size() == 2){
                resultStrBuilder.append(" both won with ");
            } else {
                resultStrBuilder.append(" all won with ");
            }

            if(highestHighCard == 14){ //Ace
                resultStrBuilder.append("an Ace High Straight!!");
            } else if(highestHighCard == 13){ //King
                resultStrBuilder.append("a King High Straight!!");
            } else if(highestHighCard == 12){ //Queen
                resultStrBuilder.append("a Queen High Straight!!");
            } else if(highestHighCard == 11){ //Jack
                resultStrBuilder.append("a Jack High Straight!!");
            } else {
                resultStrBuilder.append("a " + highestHighCard + " High Straight!!");
            }

            return resultStrBuilder.toString();
        }

        //Three of a Kind?------------------------------------------------------------------------------

        //Two Pair?-------------------------------------------------------------------------------------
        
        //One Pair?-------------------------------------------------------------------------------------

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
            distributePot(playersWithBestHand);

            StringBuilder resultStrBuilder = new StringBuilder();
            String deliminator = "";
            for(int i = 0; i < playersWithBestHand.size(); i++){
                resultStrBuilder.append(deliminator)
                                .append(playersWithBestHand.get(i).getName());
                
                deliminator = ", ";
                if(i == playersWithBestHand.size() - 2){
                    if(playersWithBestHand.size() == 2){
                        deliminator = " and ";
                    } else {
                        deliminator = ", and ";
                    }
                }
            }

            if(playersWithBestHand.size() == 1){
                resultStrBuilder.append(" won with ");
            } else if(playersWithBestHand.size() == 2){
                resultStrBuilder.append(" both won with ");
            } else {
                resultStrBuilder.append(" all won with ");
            }

            if(highestHighCard == 14){ //Ace
                resultStrBuilder.append("an Ace High.");
            } else if(highestHighCard == 13){ //King
                resultStrBuilder.append("a King High.");
            } else if(highestHighCard == 12){ //Queen
                resultStrBuilder.append("a Queen High.");
            } else if(highestHighCard == 11){ //Jack
                resultStrBuilder.append("a Jack High.");
            } else {
                resultStrBuilder.append("a " + highestHighCard + " High.");
            }

            return resultStrBuilder.toString();
        }
        
        return "No Winner!!"; //This shoud be impossible
    }

    private boolean containsCard(Card[] hand, String cardValue, String cardSuit){
        for(Card card : hand){
            if(card.getValue().equals(cardValue) && card.getSuit().equals(cardSuit)){
                return true;
            }
        }
        return false;
    }

    private int determineHighCard(Card[] hand){
        int highCard = 0;
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

            if(val > highCard){
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

    private void distributePot(ArrayList<Player> winners){
        int winningsAmount = pot/winners.size();
        for(Player p : thePlayers){
            if(winners.contains(p)){
                p.adjustCash(winningsAmount);
            }
        }

        pot = 0;
        return;
    }
    
}
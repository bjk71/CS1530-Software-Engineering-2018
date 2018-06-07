import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
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
	
}
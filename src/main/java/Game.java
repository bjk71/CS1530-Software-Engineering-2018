import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

public class Game extends JPanel {

    private final Color pokerGreen = new Color(71, 113, 72);
	private Deck theDeck;
	private Card[] communityCards;
	private Player[] thePlayers;
	
	Card cardBack;

    public Game() {
		//Passed in
		String userName = "DJ";
		int numOfComputerPlayers = 4;
		
		
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
		
		setLayout(new GridLayout(2, 1));
		_top.setLayout(new GridLayout(1, numOfComputerPlayers, 50, 100));
		_bot.setLayout(new GridLayout(1, 2, 50, 100));
		
		setBackground(pokerGreen);
		_top.setBackground(pokerGreen);
		_bot.setBackground(pokerGreen);
		
		for(int i = 0; i<numOfComputerPlayers; i++){
			_aiPlayers[i] = new JPanel();
			_top.add(_aiPlayers[i]);
			_aiPlayers[i].setBackground(pokerGreen);
			_aiPlayers[i].setLayout(new GridLayout(2, 1, 50, 50));
			
			_aiPlayersTB[i][0] = new JPanel();
			_aiPlayersTB[i][1] = new JPanel();
			_aiPlayers[i].add(_aiPlayersTB[i][0]);
			_aiPlayers[i].add(_aiPlayersTB[i][1]);
			_aiPlayersTB[i][0].setBackground(pokerGreen);
			_aiPlayersTB[i][1].setBackground(pokerGreen);
		}
		
		for(int i = 0; i<2; i++){
			_tableAndUser[i] = new JPanel();
			_bot.add(_tableAndUser[i]);
			_tableAndUser[i].setBackground(pokerGreen);
			_tableAndUser[i].setLayout(new GridLayout(2, 1, 50, 50));
			
			_tableAndUserTB[i][0] = new JPanel();
			_tableAndUserTB[i][1] = new JPanel();
			_tableAndUser[i].add(_tableAndUserTB[i][0]);
			_tableAndUser[i].add(_tableAndUserTB[i][1]);
			_tableAndUserTB[i][0].setBackground(pokerGreen);
			_tableAndUserTB[i][1].setBackground(pokerGreen);
		}
        
		
        //Make Labels and distribute starting cards
		String tableLabel = "The Table: ";
		JLabel _tableLabel = new JLabel(tableLabel);
		_tableLabel.setFont(new Font("Courier", Font.PLAIN, 28));
		_tableAndUserTB[0][0].add(_tableLabel, BorderLayout.NORTH);
		for(int i=0; i<5; i++){
			communityCards[i] = theDeck.draw();
			displayCard(_tableAndUserTB[0][1], communityCards[i]);
		}
		
		for(int i=0; i<thePlayers.length; i++){
			Card[] temp = new Card[5];
			temp[0] = theDeck.draw();
			temp[1] = theDeck.draw();
			if(i == 0){
				String yourLabel = userName + ": ";
				JLabel _yourLabel = new JLabel(yourLabel);
				_yourLabel.setFont(new Font("Courier", Font.PLAIN, 28));
				_tableAndUserTB[1][0].add(_yourLabel, BorderLayout.NORTH);
				
				thePlayers[i] = new Player(userName, temp, 1000, true);
				displayCard(_tableAndUserTB[1][1], temp[0]);
				displayCard(_tableAndUserTB[1][1], temp[1]);
			} else {
				String theirLabel = "aiName: ";
				JLabel _theirLabel = new JLabel(theirLabel);
				_theirLabel.setFont(new Font("Courier", Font.PLAIN, 28));
				_aiPlayersTB[i-1][0].add(_theirLabel, BorderLayout.NORTH);
				
				thePlayers[i] = new Player("aiName", temp, 1000, false);
				displayCard(_aiPlayersTB[i-1][1], cardBack);
				displayCard(_aiPlayersTB[i-1][1], cardBack);
			}
		}
		
		
		//Display
		add(_top);
		add(_bot);
		setVisible(true);
    }

    public Game(File saveFile) {
        // initialize game settings from save file
    }
	
	private void displayCard(JPanel loc, Card card){
		Image resizedCard = card.getFace().getScaledInstance(75, 105,  java.awt.Image.SCALE_SMOOTH);
		JLabel cardDisplay = new JLabel(new ImageIcon(resizedCard));
		loc.add(cardDisplay, BorderLayout.SOUTH);
	}
}
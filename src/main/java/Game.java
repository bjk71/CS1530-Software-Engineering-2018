import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

public class Game extends JPanel {

	private final Color pokerGreen = new Color(71, 113, 72);
	
	private Deck     theDeck;
	private Card[]   communityCards;
	private Player[] thePlayers;
	private int pot = 0;
	
	//Passed in
	private String userName = "DJ";
	private int numOfComputerPlayers = 4;
		
	Card cardBack;

    public Game() {
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
		JPanel     _top            = new JPanel();
		JPanel     _bot            = new JPanel();
		JPanel[]   _aiPlayers      = new JPanel[numOfComputerPlayers];
		JPanel[]   _tableAndUser   = new JPanel[2];
		JPanel[][] _aiPlayersTB    = new JPanel[numOfComputerPlayers][2];
        JPanel[][] _tableAndUserTB = new JPanel[2][2];
        
        String[]   aiPlayerNames   = getAINames(numOfComputerPlayers, userName);
		
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
		
		//For the table section
		String tableLabel = "The Table: ";
		JLabel _tableLabel = new JLabel(tableLabel);
		_tableLabel.setFont(new Font("Courier", Font.PLAIN, 28));
		_tableAndUserTB[0][0].add(_tableLabel, BorderLayout.NORTH);
		
		//display the pot value
		JLabel _pot = new JLabel("");
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
            
			if(i == 0) { // Human player
				String yourLabel  = userName + ": ";
                JLabel _yourLabel = new JLabel(yourLabel);

                thePlayers[i] = new Player(userName, temp, 1000, true);

                int    yourCash = thePlayers[i].getCash();
				JLabel _yourCash = new JLabel("");
                
				_yourLabel.setFont(new Font("Courier", Font.PLAIN, 28));
				_tableAndUserTB[1][0].add(_yourLabel, BorderLayout.NORTH);
				
				
				
				//display users cash
				_yourCash.setText("$" + String.valueOf(yourCash));
				_yourCash.setFont(new Font("Courier", Font.PLAIN, 28));
				_tableAndUserTB[1][0].add(_yourCash, BorderLayout.NORTH);
				
				displayCard(_tableAndUserTB[1][1], temp[0]);
				displayCard(_tableAndUserTB[1][1], temp[1]);
            } else { // AI players
                String aiName   = aiPlayerNames[i - 1];
                JLabel _aiLabel = new JLabel(aiName + ": ");
                
				_aiLabel.setFont(new Font("Courier", Font.PLAIN, 28));
				_aiPlayersTB[i-1][0].add(_aiLabel, BorderLayout.NORTH);
				
				thePlayers[i] = new Player(aiName, temp, 1000, false);
				
				//display AI cash
				int theirCash = thePlayers[i].getCash();
				JLabel _theirCash = new JLabel("");
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
		setVisible(true);
    }

    public Game(File saveFile) {
      // initialize game settings from save file
    }
	
	private void displayCard(JPanel loc, Card card){
		Image  resizedCard = card.getFace().getScaledInstance(75, 105,  java.awt.Image.SCALE_SMOOTH);
		JLabel cardDisplay = new JLabel(new ImageIcon(resizedCard));
		loc.add(cardDisplay, BorderLayout.SOUTH);
	}

	private String[] getAINames(int num, String playerName) {
        String[] namesList  = {"Daniel", "Erik", "Antonio", "Fedor","Phil", "Johnathan", "Scott", "Phil", "Brian"};
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
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

public class Game extends JPanel {

    private final Color pokerGreen = new Color(71, 113, 72);

    public Game() {
		int numOfPlayers = 7;
		
		Deck theDeck = new Deck();
		Card[] communityCards = new Card[5];
		ArrayList<Card[]> playercards = new ArrayList<Card[]>();
		String cards = "Community Cards: ";
		String yourCards = "Your Cards: ";
		for(int i=0; i<5; i++){
			communityCards[i] = theDeck.draw();
			cards += (communityCards[i].getName() + ", ");
		}
		
		for(int i=0; i<numOfPlayers; i++){
			Card[] temp = new Card[5];
			temp[0] = theDeck.draw();
			temp[1] = theDeck.draw();
			playercards.add(temp);
			if(i == 0){
				yourCards += (temp[0].getName() + ", " + temp[1].getName());
			}
		}
		
		JLabel _tableCards = new JLabel(cards);
		JLabel _yourCards = new JLabel(yourCards);
		
        _tableCards.setBackground(pokerGreen);
		_tableCards.setFont(new Font("Courier", Font.PLAIN, 48));
		_yourCards.setFont(new Font("Courier", Font.PLAIN, 48));

        setBackground(pokerGreen);
        setVisible(true);

        add(_tableCards);
		add(_yourCards);
    }

    public Game(File saveFile) {
        // initialize game settings from save file
    }
}
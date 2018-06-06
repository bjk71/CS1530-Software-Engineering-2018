import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Game extends JPanel {

    private final Color pokerGreen = new Color(71, 113, 72);

    public Game() {	
		Deck theDeck = new Deck();
		Card[] communityCards = new Card[5];
		String cards = "";
		for(int i=0; i<5; i++){
			communityCards[i] = theDeck.draw();
			cards += (communityCards[i].getName() + ", ");
		}
		JLabel _sampleLabel = new JLabel(cards);
		
        _sampleLabel.setBackground(pokerGreen);

        setBackground(pokerGreen);
        setVisible(true);

        add(_sampleLabel);
    }

    public Game(File saveFile) {
        // initialize game settings from save file
    }
}
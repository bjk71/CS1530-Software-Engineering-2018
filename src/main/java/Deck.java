import java.awt.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

public class Deck{
	private ArrayList<Card> cards;
	
	public Deck(){
		this.cards = new ArrayList<Card>();
		ArrayList<String> cardNames = new ArrayList<String>();
		String[] cs = {"AC", "2C", "3C", "4C", "5C", "6C", "7C", "8C", "9C", "10C", "JC", "QC", "KC"};
		String[] ds = {"AD", "2D", "3D", "4D", "5D", "6D", "7D", "8D", "9D", "10D", "JD", "QD", "KD"};
		String[] hs = {"AH", "2H", "3H", "4H", "5H", "6H", "7H", "8H", "9H", "10H", "JH", "QH", "KH"};
		String[] ss = {"AS", "2S", "3S", "4S", "5S", "6S", "7S", "8S", "9S", "10S", "JS", "QS", "KS"};
		cardNames.addAll(Arrays.asList(cs));
		cardNames.addAll(Arrays.asList(ds));
		cardNames.addAll(Arrays.asList(hs));
		cardNames.addAll(Arrays.asList(ss));
		
		for(String cardName : cardNames){
			try {
				Image temp = ImageIO.read(this.getClass().getResource("/" + cardName + ".png"));
				cards.add(new Card(cardName, temp));
			} catch (IOException ioex) {
				System.exit(1);
			}
		}
		
		this.shuffle();
	}
	
	public void shuffle(){
		Collections.shuffle(this.cards);
	}
	
	public Card draw(){
		return this.cards.remove(cards.size()-1);
	}
		
}
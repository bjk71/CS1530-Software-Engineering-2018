import java.io.*;
import java.util.*;

public class Deck{
	private ArrayList<String> cards;
	
	public Deck(){
		this.cards = new ArrayList<String>();
		String[] cs = {"AC", "2C", "3C", "4C", "5C", "6C", "7C", "8C", "9C", "10C", "JC", "QC", "KC"};
		String[] ds = {"AD", "2D", "3D", "4D", "5D", "6D", "7D", "8D", "9D", "10D", "JD", "QD", "KD"};
		String[] hs = {"AH", "2H", "3H", "4H", "5H", "6H", "7H", "8H", "9H", "10H", "JH", "QH", "KH"};
		String[] ss = {"AS", "2S", "3S", "4S", "5S", "6S", "7S", "8S", "9S", "10S", "JS", "QS", "KS"};
		this.cards.addAll(Arrays.asList(cs));
		this.cards.addAll(Arrays.asList(ds));
		this.cards.addAll(Arrays.asList(hs));
		this.cards.addAll(Arrays.asList(ss));
		this.shuffle();
	}
	
	public void shuffle(){
		Collections.shuffle(this.cards);
	}
	
	public String draw(){
		return this.cards.remove(0);
	}
		
}
import java.io.*;
import java.util.*;

public class Player{
	private String name;
	private Card[] cards;
	private int cash;
	private boolean isUser;
	private boolean inHand;
	
	public Player(String name, Card[] cards, int cash, boolean isUser){
		this.name = name;
		this.cards = cards;
		this.cash = cash;
		this.isUser = isUser;
		this.inHand = true;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Card[] getCards(){
		return this.cards;
	}
	
	public int getCash(){
		return this.cash;
	}

	public boolean isPlayingHand(){
		return this.inHand;
	}

	public void setPlayingHand(boolean inHand){
		this.inHand = inHand;
	}
		
}
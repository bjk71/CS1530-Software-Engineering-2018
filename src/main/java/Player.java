import javax.swing.*;
import java.io.Serializable;

public class Player implements Serializable {
	private final int MAXIMUM_SIDEPOTS = 8;

	private String  name;
	private Card[]  cards;
	private int 	role;
	private int     cash;
	private PlayerPanel playerPanel;
	private int     index;
	private boolean inHand;
	private boolean[] activePot;

	private Card[]  fullHand; //For determining best hand, stores 2 card hand and community cards

	private JPanel  _cardLoc;
	
	public Player(String name, Card[] cards, int role, int cash, int index){
		this.name   = name;
		this.cards  = cards;
		this.role	= role;
		this.cash   = cash;
		this.index  = index;
		this.inHand = true;
		this.activePot = new boolean[MAXIMUM_SIDEPOTS];
	}

	/**
     * Reinitialize Card images after loading from save file
     */
	public void reinitImages() {
		this.playerPanel.reinitImages();
	}
	
	public String getName(){
		return this.name;
	}
	
	public Card[] getCards(){
		return this.cards;
	}
	
	public int getIndex() {
		return this.index;
	}

	public void setCards(Card[] newHand, boolean updateDisplay){
		this.cards = newHand;

		if(updateDisplay){
			this.playerPanel.setPlayerCards(newHand);
			this.playerPanel.showCards(false);
		}
	}

	public void setFullHand(Card[] hand){
		this.fullHand = hand;
	}

	public Card[] getFullHand(){
		return this.fullHand;
	}

	public void setActivePot(int num, boolean b){
		this.activePot[num] = b;
	}

	public boolean getActivePot(int num){
		return this.activePot[num];
	}

	public void setPlayerPanel(PlayerPanel panel) {
		this.playerPanel = panel;
	}

	public PlayerPanel getPlayerPanel() {
		return this.playerPanel;
	}
	
	public int getRole() {
		return this.role;
	}

	public void setRole(int role) {
		this.role = role;
	}
	
	public int getCash(){
		return this.cash;
	}

	public void adjustCash(int amount, boolean updateDisplay){
		this.cash += amount;
		if(this.cash < 0){
			this.inHand = false;
		}

		if(updateDisplay){
			setLabel();
		}
		
	}

	public boolean isPlayingHand() {
		return this.inHand;
	}

	public void setPlayingHand(boolean inHand) {
		this.inHand = inHand;
	}

	private void setLabel() {
		if(this.cash < 0) {
			playerPanel.setPlayerCash(0);
			this.inHand = false;
		} else {
			playerPanel.setPlayerCash(cash);
		}
	}

	public void setCardPanel(JPanel _cardLoc) {
		this._cardLoc = _cardLoc;
	}

	public JPanel getCardPanel() {
		return this._cardLoc;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Name: " + name);
		sb.append(", Cards: " + cards[0].getName() + " " + cards[1].getName());
		sb.append(", Cash: " + cash);
		sb.append(", Role: " + role);

		return sb.toString();
	}
		
}
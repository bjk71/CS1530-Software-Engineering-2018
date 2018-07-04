import javax.swing.*;

public class Player{
	private String  name;
	private Card[]  cards;
	private int 	role;
	private int     cash;
	private JLabel  _cash;
	private boolean isUser;
	private boolean inHand;

	private JPanel  _cardLoc;
	
	public Player(String name, Card[] cards, int role, int cash, JLabel _cash, boolean isUser){
		this.name   = name;
		this.cards  = cards;
		this.role	= role;
		this.cash   = cash;
		this._cash  = _cash;
		this.isUser = isUser;
		this.inHand = true;
	}
	
	public String getName(){
		return this.name;
	}
	
	public Card[] getCards(){
		return this.cards;
	}

	public void setCards(Card[] newHand){
		this.cards = newHand;
	}
	
	public int getRole(){
		return this.role;
	}
	
	public int getCash(){
		return this.cash;
	}

	public void adjustCash(int amount){
		this.cash += amount;
		if(this.cash < 0){
			this.inHand = false;
		}

		setLabel();
	}

	public boolean isPlayingHand(){
		return this.inHand;
	}

	public void setPlayingHand(boolean inHand){
		this.inHand = inHand;
	}

	private void setLabel()
	{
		this._cash.setText("$" + Integer.toString(this.cash));
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

		return sb.toString();
	}
		
}
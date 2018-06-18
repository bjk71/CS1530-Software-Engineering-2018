
public class Player{
	private String  name;
	private Card[]  cards;
	private int     cash;
	private boolean isUser;
	private boolean active;
	
	public Player(String name, Card[] cards, int cash, boolean isUser){
		this.name   = name;
		this.cards  = cards;
		this.cash   = cash;
		this.isUser = isUser;
		this.active = true;
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

	public void adjustCash(int amount){
		this.cash += amount;
		if(this.cash < 0){
			this.active = false;
		}
	}

	public void setActive(boolean a){
		this.active = a;
	}

	public boolean isActive(){
		return this.active;
	}
		
}
import javax.swing.*;

public class Pot{
	private int       value;
    private int 	  currBet;
    private Player[]  validPlayers;
    private boolean[] haveBetPot;
    private boolean   sidePot;
	private JLabel    _pot;
    private JPanel    _potJPanel;
    
    /**
     * Initialize pot
     */
    public Pot(){
        this(0, 0, null, false);
    }

    /**
     * Initialize pot with starting value
     * @param value Dollar value to initialize pot to.
     */
    public Pot(int value){
        this(value, 0, null, false);
    }


	/**
     * Initialize pot with players who can win it
     * @param value         Dollar value to initialize pot to.
     * @param currBet       Current minimum bet action of the pot
     * @param validPlayers  Player array representing the players that can win the pot
     * @param sidePot       Boolean representing if this pot is a side pot
     */
	public Pot(int value, int currBet, Player[] validPlayers, boolean sidePot){
        this.value        = value;
        this.currBet      = currBet;
        this.validPlayers = validPlayers;
        this.sidePot      = sidePot;
        this.haveBetPot   = new boolean[validPlayers.length];
	}
	
	public int getValue(){
		return this.value;
	}
	
	public int getCurrentBet(){
		return this.currBet;
    }
    
    public boolean getHaveBetPot(int i){
		return this.haveBetPot[i];
	}
	
	public Player[] getValidPlayers(){
		return this.validPlayers;
	}
	
	public boolean isSidePot(){
		return this.sidePot;
    }
    
    public void adjustCurrentBet(int bet){
        this.currBet = bet;
    }

    public void removePlayer(Player p){
        
    }

    public void setSidePot(boolean sidePot){
        this.sidePot = sidePot;
    }

    public void setHaveBetPot(int i, boolean b){
        this.haveBetPot[i] = b;
    }

	public void adjustPot(int bet){
		this.value += bet;
		if(bet > this.currBet){
			this.currBet = bet;
		}

		//setLabel();
	}

	public void setLabel(int value) {
		this._pot.setText("$" + Integer.toString(value));
	}

    public JLabel getLabel() {
		return this._pot;
	}
	public void setPotPanel(JPanel _potJPanel) {
		this._potJPanel = _potJPanel;
	}

	public JPanel getPotPanel() {
		return this._potJPanel;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
        String prefix = "";
        if ( isSidePot() ){
            sb.append("Side Pot: " + value);
        } else {
            sb.append("Main Pot: " + value);
        }
        sb.append(", Players in Pot: ");
            for (Player p : validPlayers){
                sb.append(prefix);
                prefix=", ";
                sb.append(p.getName());
            }
		return sb.toString();
	}
		
}
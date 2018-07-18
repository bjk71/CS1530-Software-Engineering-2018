import javax.swing.*;

public class Pot{
	private int       value;
    private int 	  currBet;
    private int       setBet;
    private int       numOfPlayers;
    private int[]     pots;
    private int[]     playerBets;
    private Player[]  validPlayers;
    private boolean[] haveBetPot;
    private boolean   sidePot;
	private JLabel    _pot;
    private JPanel    _potJPanel;
    private PotPanel  _potPanel;

    
    public Pot(PotPanel _potPanel, int numOfPlayers) {
        this._potPanel = _potPanel;
        this.numOfPlayers = numOfPlayers;

        this.value = 0;
        this.currBet = -1;
        this.playerBets   = new int[numOfPlayers];
    }
	
	public int getValue() {
		return this.value;
	}
	
	public int getCurrentBet() {
		return this.currBet;
    }
    
    public boolean getHaveBetPot(int i) {
		return this.haveBetPot[i];
	}
	
	public Player[] getValidPlayers() {
		return this.validPlayers;
	}
	
	public boolean isSidePot() {
		return this.sidePot;
    }
    
    public void adjustCurrentBet(int bet) {
        this.currBet = bet;
    }

    /**
     * Return how much money player at playerIndex has bet this round.
     */
    public int getPlayerBet(int playerIndex) {
        return this.playerBets[playerIndex];
    }

    public Action bet(Player player, int amount) {
        int playerIndex = player.getIndex();
        
        if(amount < 0) {
            // player folded
            player.setPlayingHand(false);
            return new Action(0);
        } else if(amount == player.getCash()) {
            // start checking for sidepots
        }

        // remove cash from player
        player.adjustCash(-amount, true);
        // add cash to player's bet
        this.playerBets[playerIndex] += amount;

        System.out.printf("Player %d bet %d, for a total of %d; currBet = %d\n", playerIndex, amount, playerBets[playerIndex], currBet);
        // System.out.println("Update currBet: " + (this.playerBets[playerIndex] > this.currBet));

        if(this.playerBets[playerIndex] > this.currBet) {
            this.setBet  = playerIndex;
            this.currBet = this.playerBets[playerIndex];
            System.out.printf(">> Updated setBet to index: %d, value: %d\n", playerIndex, this.currBet);
        }
        
        this.adjustPot(amount, 0);

        return new Action(this.playerBets[playerIndex]);
    }

    /**
     * Start new round by reseting current bet and playerBets, and updating setBet variable.
     */
    public void newRound(int dealerIndex) {
        this.currBet = -1;
        this.setBet = dealerIndex;
        System.out.printf("New Hand: Updated setBet to index: %d, value: %d\n", dealerIndex, -1);

        for(int i = 0; i < this.playerBets.length; i++) {
            this.playerBets[i] = 0;
        }
    }

    public int getSetBet() {
        return setBet;
    }

    public void setSidePot(boolean sidePot) {
        this.sidePot = sidePot;
    }

    public void setHaveBetPot(int i, boolean b) {
        this.haveBetPot[i] = b;
    }

	public void adjustPot(int bet, int num) {
		this.value += bet;
        this._potPanel.adjustPot(num, bet);
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
        if ( isSidePot() ) {
            sb.append("Side Pot: " + value);
        } else {
            sb.append("Main Pot: " + value);
        }
        sb.append(", Players in Pot: ");

            for (Player p : validPlayers) {
                sb.append(prefix);
                prefix=", ";
                sb.append(p.getName());
            }
		return sb.toString();
	}
		
}
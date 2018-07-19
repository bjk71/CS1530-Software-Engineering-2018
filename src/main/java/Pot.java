import java.util.stream.IntStream;
import java.io.Serializable;

import javax.swing.*;

public class Pot implements Serializable {
	private int       value;
    private int 	  currBet;
    private int       setBet;
    private int       numOfPlayers;
    private int       activePot;
    private int       numOfSidePots;
    private int[]     pots;
    private int[]     playerBets;
    private int[]     betsThisHand;
    private int[]     inPot;
    private boolean[] causedSidepot;
    private boolean[] inHand;
    private boolean[] haveBetPot;
    private PotPanel  _potPanel;

    
    public Pot(PotPanel _potPanel, int numOfPlayers) {
        this._potPanel     = _potPanel;
        this.numOfPlayers  = numOfPlayers;

        this.value         = 0;
        this.currBet       = -1;
        this.activePot     = 0;
        this.numOfSidePots = 0;
        this.playerBets    = new int[numOfPlayers];
        this.betsThisHand  = new int[numOfPlayers];
        this.causedSidepot = new boolean[numOfPlayers];
        this.pots          = new int[numOfPlayers];
        this.inPot         = new int[numOfPlayers];
        this.inHand        = new boolean[numOfPlayers];
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
    
    public void adjustCurrentBet(int bet) {
        this.currBet = bet;
    }

    /**
     * Return how much money player at <b>playerIndex</b> has bet this round.
     * @param  playerIndex Index of player to return bet amount of.
     * @return             Amount bet by player this round.
     */
    public int getPlayerBet(int playerIndex) {
        return this.playerBets[playerIndex];
    }

    /**
     * Handle bet for <b>player</b> of size <b>amount</b>.
     * @param  player Player making bet.
     * @param  amount Bet size.
     * @return        Action represting the amount the player bet.
     */
    public Action bet(Player player, int amount) {
        int playerIndex = player.getIndex();
        
        if(amount < 0) { // player folded
            player.setPlayingHand(false);
            inHand[playerIndex] = false;
            return new Action(0);
        } else {
            inHand[playerIndex] = true;
        }
        
        // remove cash from player
        player.adjustCash(-amount, true);

        // add cash to player's bet
        this.playerBets[playerIndex]   += amount;
        this.betsThisHand[playerIndex] += amount;

        // check if player is all in
        if(this.playerBets[playerIndex] < currBet || player.getCash() == 0) {
            // start checking for sidepots
            this.createSidepot(player);
        }

        // handle sidepots
        if(numOfSidePots > 0) {
            this.handleSidePots();
        } else {
            this.adjustPot(amount, activePot);
        }

        // System.out.printf("Player %d bet %d, for a total of %d; currBet = %d\n", playerIndex, amount, playerBets[playerIndex], currBet);

        if(this.playerBets[playerIndex] > this.currBet) {
            this.setBet  = playerIndex;
            this.currBet = this.playerBets[playerIndex];
            System.out.printf(">> Updated setBet to index: %d, value: %d\n", playerIndex, this.currBet);
        }

        _potPanel.setPots(pots);

        return new Action(this.playerBets[playerIndex]);
    }

    /**
     * Start new round by reseting current bet and playerBets, and updating setBet variable.
     * @param dealerIndex Location of dealer, used to intialize who sets the betting for
     * the current round.
     */
    public void newRound(int dealerIndex) {
        this.currBet = -1;
        this.setBet = dealerIndex;
        // System.out.printf("New Hand: Updated setBet to index: %d, value: %d\n", dealerIndex, -1);

        for(int i = 0; i < this.playerBets.length; i++) {
            this.playerBets[i] = 0;
        }
    }

    /**
     * Reset sidepot variables for new hand.
     * @param players Array of players, used to record who is in hand to start.
     */
    public void newHand(Player[] players) {
        numOfSidePots = 0;
        activePot     = 0;

        for(int i = 0; i < causedSidepot.length; i++) {
            causedSidepot[i] = false;
        }

        pots         = new int[numOfPlayers];
        betsThisHand = new int[numOfPlayers];

        _potPanel.setPots(pots);

        for(int i = 0; i < players.length; i++)
            inHand[i] = (players[i].getCash() > 0);
    }

    /**
     * Return true if sidepots exist in this Pot object.
     * @return Boolean true or false if sidepots exist.
     */
    public boolean hasSidepots() {
        for(int i = 0; i < causedSidepot.length; i++) {
            if(causedSidepot[i] == true)
                return true;
        }

        return false;
    }

    /**
     * Create a new sidepot based on the value of <b>player</b>'s bet and update display
     * in PotPanel object.
     * @param player Player object of user who went all in, creating sidepot.
     */
    public void createSidepot(Player player) {
        int playerIndex      = player.getIndex();
        int[] sidepotAmounts = new int[numOfPlayers];
        int[] remainingBet   = new int[numOfPlayers];

        System.arraycopy(betsThisHand, 0, remainingBet, 0, betsThisHand.length);

        player.setPlayingHand(false);
        this.causedSidepot[playerIndex] = true;

        // record that all other players reach new sidepot
        for(int i = 0; i < inPot.length; i++) {
            if(i != playerIndex) {
                if(inPot[i] == numOfSidePots && inHand[i]) {
                    inPot[i]++;
                }
            }
        }
        numOfSidePots++;

        // System.out.println("inPot => " + Arrays.toString(inPot));

        sidepotAmounts = orderSidepotAmounts();

        // System.out.println("remainingBet => " + Arrays.toString(remainingBet));

        for(int i = 0; i < sidepotAmounts.length; i++) {
            if(sidepotAmounts[i] > 0) {
                int potTotal   = 0;
                int takeForPot = sidepotAmounts[i];
                if(i > 0) {
                    takeForPot -= sidepotAmounts[i - 1];
                }
                // iterate through money each player has bet this hand and remove for sidepot
                for(int j = 0; j < remainingBet.length; j++) {
                    if(takeForPot <= remainingBet[j]) {
                        potTotal        += takeForPot;
                        remainingBet[j] -= takeForPot;
                    } else {
                        potTotal        += remainingBet[j];
                        remainingBet[j] = 0;
                    }
                }
                pots[i] = potTotal;
            } else {
                pots[i] = 0;
            }
        }

        // System.out.println("pots => " + Arrays.toString(pots));

        // System.out.println("beforeRemainingBet => " + Arrays.toString(sidepotAmounts));

        // add remaining bet to main pot
        for(int i = 0; i < pots.length; i++) {
            if(pots[i] == 0) {
                int potTotal = 0;
                for(int j = 0; j < remainingBet.length; j++) {
                    potTotal += remainingBet[j];
                }
                pots[i] = potTotal;
                break;
            }
        }

        // increment sidepot counter variable
        activePot++;

        _potPanel.setPots(pots);
    }

    /**
     * Update sidepot values without adding a new sidepot.
     */
    public void handleSidePots() {
        int[] sidepotAmounts = new int[numOfPlayers];
        int[] remainingBet   = new int[numOfPlayers];

        pots = new int[numOfPlayers];

        System.arraycopy(betsThisHand, 0, remainingBet, 0, betsThisHand.length);

        // System.out.println("inPot2 => " + Arrays.toString(inPot));

        sidepotAmounts = orderSidepotAmounts();

        // System.out.println("remainingBet2 => " + Arrays.toString(remainingBet));

        for(int i = 0; i < sidepotAmounts.length; i++) {
            if(sidepotAmounts[i] > 0) {
                int potTotal   = 0;
                int takeForPot = sidepotAmounts[i];
                if(i > 0) {
                    takeForPot -= sidepotAmounts[i - 1];
                }
                // iterate through money each player has bet this hand and remove for sidepot
                for(int j = 0; j < remainingBet.length; j++) {
                    if(takeForPot <= remainingBet[j]) {
                        potTotal        += takeForPot;
                        remainingBet[j] -= takeForPot;
                    } else {
                        potTotal        += remainingBet[j];
                        remainingBet[j] = 0;
                    }
                }
                pots[i] = potTotal;
            }
        }

        // System.out.println("pots2 => " + Arrays.toString(pots));

        // System.out.println("remainingBet2 => " + Arrays.toString(remainingBet));

        // add remaining bet to main pot
        for(int i = 0; i < pots.length; i++) {
            if(pots[i] == 0) {
                int potTotal = 0;
                for(int j = 0; j < remainingBet.length; j++) {
                    potTotal += remainingBet[j];
                }
                pots[i] = potTotal;
                break;
            }
        }

        _potPanel.setPots(pots);
    }

    /**
     * Order sidepot values from least to greatest, eliminating zero values.
     * @return Integer array of sidepot values.
     */
    public int[] orderSidepotAmounts() {
        int[] sidepotAmounts = new int[numOfPlayers];

        // get all sidepot amounts
        for(int i = 0; i < numOfPlayers; i++) {
            if(causedSidepot[i]) {
                sidepotAmounts[i] = betsThisHand[i];
            }
        }

        // quick sort of sidepot amounts
        for(int i = 0; i < numOfPlayers - 1; i++) {
            for(int j = 0; j < numOfPlayers - i - 1; j++) {
                if(sidepotAmounts[j] > sidepotAmounts[j + 1]) {
                    int temp = sidepotAmounts[j];
                    sidepotAmounts[j] = sidepotAmounts[j+1];
                    sidepotAmounts[j+1] = temp;
                } else if(sidepotAmounts[j] == sidepotAmounts[j + 1]) {
                    sidepotAmounts[j] = 0;
                }
            }
        }

        // shift non-zero values left
        if(IntStream.of(sidepotAmounts).sum() > 0) {
            while(sidepotAmounts[0] == 0) {
                for(int i = 0; i < sidepotAmounts.length - 1; i++) {
                    sidepotAmounts[i] = sidepotAmounts[i + 1];
                    sidepotAmounts[i + 1] = 0;
                }
            }
        }

        return sidepotAmounts;
    }

    public int getSetBet() {
        return setBet;
    }

    public int clearPot(int potNum) {
        int amount = pots[potNum];
        pots[potNum] = 0;

        return amount;
    }

    public int[] getPlayersInPot(int pot) {
        int[] playersInPot = new int[inPot.length];
        int   index        = 0;
        int[] returnArr;

        for(int i = 0; i < playersInPot.length; i++) {
            playersInPot[i] = -1;
        }

        for(int i = 0; i < inPot.length; i++) {
            if(inPot[i] >= pot) {
                playersInPot[index] = i;
                index++;
            }
        }

        // trim return array
        returnArr = new int[index];
        for(int i = 0; i < index; i++) {
            returnArr[i] = playersInPot[i];
        }

        return returnArr;
    }

    public int numberOfPots() {
        return numOfSidePots + 1;
    }

    public void addAllInPlayers(Player[] players) {
        for(int i = 0; i < causedSidepot.length; i++) {
            if(causedSidepot[i]) {
                players[i].setPlayingHand(true);
            }
        }
    }

    public void setHaveBetPot(int i, boolean b) {
        this.haveBetPot[i] = b;
    }

	public void adjustPot(int bet, int num) {
        this.value     += bet;
        this.pots[num] += bet;
        this._potPanel.adjustPot(num, bet);
	}
}
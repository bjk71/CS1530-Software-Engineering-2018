import java.util.ArrayList;
import java.util.Arrays;

/**
 * Utilies for Poker game, includes:
 *              - Determining winning hand
 *              - Evaluating hand strength
 *              - Calculating pot odds 
 */
public class GameUtils {
    private static final int MAX_RANK = 169;
    /**
     * Return up to nine random names for AI players. Checks against playerName to ensure uniqueness.
     * @param  numOfAI    The number of AI player names to return.
     * @param  playerName Player name to check AI names against.
     * @return           String array of AI player names.
     */
    public String[] getAINames(int numOfAI, String playerName) {
        String[] namesList  = {"Daniel", "Erik", "Antonio", "Fedor","Phil", "Johnathan", "Scott", "Elton", "Brian"};
        String[] returnList = new String[numOfAI];
        int      listLength = 0;
        int      index      = 0;
        
        while(listLength < numOfAI) {
            if(!namesList[index].equals(playerName)) {
                returnList[listLength] = namesList[index];
                listLength++;
            }
            index++;
        }

        return returnList;
    }
  
    /**
     * Given the players' and community cards, determine who should win (via what hand), update
     * the necessary displays and call distributePot with the provided pot value
     * @param thePlayers      The array of Players who can win the provided pot
     * @param communityCards  The array of Cards containing the community cards
     * @param pot             int value of the pot 
     * @param mainPot         boolean telling whether the pot value represents the main pot or a side pot
     * @return                Result string saying who won and with what hand
     */
    public String determineBestHand(Player[] thePlayers, Card[] communityCards, int pot, boolean mainPot){
        return determineBestHand(thePlayers, communityCards, pot, mainPot, true);
    }
    
    /**
     * Given the players' and community cards, determine who should win (via what hand), update
     * the necessary displays (if told to) and call distributePot with the provided pot value
     * @param thePlayers      The array of Players who can win the provided pot
     * @param communityCards  The array of Cards containing the community cards
     * @param pot             int value of the pot
     * @param mainPot         boolean telling whether the pot value represents the main pot or a side pot
     * @param updateDisplay   boolean telling whether or not to update the display panels
     * @return                Result string saying who won and with what hand
     */
    public String determineBestHand(Player[] thePlayers, Card[] communityCards, int pot, boolean mainPot, boolean updateDisplay){
        ArrayList<Player> remainingPlayers = new ArrayList<Player>();
        ArrayList<Player> playersWithBestHand = new ArrayList<Player>();
        int highestHighCard = 0;
        int highestHighCard2 = 0;
        int highestHighCard3 = 0;
        int highestHighCard4 = 0;
        int highestHighCard5 = 0;
        String bestHand;
        String suit;

        //Determine which players to consider
        for(Player p : thePlayers){
            if(p.isPlayingHand()){
                remainingPlayers.add(p);
            }
        }

        //Check if only one player remains
        if(remainingPlayers.size() == 1){
            distributePot(remainingPlayers, thePlayers, pot, updateDisplay);
            return remainingPlayers.get(0).getName() + " has won as the last man Standing!";
        }

        //Rather than always having to check to arrays, combine community cards with each players'
        for(Player p : remainingPlayers){
            ArrayList<Card> allAvailableCards = new ArrayList<Card>();
            allAvailableCards.addAll(Arrays.asList(communityCards));
            allAvailableCards.addAll(Arrays.asList(p.getCards()));

            Card[] hand = new Card[allAvailableCards.size()];
            hand = allAvailableCards.toArray(hand);
            p.setFullHand(hand);
        }

        //Determine best hand
        //Royal/Straight Flush?-------------------------------------------------------------------------
        for(Player p : remainingPlayers){
            suit = containsFlush(p.getFullHand());
            if(!suit.equals("")){
                int highCard = containsStraight(p.getFullHand(), suit, false);
                if(highCard >= highestHighCard){
                    if(highCard > highestHighCard){
                        highestHighCard =  highCard;
                        playersWithBestHand = new ArrayList<Player>();
                    }

                    playersWithBestHand.add(p);
                }
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "a Royal Flush!!!";
            } else if(highestHighCard == 13){ //King
                bestHand = "a King's High Straight Flush!!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a Queen's High Straight Flush!!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a Jack's High Straight Flush!!";
            } else {
                bestHand = "a " + highestHighCard + "'s High Straight Flush!!";
            }

            if(mainPot){
                displayWinningHand(playersWithBestHand, 1);
            }

            clearFullHand(remainingPlayers);
            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Four of a kind?-------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsNOfAKind(p.getFullHand(), 4, false);
            if(highCards[0] >= highestHighCard){
                if(highCards[0] > highestHighCard){
                    highestHighCard =  highCards[0];
                    highestHighCard2 = highCards[1];
                    playersWithBestHand = new ArrayList<Player>();
                    playersWithBestHand.add(p);
                } else if(highCards[1] >= highestHighCard2){
                    if(highCards[1] > highestHighCard2){
                        highestHighCard2 = highCards[1];
                        playersWithBestHand = new ArrayList<Player>();
                    }

                    playersWithBestHand.add(p);
                }                
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "four Ace's!!";
            } else if(highestHighCard == 13){ //King
                bestHand = "four Kings!!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "four Queens!!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "four Jacks!!";
            } else {
                bestHand = "four " + highestHighCard + "'s!!";
            }

            if(mainPot){
                displayWinningHand(playersWithBestHand, 2);
            }

            clearFullHand(remainingPlayers);
            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Full House?-----------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsFullHouse(p.getFullHand(), false);
            if(highCards[0] >= highestHighCard){
                if(highCards[0] > highestHighCard){
                    highestHighCard =  highCards[0];
                    highestHighCard2 = highCards[1];
                    playersWithBestHand = new ArrayList<Player>();
                    playersWithBestHand.add(p);
                } else if(highCards[1] >= highestHighCard2){
                    if(highCards[1] > highestHighCard2){
                        highestHighCard2 = highCards[1];
                        playersWithBestHand = new ArrayList<Player>();
                    }

                    playersWithBestHand.add(p);
                }                
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "a full house, Ace's full of ";
            } else if(highestHighCard == 13){ //King
                bestHand = "a full house, Kings full of ";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a full house, Queens full of ";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a full house, Jacks full of ";
            } else {
                bestHand = "a full house, " + highestHighCard + "'s full of ";
            }

            if(highestHighCard2 == 14){ //Ace
                bestHand += "Ace's!!";
            } else if(highestHighCard2 == 13){ //King
                bestHand += "Kings!!";
            } else if(highestHighCard2 == 12){ //Queen
                bestHand += "Queens!!";
            } else if(highestHighCard2 == 11){ //Jack
                bestHand += "Jacks!!";
            } else {
                bestHand += highestHighCard2 + "'s!!";
            }

            if(mainPot){
                displayWinningHand(playersWithBestHand, 3);
            }

            clearFullHand(remainingPlayers);
            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Flush?----------------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        suit = "";
        for(Player p : remainingPlayers){
            suit = containsFlush(p.getFullHand());
            if(!suit.equals("")){
                int[] highCards = determineHighCard(p.getFullHand(), suit, false);
                if(highCards[0] >= highestHighCard){
                    if(highCards[0] > highestHighCard){
                        highestHighCard =  highCards[0];
                        highestHighCard2 = highCards[1];
                        highestHighCard3 = highCards[2];
                        highestHighCard4 = highCards[3];
                        highestHighCard5 = highCards[4];
                        playersWithBestHand = new ArrayList<Player>();
                        playersWithBestHand.add(p);
                    } else if(highCards[1] >= highestHighCard2){
                        if(highCards[1] > highestHighCard2){
                            highestHighCard2 = highCards[1];
                            highestHighCard3 = highCards[2];
                            highestHighCard4 = highCards[3];
                            highestHighCard5 = highCards[4];
                            playersWithBestHand = new ArrayList<Player>();
                            playersWithBestHand.add(p);
                        } else if(highCards[2] >= highestHighCard3){
                            if(highCards[2] > highestHighCard3){
                                highestHighCard3 = highCards[2];
                                highestHighCard4 = highCards[3];
                                highestHighCard5 = highCards[4];
                                playersWithBestHand = new ArrayList<Player>();
                                playersWithBestHand.add(p);
                            } else if(highCards[3] >= highestHighCard4){
                                if(highCards[3] > highestHighCard4){
                                    highestHighCard4 = highCards[3];
                                    highestHighCard5 = highCards[4];
                                    playersWithBestHand = new ArrayList<Player>();
                                    playersWithBestHand.add(p);
                                } else if(highCards[4] >= highestHighCard5){
                                    if(highCards[4] > highestHighCard5){
                                        highestHighCard5 = highCards[4];
                                        playersWithBestHand = new ArrayList<Player>();
                                    }
                                
                                    playersWithBestHand.add(p);
                                }
                            }
                        }
                    }
                }
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "an Ace High Flush!";
            } else if(highestHighCard == 13){ //King
                bestHand = "a King's High Flush!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a Queen's High Flush!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a Jack's High Flush!";
            } else {
                bestHand = "a " + highestHighCard + "'s High Flush!";
            }

            if(mainPot){
                displayWinningHand(playersWithBestHand, 4);
            }

            clearFullHand(remainingPlayers);
            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Straight?-------------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int highCard = containsStraight(p.getFullHand(), false);
            if(highCard >= highestHighCard){
                if(highCard > highestHighCard){
                    highestHighCard =  highCard;
                    playersWithBestHand = new ArrayList<Player>();
                }

                playersWithBestHand.add(p);
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "an Ace High Straight!";
            } else if(highestHighCard == 13){ //King
                bestHand = "a King's High Straight!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a Queen's High Straight!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a Jack's High Straight!";
            } else {
                bestHand = "a " + highestHighCard + "'s High Straight!";
            }

            if(mainPot){
                displayWinningHand(playersWithBestHand, 5);
            }

            clearFullHand(remainingPlayers);
            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Three of a Kind?------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        highestHighCard3 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsNOfAKind(p.getFullHand(), 3, false);
            if(highCards[0] >= highestHighCard){
                if(highCards[0] > highestHighCard){
                    highestHighCard =  highCards[0];
                    highestHighCard2 = highCards[1];
                    highestHighCard3 = highCards[2];
                    playersWithBestHand = new ArrayList<Player>();
                    playersWithBestHand.add(p);
                } else if(highCards[1] >= highestHighCard2){
                    if(highCards[1] > highestHighCard2){
                        highestHighCard2 = highCards[1];
                        highestHighCard3 = highCards[2];
                        playersWithBestHand = new ArrayList<Player>();
                        playersWithBestHand.add(p);
                    } else if(highCards[2] >= highestHighCard3){
                        if(highCards[2] > highestHighCard3){
                            highestHighCard3 = highCards[2];
                            playersWithBestHand = new ArrayList<Player>();
                        }

                        playersWithBestHand.add(p);
                    }
                }
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "three Ace's!";
            } else if(highestHighCard == 13){ //King
                bestHand = "three Kings!";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "three Queens!";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "three Jacks!";
            } else {
                bestHand = "three " + highestHighCard + "'s!";
            }
            
            if(mainPot){
                displayWinningHand(playersWithBestHand, 6);
            }

            clearFullHand(remainingPlayers);
            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Two Pair?-------------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        highestHighCard3 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsTwoPair(p.getFullHand(), false);
            if(highCards[0] >= highestHighCard){
                if(highCards[0] > highestHighCard){
                    highestHighCard =  highCards[0];
                    highestHighCard2 = highCards[1];
                    highestHighCard3 = highCards[2];
                    playersWithBestHand = new ArrayList<Player>();
                    playersWithBestHand.add(p);
                } else if(highCards[1] >= highestHighCard2){
                    if(highCards[1] > highestHighCard2){
                        highestHighCard2 = highCards[1];
                        highestHighCard3 = highCards[2];
                        playersWithBestHand = new ArrayList<Player>();
                        playersWithBestHand.add(p);
                    } else if(highCards[2] >= highestHighCard3){
                        if(highCards[2] > highestHighCard3){
                            highestHighCard3 = highCards[2];
                            playersWithBestHand = new ArrayList<Player>();
                        }

                        playersWithBestHand.add(p);
                    }
                }
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "a two pair, Ace's over ";
            } else if(highestHighCard == 13){ //King
                bestHand = "a two pair, Kings over ";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a two pair, Queens over ";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a two pair, Jacks over ";
            } else {
                bestHand = "a two pair, " + highestHighCard + "'s over ";
            }

            if(highestHighCard2 == 14){ //Ace
                bestHand += "Ace's.";
            } else if(highestHighCard2 == 13){ //King
                bestHand += "Kings.";
            } else if(highestHighCard2 == 12){ //Queen
                bestHand += "Queens.";
            } else if(highestHighCard2 == 11){ //Jack
                bestHand += "Jacks.";
            } else {
                bestHand += highestHighCard2 + "'s.";
            }

            if(mainPot){
                displayWinningHand(playersWithBestHand, 7);
            }

            clearFullHand(remainingPlayers);
            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //One Pair?-------------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        highestHighCard3 = 0;
        highestHighCard4 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsNOfAKind(p.getFullHand(), 2, false);
            if(highCards[0] >= highestHighCard){
                if(highCards[0] > highestHighCard){
                    highestHighCard =  highCards[0];
                    highestHighCard2 = highCards[1];
                    highestHighCard3 = highCards[2];
                    highestHighCard4 = highCards[3];
                    playersWithBestHand = new ArrayList<Player>();
                    playersWithBestHand.add(p);
                } else if(highCards[1] >= highestHighCard2){
                    if(highCards[1] > highestHighCard2){
                        highestHighCard2 = highCards[1];
                        highestHighCard3 = highCards[2];
                        highestHighCard4 = highCards[3];
                        playersWithBestHand = new ArrayList<Player>();
                        playersWithBestHand.add(p);
                    } else if(highCards[2] >= highestHighCard3){
                        if(highCards[2] > highestHighCard3){
                            highestHighCard3 = highCards[2];
                            highestHighCard4 = highCards[3];
                            playersWithBestHand = new ArrayList<Player>();
                            playersWithBestHand.add(p);
                        } else if(highCards[3] >= highestHighCard4){
                            if(highCards[3] > highestHighCard4){
                                highestHighCard4 = highCards[3];
                                playersWithBestHand = new ArrayList<Player>();
                            }
                            
                            playersWithBestHand.add(p);
                        }
                    }
                }
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "two Ace's.";
            } else if(highestHighCard == 13){ //King
                bestHand = "two Kings.";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "two Queens.";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "two Jacks.";
            } else {
                bestHand = "two " + highestHighCard + "'s.";
            }

            if(mainPot){
                displayWinningHand(playersWithBestHand, 8);
            }

            clearFullHand(remainingPlayers);
            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //High Card?------------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        highestHighCard3 = 0;
        highestHighCard4 = 0;
        highestHighCard5 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = determineHighCard(p.getFullHand(), false);
            if(highCards[0] >= highestHighCard){
                if(highCards[0] > highestHighCard){
                    highestHighCard =  highCards[0];
                    highestHighCard2 = highCards[1];
                    highestHighCard3 = highCards[2];
                    highestHighCard4 = highCards[3];
                    highestHighCard5 = highCards[4];
                    playersWithBestHand = new ArrayList<Player>();
                    playersWithBestHand.add(p);
                } else if(highCards[1] >= highestHighCard2){
                    if(highCards[1] > highestHighCard2){
                        highestHighCard2 = highCards[1];
                        highestHighCard3 = highCards[2];
                        highestHighCard4 = highCards[3];
                        highestHighCard5 = highCards[4];
                        playersWithBestHand = new ArrayList<Player>();
                        playersWithBestHand.add(p);
                    } else if(highCards[2] >= highestHighCard3){
                        if(highCards[2] > highestHighCard3){
                            highestHighCard3 = highCards[2];
                            highestHighCard4 = highCards[3];
                            highestHighCard5 = highCards[4];
                            playersWithBestHand = new ArrayList<Player>();
                            playersWithBestHand.add(p);
                        } else if(highCards[3] >= highestHighCard4){
                            if(highCards[3] > highestHighCard4){
                                highestHighCard4 = highCards[3];
                                highestHighCard5 = highCards[4];
                                playersWithBestHand = new ArrayList<Player>();
                                playersWithBestHand.add(p);
                            } else if(highCards[4] >= highestHighCard5){
                                if(highCards[4] > highestHighCard5){
                                    highestHighCard5 = highCards[4];
                                    playersWithBestHand = new ArrayList<Player>();
                                }
                            
                                playersWithBestHand.add(p);
                            }
                        }
                    }
                }
            }
        }

        if(playersWithBestHand.size() > 0){
            if(highestHighCard == 14){ //Ace
                bestHand = "an Ace High.";
            } else if(highestHighCard == 13){ //King
                bestHand = "a King's High.";
            } else if(highestHighCard == 12){ //Queen
                bestHand = "a Queen's High.";
            } else if(highestHighCard == 11){ //Jack
                bestHand = "a Jack's High.";
            } else {
                bestHand = "a " + highestHighCard + "'s High.";
            }

            if(mainPot){
                displayWinningHand(playersWithBestHand, 9);
            }

            clearFullHand(remainingPlayers);
            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }
        
        return "No Winner!!"; //This shoud be impossible
    }

    //Determines highest value within passed in array of cards
    private int[] determineHighCard(Card[] hand, boolean setInWinningHand){
        return this.determineHighCard(hand, "", setInWinningHand);
    }

    //Determines highest value within passed in array of cards (of specified suit)
    private int[] determineHighCard(Card[] hand, String suit, boolean setInWinningHand){
        int[] returnArr = new int[7];
        boolean contains = true;
        for(int i = 0; i < hand.length; i++){
            Card card = hand[i];
            if( suit.equals("") || suit.equals(card.getSuit()) ){
                if(card.getValue().equals("A")){
                    returnArr[i] = 14;
                } else if(card.getValue().equals("K")){
                    returnArr[i] = 13;
                } else if(card.getValue().equals("Q")){
                    returnArr[i] = 12;
                } else if(card.getValue().equals("J")){
                    returnArr[i] = 11;
                } else {
                    returnArr[i] = Integer.parseInt(card.getValue());
                }
            }
            
        }

        //Insertion Sort (cuz its a small array)
        for(int i = 1; i < returnArr.length; i++){
            int k = i;
            while(k > 0 && returnArr[k] > returnArr[k-1]){
                int temp = returnArr[k];
                returnArr[k] = returnArr[k-1];
                returnArr[k-1] = temp;
                k--;
            }
        }

        //Make sure at least 5 valid card values are being returned, else return all values -1
        for(int i = 0; i < 5; i++){
            if(returnArr[i] == 0){
                contains = false;
            }
        }

        if(!contains){
            returnArr = new int[]{-1, -1, -1, -1, -1};
        } else {
            int[] tempArr = new int[5];
            for(int i = 0; i < tempArr.length; i++){
                tempArr[i] = returnArr[i];
            }

            returnArr = tempArr;
        }

        //Set inWinningHand attribute of cards that make up winning hand to be true if told to
        if(contains && setInWinningHand){
            for(int i = 0; i < returnArr.length; i++){
                String returnVal = "";
                if(returnArr[i] == 14){
                    returnVal = "A";
                } else if(returnArr[i] == 13){
                    returnVal = "K";
                } else if(returnArr[i] == 12){
                    returnVal = "Q";
                } else if(returnArr[i] == 11){
                    returnVal = "J";
                } else {
                    returnVal = returnArr[i] + "";
                }

                ArrayList<Card> cardsWithCurrentValue = new ArrayList<Card>();
                for(int j = 0; j < hand.length; j++){
                    if( hand[j].getValue().equals(returnVal) && ( suit.equals("") || suit.equals(hand[j].getSuit()) ) ){
                        cardsWithCurrentValue.add(hand[j]);
                    }
                }

                //Set inWinningHand attribute as long as there isn't already a card with the same value set
                if(cardsWithCurrentValue.size() == 1){
                    cardsWithCurrentValue.get(0).setInWinningHand(true);
                } else {
                    boolean alreadyMarked = false;
                    for(Card c : cardsWithCurrentValue){
                        if(c.isInWinningHand()){
                            alreadyMarked = true;
                        }
                    }

                    if(!alreadyMarked){
                        cardsWithCurrentValue.get(0).setInWinningHand(true);
                    }
                }
            }
        }
        
        return returnArr;
    }

    //Determines if passed in array of cards contains a flush, returns suit if it does, empty string if not
    private String containsFlush(Card[] hand){
        //Clubs = 0, Diamonds = 1, Hearts = 2, Spades = 3
        int[] suits = {0,0,0,0};
        for(Card card : hand){
            if(card.getSuit().equals("C")){
                suits[0]++;
            } else if(card.getSuit().equals("D")){
                suits[1]++;
            } else if(card.getSuit().equals("H")){
                suits[2]++;
            } else if(card.getSuit().equals("S")){
                suits[3]++;
            }
        }

        if(suits[0] >= 5){
            return "C";
        } else if(suits[1] >= 5){
            return "D";
        } else if(suits[2] >= 5){
            return "H";
        } else if(suits[3] >= 5){
            return "S";
        }

        return "";
    }

    //Determines if passed in array of cards contains a Straight, returns high card value if it does, -1 if not
    private int containsStraight(Card[] hand, boolean setInWinningHand){
        return this.containsStraight(hand, "", setInWinningHand);
    }

    //Determines if passed in array of cards contains a Straight of specified suit,
    //returns high card value if it does, -1 if not
    private int containsStraight(Card[] hand, String suit, boolean setInWinningHand){
        int[] values = new int[14];
        int numInArow = 0;
        int returnVal = -2;
        for(Card card : hand){
            if( suit.equals("") || suit.equals(card.getSuit()) ){
                if(card.getValue().equals("A")){
                    values[0] = 1;
                    values[13] = 1;
                } else if(card.getValue().equals("K")){
                    values[12] = 1;
                } else if(card.getValue().equals("Q")){
                    values[11] = 1;
                } else if(card.getValue().equals("J")){
                    values[10] = 1;
                } else {
                    values[Integer.parseInt(card.getValue()) - 1] = 1;
                }
            }
        }


        for(int i = 0; i < 14; i++){
            if(values[i] == 1){
                numInArow++;
            } else {
                numInArow = 0;
            }

            if(numInArow >= 5){
                returnVal = i;
            }
        }

        returnVal = returnVal + 1; //To offest the array starting at 0, returning a 10 means high card is 10, not jack

        if ( returnVal > 0 && setInWinningHand ) { //Set inWinningHand attribute of cards that make up winning hand to be true if told to
            for(int i = returnVal; i > returnVal - 5; i--){
                String highVal = "";
                if(i == 14){
                    highVal = "A";
                } else if(i == 13){
                    highVal = "K";
                } else if(i == 12){
                    highVal = "Q";
                } else if(i == 11){
                    highVal = "J";
                } else {
                    highVal = i + "";
                }

                ArrayList<Card> cardsWithCurrentValue = new ArrayList<Card>();
                for(int j = 0; j < hand.length; j++){
                    if( hand[j].getValue().equals(highVal) && ( suit.equals("") || suit.equals(hand[j].getSuit()) ) ){
                        cardsWithCurrentValue.add(hand[j]);
                    }
                }

                //Set inWinningHand attribute as long as there isn't already a card with the same value set
                if(cardsWithCurrentValue.size() == 1){
                    cardsWithCurrentValue.get(0).setInWinningHand(true);
                } else {
                    boolean alreadyMarked = false;
                    for(Card c : cardsWithCurrentValue){
                        if(c.isInWinningHand()){
                            alreadyMarked = true;
                        }
                    }

                    if(!alreadyMarked){
                        cardsWithCurrentValue.get(0).setInWinningHand(true);
                    }
                }
            }
        }

        return returnVal;
    }

    //Determines if passed in array of cards contains a full house, 
    //returns array containing {three of a kind, two of a kind} if it does, {-1, -1} if not
    private int[] containsFullHouse(Card[] hand, boolean setInWinningHand){
        int[] values = new int[15];
        int[] returnArr = {-1, -1};
        for(Card card : hand){
            if(card.getValue().equals("A")){
                values[14]++;
            } else if(card.getValue().equals("K")){
                values[13]++;
            } else if(card.getValue().equals("Q")){
                values[12]++;
            } else if(card.getValue().equals("J")){
                values[11]++;
            } else {
                values[Integer.parseInt(card.getValue())]++;
            }
        }

        //Determine which values (if any) meet requirements for a full house
        for(int i = 2; i < 15; i++){
            if(values[i] == 3){
                returnArr[0] = i;
            }
        }

        for(int i = 2; i < 15; i++){
            if(values[i] >= 2 && i != returnArr[0]){
                returnArr[1] = i;
            }
        }

        //Make sure both values are set, if not, it doesn't contain -> {-1,-1}
        if(returnArr[0] == -1 || returnArr[1] == -1){
            returnArr[0] = -1;
            returnArr[1] = -1;
        } else if (setInWinningHand){ //Set inWinningHand attribute of cards that make up winning hand to be true if told to
            for(int i = 0; i < returnArr.length; i++){
                String returnVal = "";
                if(returnArr[i] == 14){
                    returnVal = "A";
                } else if(returnArr[i] == 13){
                    returnVal = "K";
                } else if(returnArr[i] == 12){
                    returnVal = "Q";
                } else if(returnArr[i] == 11){
                    returnVal = "J";
                } else {
                    returnVal = returnArr[i] + "";
                }

                ArrayList<Card> cardsWithCurrentValue = new ArrayList<Card>();
                for(int j = 0; j < hand.length; j++){
                    if(hand[j].getValue().equals(returnVal)){
                        cardsWithCurrentValue.add(hand[j]);
                    }
                }

                //Set inWinningHand attribute for the 3 of a kind and 2 of a kind value (as long as theres only 2)
                //else, check to make sure only 2 cards get/are set
                if( i == 0 || (i != 0 && cardsWithCurrentValue.size() == 2) ){
                    for(Card c : cardsWithCurrentValue){
                        c.setInWinningHand(true);
                    }
                } else {
                    int alreadyMarked = 0;
                    for(Card c : cardsWithCurrentValue){
                        if(c.isInWinningHand()){
                            alreadyMarked++;
                        }
                    }

                    if(alreadyMarked == 0){
                        cardsWithCurrentValue.get(0).setInWinningHand(true);
                        cardsWithCurrentValue.get(1).setInWinningHand(true);
                    } else if(alreadyMarked == 1){
                        for(Card c : cardsWithCurrentValue){
                            if( alreadyMarked == 1 && !c.isInWinningHand()){
                                c.setInWinningHand(true);
                                alreadyMarked++;
                            }
                        }
                    } else if(alreadyMarked > 2){
                        System.out.println("Error in displaying cards in winning hand!");
                    }
                }
            }
        }

        return returnArr;
    }

    //Determines if passed in array of cards contains two pairs, 
    //returns array containing {larger pair, smaller pair} if it does, {-1, -1} if not
    private int[] containsTwoPair(Card[] hand, boolean setInWinningHand){
        int[] values = new int[15];
        int[] returnArr = new int[3];
        boolean contains = true;
        for(Card card : hand){
            if(card.getValue().equals("A")){
                values[14]++;
            } else if(card.getValue().equals("K")){
                values[13]++;
            } else if(card.getValue().equals("Q")){
                values[12]++;
            } else if(card.getValue().equals("J")){
                values[11]++;
            } else {
                values[Integer.parseInt(card.getValue())]++;
            }
        }

        //Determine pair values and the kicker
        for(int i = 2; i < 15; i++){
            if(values[i] == 2){
                returnArr[0] = i;
            }
        }
    
        for(int i = 2; i < 15; i++){
            if(values[i] == 2 && i != returnArr[0]){
                returnArr[1] = i;
            }
        }
            
        for(int i = 2; i < 15; i++){
            if(values[i] > 0 && i != returnArr[0] && i != returnArr[1]){
                returnArr[2] = i;
            }
        }   
        
        //If any of the values were note set, hand does not contain a two pair, reset return values to -1
        for(int i = 0; i < returnArr.length; i++){
            if(returnArr[i] == 0){
                contains = false;
            }
        }

        if(!contains){
            for(int i = 0; i < returnArr.length; i++){
                returnArr[i] = -1;
            }
        } else if (setInWinningHand){ //Set inWinningHand attribute of cards that make up winning hand to be true if told to
            for(int i = 0; i < returnArr.length; i++){
                String returnVal = "";
                if(returnArr[i] == 14){
                    returnVal = "A";
                } else if(returnArr[i] == 13){
                    returnVal = "K";
                } else if(returnArr[i] == 12){
                    returnVal = "Q";
                } else if(returnArr[i] == 11){
                    returnVal = "J";
                } else {
                    returnVal = returnArr[i] + "";
                }

                ArrayList<Card> cardsWithCurrentValue = new ArrayList<Card>();
                for(int j = 0; j < hand.length; j++){
                    if(hand[j].getValue().equals(returnVal)){
                        cardsWithCurrentValue.add(hand[j]);
                    }
                }

                //Set inWinningHand attribute for the 2 pairs and the kicker as long as 
                //there isn't already a card with the same value set
                if(i == 0 || i == 1){
                    for(Card c : cardsWithCurrentValue){
                        c.setInWinningHand(true);
                    }
                } else {
                    if(cardsWithCurrentValue.size() == 1){
                        cardsWithCurrentValue.get(0).setInWinningHand(true);
                    } else {
                        boolean alreadyMarked = false;
                        for(Card c : cardsWithCurrentValue){
                            if(c.isInWinningHand()){
                                alreadyMarked = true;
                            }
                        }

                        if(!alreadyMarked){
                            cardsWithCurrentValue.get(0).setInWinningHand(true);
                        }
                    }
                }
            }
        }

        return returnArr;
    }

    //Determines if passed in array of cards contains a exactly N of any card, 
    //returns the cards value if it does, -1 if not
    private int[] containsNOfAKind(Card[] hand, int N, boolean setInWinningHand){
        int retArrSize = 6 - N;
        int[] values = new int[15];
        int[] returnArr = new int[retArrSize];
        ArrayList<Integer> usedVals = new ArrayList<Integer>();
        boolean contains = true;
        for(Card card : hand){
            if(card.getValue().equals("A")){
                values[14]++;
            } else if(card.getValue().equals("K")){
                values[13]++;
            } else if(card.getValue().equals("Q")){
                values[12]++;
            } else if(card.getValue().equals("J")){
                values[11]++;
            } else {
                values[Integer.parseInt(card.getValue())]++;
            }
        }

        //Determine if the hand does contain N of a kind, and then the rest of the high cards
        for(int h = 0; h < retArrSize; h++){
            if(h == 0){
                for(int i = 2; i < 15; i++){
                    if(values[i] == N){
                        returnArr[h] = i;
                    }
                }
            } else {
                for(int i = 2; i < 15; i++){
                    if(values[i] > 0 && !usedVals.contains(i)){
                        returnArr[h] = i;
                    }
                }
            }
            
            usedVals.add(returnArr[h]);
        }

        //If any of the values were note set, hand does not contain N of a Kind, reset return values to -1
        for(int i = 0; i < returnArr.length; i++){
            if(returnArr[i] == 0){
                contains = false;
            }
        }

        if(!contains){
            for(int i = 0; i < returnArr.length; i++){
                returnArr[i] = -1;
            }
        } else if (setInWinningHand){ //Set inWinningHand attribute of cards that make up winning hand to be true if told to
            for(int i = 0; i < returnArr.length; i++){
                String returnVal = "";
                if(returnArr[i] == 14){
                    returnVal = "A";
                } else if(returnArr[i] == 13){
                    returnVal = "K";
                } else if(returnArr[i] == 12){
                    returnVal = "Q";
                } else if(returnArr[i] == 11){
                    returnVal = "J";
                } else {
                    returnVal = returnArr[i] + "";
                }

                ArrayList<Card> cardsWithCurrentValue = new ArrayList<Card>();
                for(int j = 0; j < hand.length; j++){
                    if(hand[j].getValue().equals(returnVal)){
                        cardsWithCurrentValue.add(hand[j]);
                    }
                }

                //Set inWinningHand attribute for the N of a kind value (as long as theres only 2) and the kicker(s) 
                //as long as there isn't already a card with the same value set
                if(i == 0){
                    for(Card c : cardsWithCurrentValue){
                        c.setInWinningHand(true);
                    }
                } else {
                    if(cardsWithCurrentValue.size() == 1){
                        cardsWithCurrentValue.get(0).setInWinningHand(true);
                    } else {
                        boolean alreadyMarked = false;
                        for(Card c : cardsWithCurrentValue){
                            if(c.isInWinningHand()){
                                alreadyMarked = true;
                            }
                        }

                        if(!alreadyMarked){
                            cardsWithCurrentValue.get(0).setInWinningHand(true);
                        }
                    }
                }
            }
        }

        return returnArr;
    }

    /**
     * Makes the result string describing the outcome of the game (who won and how)
     * @param winners      ArrayList of players that won
     * @param winningHand  String containing a description of the winning hand
     * @return             The result string
     */
    public String buildResultsString(ArrayList<Player> winners, String winningHand){
        StringBuilder resultStrBuilder = new StringBuilder();
        String deliminator = "";
        for(int i = 0; i < winners.size(); i++){
            resultStrBuilder.append(deliminator).append(winners.get(i).getName());
            
            deliminator = ", ";
            if(i == winners.size() - 2){
                if(winners.size() == 2){
                    deliminator = " and ";
                } else {
                    deliminator = ", and ";
                }
            }
        }

        if(winners.size() == 1){
            resultStrBuilder.append(" won with ").append(winningHand);
        } else if(winners.size() == 2){
            resultStrBuilder.append(" both won with ").append(winningHand);
        } else {
            resultStrBuilder.append(" all won with ").append(winningHand);
        }

        return resultStrBuilder.toString();
    }

    //Sets inWinningHand attribute of the cards that make up the winning hand to be true
    private void displayWinningHand(ArrayList<Player> winners, int winningHandPower){
        for(Player p : winners){
            if(winningHandPower == 1){ //Royal/Straight Flush
                containsStraight(p.getFullHand(), containsFlush(p.getFullHand()), true);
            } else if(winningHandPower == 2){ //Four of a kind
                containsNOfAKind(p.getFullHand(), 4, true);
            } else if(winningHandPower == 3){ //Full House
                containsFullHouse(p.getFullHand(), true);
            } else if(winningHandPower == 4){ //Flush
                determineHighCard(p.getFullHand(), containsFlush(p.getFullHand()), true);
            } else if(winningHandPower == 5){ //Straight
                containsStraight(p.getFullHand(), true);
            } else if(winningHandPower == 6){ //Three of a Kind
                containsNOfAKind(p.getFullHand(), 3, true);
            } else if(winningHandPower == 7){ //Two Pair
                containsTwoPair(p.getFullHand(), true);
            } else if(winningHandPower == 8){ //One Pair
                containsNOfAKind(p.getFullHand(), 2, true);
            } else if(winningHandPower == 9){ //High Card
                determineHighCard(p.getFullHand(), true);
            } else {
                //This should never happen
            }
        }
    }

    //When finished determining best hand, clear fullHand attribute of player for next round
    private void clearFullHand(ArrayList<Player> thePlayers){
        for(Player p : thePlayers){
            p.setFullHand(null);
        }
    }

    //Divide up the pot among the winner(s), and set it back to 0
    private void distributePot(ArrayList<Player> winners, Player[] thePlayers, int pot, boolean updateDisplay){
        if(winners.size() > 0){
            int winningsAmount = pot/winners.size();
            for(Player p : thePlayers){
                if(winners.indexOf(p) >= 0){
                    p.adjustCash(winningsAmount, updateDisplay);

                }
            }
        }

        return;
    }

    /**
     * Gets a string representation of the pot odds
     * @param pot   Current value of pot
     * @param bet   Minimum bet facing player
     * 
     */
    public String getPotOdds(int pot, int bet) {
        // System.out.println("DEBUG: " + pot);
        // System.out.println("DEBUG: " + bet);
        
        int gcm;
        if (bet == 0) {     //if no bet facing you, default to 1
            bet = 1;
        }
        gcm = gcm (pot, bet);
        
        return "Pot Odds: " + (pot/gcm) + " : " + (bet/gcm);
    }

    /**
     * Returns the greatest common multiple of two integeres
     * @param a     2nd Integer
     * @param b     1st Integer
     * @return      Greatest Common Multiple
     */
    private int gcm(int a, int b) {
        return b == 0 ? a : gcm(b, a % b);
    }

    /**
     * Gets a string representation of the strength
     * of a hand. Hands are ranked on a scale of 
     * 1 to 169, according to http://www.preflophands.com/
     */
    public String getHandStrength(Card[] hand) {
        int rank;
        String handName;
        String a, b;
        String ret = "";

        //Make 1st card highesst
        if (hand[0].compareTo(hand[1]) < 0 ) {
            Card temp = hand[0];
            hand[0] = hand[1];
            hand[1] = temp;
        }

        a = hand[0].getValue();
        b = hand[1].getValue();

        if (areSuited(hand[0], hand[1])) {      //Suited
            switch (a) {
                case "A":
                    switch (b) {
                        case "K":
                            rank = 4;    
                            handName = "Big Slick in a Suit";
                            break;
                        case "Q":
                            rank = 6;
                            handName = "Anthony & Cleopatra";
                            break;
                        case "J":
                            rank = 8;
                            handName = "Black Jack";
                            break;
                        case "10":
                            rank = 12;
                            handName = "Bookends";
                            break;
                        case "9":
                            rank = 19;
                            handName = "Driving The Truck";
                            break;
                        case "8":
                            rank = 24;
                            handName = "Dead Man's Hand";
                            break;
                        case "7":
                            rank = 30;
                            handName = "Red Baron";
                            break;
                        case "6":
                            rank = 34;
                            handName = "Mile High";
                            break;
                        case "5":
                            rank = 28;
                            handName = "High Five";
                            break;
                        case "4":
                            rank = 32;
                            handName = "Sharp Tops";
                            break;
                        case "3":
                            rank = 33;
                            handName = "Ash Tray";
                            break;
                        case "2":
                            rank = 39;
                            handName = "Hunting Season";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;
                case "K":
                    switch (b) {
                        case "Q":
                            rank = 7;
                            handName = "Royal Couple";
                            break;
                        case "J":
                            rank = 9;
                            handName = "King John";
                            break;
                        case "10":
                            rank = 14;
                            handName = "Katie";
                            break;
                        case "9":
                            rank = 22;
                            handName = "Canine";
                            break;
                        case "8":
                            rank = 37;
                            handName = "The Feast";
                            break;
                        case "7":
                            rank = 44;
                            handName = "King Salmon";
                            break;
                        case "6":
                            rank = 53;
                            handName = "The Concubine";
                            break;
                        case "5":
                            rank = 55;
                            handName = "King of Nickels";
                            break;
                        case "4":
                            rank = 58;
                            handName = "Fork";
                            break;
                        case "3":
                            rank = 59;
                            handName = "King Crab";
                            break;
                        case "2":
                            rank = 60;
                            handName = "White Men Can't Jump";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;
                case "Q":
                    switch (b) {
                        case "J":
                            rank = 13;
                            handName = "Maverick";
                            break;
                        case "10":
                            rank = 15;
                            handName = "Gratitude";
                            break;
                        case "9":
                            rank = 25;
                            handName = "Quinine";
                            break;
                        case "8":
                            rank = 43;
                            handName = "Kuwait";
                            break;
                        case "7":
                            rank = 61;
                            handName = "Computer Hand";
                            break;
                        case "6":
                            rank = 66;
                            handName = "Nesquik";
                            break;
                        case "5":
                            rank = 69;
                            handName = "Granny Mae";
                            break;
                        case "4":
                            rank = 71;
                            handName = "Housework";
                            break;
                        case "3":
                            rank = 72;
                            handName = "San Francisco Busboy";
                            break;
                        case "2":
                            rank = 75;
                            handName = "Windsor Waiter";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;
                case "J":
                    switch (b) {
                        case "10":
                            rank = 16;
                            handName = "Morgan";
                            break;
                        case "9":
                            rank = 26;
                            handName = "Jeanine";
                            break;
                        case "8":
                            rank = 41;
                            handName = "Jeffrey Dalmer";
                            break;
                        case "7":
                            rank = 64;
                            handName = "Dice Hand";
                            break;
                        case "6":
                            rank = 79;
                            handName = "Railroad";
                            break;
                        case "5":
                            rank = 82;
                            handName = "Jackson Five";
                            break;
                        case "4":
                            rank = 86;
                            handName = "Flat Tire";
                            break;
                        case "3":
                            rank = 87;
                            handName = "J-Lo";
                            break;
                        case "2":
                            rank = 89;
                            handName = "The Jew";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;
                case "10":
                    switch (b) {
                        case "9":
                            rank = 23;
                            handName = "Count Down";
                            break;
                        case "8":
                            rank = 38;
                            handName = "Tetris";
                            break;
                        case "7":
                            rank = 57;
                            handName = "Split";
                            break;
                        case "6":
                            rank = 74;
                            handName = "Driver's License";
                            break;
                        case "5":
                            rank = 93;
                            handName = "Dimestore";
                            break;
                        case "4":
                            rank = 95;
                            handName = "Roger That";
                            break;
                        case "3":
                            rank = 96;
                            handName = "Hot Waitress";
                            break;
                        case "2":
                            rank = 98;
                            handName = "Terminator II";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;        
            case "9":
                switch (b) {
                    case "8":
                        rank = 40;
                        handName = "Oldsmobile";
                        break;
                    case "7":
                        rank = 54;
                        handName = "Grapefruit";
                        break;
                    case "6":
                        rank = 68;
                        handName = "Overtime";
                        break;
                    case "5":
                        rank = 88;
                        handName = "Hard Working";
                        break;
                    case "4":
                        rank = 106;
                        handName = "San Francisco";
                        break;
                    case "3":
                        rank = 107;
                        handName = "Jack Benny";
                        break;
                    case "2":
                        rank = 111;
                        handName = "Twiggy";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;        
            case "8":
                switch (b) {
                    case "7":
                        rank = 48;
                        handName = "RPM";
                        break;
                    case "6":
                        rank = 62;
                        handName = "Jagr";
                        break;
                    case "5":
                        rank = 78;
                        handName = "Finky Dinky";
                        break;
                    case "4":
                        rank = 94;
                        handName = "Big Brother";
                        break;
                    case "3":
                        rank = 116;
                        handName = "Raquel Welch";
                        break;
                    case "2":
                        rank = 118;
                        handName = "Fat Lady & a Duck";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "7":
                switch (b) {
                    case "6":
                        rank = 56;
                        handName = "America";
                        break;
                    case "5":
                        rank = 67;
                        handName = "Heinz 57 Sauce";
                        break;
                    case "4":
                        rank = 85;
                        handName = "Barn Owl";
                        break;
                    case "3":
                        rank = 103;
                        handName = "Dutch Waiter";
                        break;
                    case "2":
                        rank = 120;
                        handName = "Beer";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "6":
                switch (b) {
                    case "5":
                        rank = 63;
                        handName = "Bus Pass";
                        break;
                    case "4":
                        rank = 70;
                        handName = "Revolution";
                        break;
                    case "3":
                        rank = 90;
                        handName = "JFK";
                        break;
                    case "2":
                        rank = 110;
                        handName = "Aimsworth";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "5":
                switch (b) {
                    case "4":
                        rank = 65;
                        handName = "Colt";
                        break;
                    case "3":
                        rank = 77;
                        handName = "Juggernaut";
                        break;
                    case "2":
                        rank = 92;
                        handName = "Quarter";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "4":
                switch (b) {
                    case "3":
                        rank = 84;
                        handName = "Waltz Time";
                        break;
                    case "2":
                        rank = 97;
                        handName = "Lumberjack";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "3":
                switch (b) {
                    case "2":
                        rank = 105;
                        handName = "Jordan";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            default:
                rank = -1;
                handName = "Unknown";
                break;
            }
        } else {                    //Off-suit
            switch (a) {
                case "A":
                    switch (b) {
                        case "A":
                            rank = 1;
                            handName = "Pocket Rockets";
                            break;
                        case "K":
                            rank = 11;    
                            handName = "Big Slick";
                            break;
                        case "Q":
                            rank = 18;
                            handName = "Big Chick";
                            break;
                        case "J":
                            rank = 27;
                            handName = "Ace Jack-off";
                            break;
                        case "10":
                            rank = 42;
                            handName = "Bookends";
                            break;
                        case "9":
                            rank = 76;
                            handName = "Jesus";
                            break;
                        case "8":
                            rank = 91;
                            handName = "Dead Man's Hand";
                            break;
                        case "7":
                            rank = 102;
                            handName = "Red Baron";
                            break;
                        case "6":
                            rank = 113;
                            handName = "Mile High";
                            break;
                        case "5":
                            rank = 101;
                            handName = "High Five";
                            break;
                        case "4":
                            rank = 104;
                            handName = "Crashing Airlines";
                            break;
                        case "3":
                            rank = 109;
                            handName = "Baskin Robbins";
                            break;
                        case "2":
                            rank = 117;
                            handName = "Arizona";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;
                case "K":
                    switch (b) {
                        case "K":
                            rank = 2;
                            handName = "Cowboys";
                            break;
                        case "Q":
                            rank = 20;
                            handName = "Othello";
                            break;
                        case "J":
                            rank = 31;
                            handName = "Kojax";
                            break;
                        case "10":
                            rank = 45;
                            handName = "Woodcutter";
                            break;
                        case "9":
                            rank = 81;
                            handName = "Sawmill";
                            break;
                        case "8":
                            rank = 112;
                            handName = "Kokomo";
                            break;
                        case "7":
                            rank = 122;
                            handName = "King Salmon";
                            break;
                        case "6":
                            rank = 125;
                            handName = "The Concubine";
                            break;
                        case "5":
                            rank = 128;
                            handName = "Rotten Cowboy";
                            break;
                        case "4":
                            rank = 132;
                            handName = "Fork";
                            break;
                        case "3":
                            rank = 133;
                            handName = "Commander Crab";
                            break;
                        case "2":
                            rank = 135;
                            handName = "Big Fritz";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;
                case "Q":
                    switch (b) {
                        case "Q":
                            rank = 3;
                            handName = "West Hollywood";
                            break;
                        case "J":
                            rank = 35;
                            handName = "Fred & Ethel";
                            break;
                        case "10":
                            rank = 49;
                            handName = "Greyhound";
                            break;
                        case "9":
                            rank = 83;
                            handName = "Quinine";
                            break;
                        case "8":
                            rank = 115;
                            handName = "Kuwait";
                            break;
                        case "7":
                            rank = 131;
                            handName = "Computer Hand";
                            break;
                        case "6":
                            rank = 137;
                            handName = "Quix";
                            break;
                        case "5":
                            rank = 141;
                            handName = "Granny Mae";
                            break;
                        case "4":
                            rank = 143;
                            handName = "Housework";
                            break;
                        case "3":
                            rank = 144;
                            handName = "San Francisco Busboy";
                            break;
                        case "2":
                            rank = 146;
                            handName = "The Vesty";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;
                case "J":
                    switch (b) {
                        case "J":
                            rank = 5;
                            handName = "Jokers";
                            break;
                        case "10":
                            rank = 47;
                            handName = "Morgan";
                            break;
                        case "9":
                            rank = 80;
                            handName = "Emergency";
                            break;
                        case "8":
                            rank = 108;
                            handName = "Jeffrey Dalmer";
                            break;
                        case "7":
                            rank = 129;
                            handName = "Dice";
                            break;
                        case "6":
                            rank = 147;
                            handName = "Jack Sikma";
                            break;
                        case "5":
                            rank = 149;
                            handName = "Motown";
                            break;
                        case "4":
                            rank = 152;
                            handName = "Kid Grenade";
                            break;
                        case "3":
                            rank = 153;
                            handName = "Fortran";
                            break;
                        case "2":
                            rank = 155;
                            handName = "Bennifer";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;
                case "10":
                    switch (b) {
                        case "10":
                            rank = 10;
                            handName = "Twenty Miles";
                            break;
                        case "9":
                            rank = 73;
                            handName = "Mobile Hand";
                            break;
                        case "8":
                            rank = 100;
                            handName = "Tetris";
                            break;
                        case "7":
                            rank = 124;
                            handName = "Bowling";
                            break;
                        case "6":
                            rank = 140;
                            handName = "Sweet Sixteen";
                            break;
                        case "5":
                            rank = 157;
                            handName = "Nickels & Dimes";
                            break;
                        case "4":
                            rank = 158;
                            handName = "Over & Out";
                            break;
                        case "3":
                            rank = 160;
                            handName = "Hot Waitress";
                            break;
                        case "2":
                            rank = 162;
                            handName = "Texas Dolly";
                            break;
                        default:
                            rank = -1;
                            handName = "Unknown";
                            break;
                    }
                    break;        
            case "9":
                switch (b) {
                    case "9":
                        rank = 17;
                        handName = "Wayne Gretzky";
                        break;
                    case "8":
                        rank = 99;
                        handName = "Oldsmobile";
                        break;
                    case "7":
                        rank = 119;
                        handName = "Grapefruit";
                        break;
                    case "6":
                        rank = 134;
                        handName = "Percy";
                        break;
                    case "5":
                        rank = 150;
                        handName = "Dolly Parton";
                        break;
                    case "4":
                        rank = 164;
                        handName = "Joe Montana Banana";
                        break;
                    case "3":
                        rank = 165;
                        handName = "Jack Benny";
                        break;
                    case "2":
                        rank = 166;
                        handName = "Twiggy";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;        
            case "8":
                switch (b) {
                    case "8":
                        rank = 21;
                        handName = "Snowmen";
                        break;
                    case "7":
                        rank = 114;
                        handName = "Tahoe";
                        break;
                    case "6":
                        rank = 126;
                        handName = "Maxwell Smart";
                        break;
                    case "5":
                        rank = 139;
                        handName = "The Scag";
                        break;
                    case "4":
                        rank = 156;
                        handName = "George Orwell";
                        break;
                    case "3":
                        rank = 167;
                        handName = "Sven";
                        break;
                    case "2":
                        rank = 168;
                        handName = "Fat Lady & a Duck";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "7":
                switch (b) {
                    case "7":
                        rank = 29;
                        handName = "Saturn";
                        break;
                    case "6":
                        rank = 121;
                        handName = "Union Oil";
                        break;
                    case "5":
                        rank = 130;
                        handName = "Filipino Slick";
                        break;
                    case "4":
                        rank = 145;
                        handName = "Cambodian Slick";
                        break;
                    case "3":
                        rank = 161;
                        handName = "Rusty Trombone";
                        break;
                    case "2":
                        rank = 169;
                        handName = "Death";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "6":
                switch (b) {
                    case "6":
                        rank = 36;
                        handName = "Kicks";
                        break;
                    case "5":
                        rank = 123;
                        handName = "Ken Warren";
                        break;
                    case "4":
                        rank = 136;
                        handName = "The Question";
                        break;
                    case "3":
                        rank = 148;
                        handName = "Blocky";
                        break;
                    case "2":
                        rank = 163;
                        handName = "Bed & Breakfast";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "5":
                switch (b) {
                    case "5":
                        rank = 46;
                        handName = "Presto";
                        break;
                    case "4":
                        rank = 127;
                        handName = "Colt 45";
                        break;
                    case "3":
                        rank = 138;
                        handName = "Bully Johnson";
                        break;
                    case "2":
                        rank = 151;
                        handName = "Quarter";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "4":
                switch (b) {
                    case "4":
                        rank = 50;
                        handName = "Sail Boats";
                        break;
                    case "3":
                        rank = 142;
                        handName = "Waltz Time";
                        break;
                    case "2":
                        rank = 154;
                        handName = "The Answer";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "3":
                switch (b) {
                    case "3":
                        rank = 51;
                        handName = "City Parks";
                        break;
                    case "2":
                        rank = 159;
                        handName = "Can of Corn";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            case "2":
                switch (b) {
                    case "2":
                        rank = 52;
                        handName = "Quackers";
                        break;
                    default:
                        rank = -1;
                        handName = "Unknown";
                        break;
                }
                break;
            default:
                rank = -1;
                handName = "Unknown";
                break;
            }
        }

        ret += "<html><p>";
        ret += "<center>" + handName + "</center>";
        ret += "<br />";
        ret += "<center>Hand Strength : " + rank + " / " + MAX_RANK + "</center>";
        ret += "</html>";

        return ret;
    } 


    /**
     * Determines if two cards have the same suit
     * @param a     1st Card
     * @param b     2nd Card
     * @return      boolean:    true if same suit
     */
    private boolean areSuited(Card a, Card b) {
        return a.getSuit().equals(b.getSuit());
    }

    /**
     * Determines if two cards have the same value
     * @param a     1st Card
     * @param b     2nd Card
     * @return      boolean:    true if same val
     */
    private boolean areRanked(Card a, Card b) {
        return a.getValue().equals(b.getValue());
    }



}
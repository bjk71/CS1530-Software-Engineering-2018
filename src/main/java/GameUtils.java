import java.util.ArrayList;
import java.util.Arrays;

public class GameUtils{
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
  
    public String determineBestHand(Player[] thePlayers, Card[] communityCards, int pot){
        return determineBestHand(thePlayers, communityCards, pot, true);
    }
    
    public String determineBestHand(Player[] thePlayers, Card[] communityCards, int pot, boolean updateDisplay){
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
            allAvailableCards.addAll(Arrays.asList(p.getCards()));
            allAvailableCards.addAll(Arrays.asList(communityCards));

            Card[] hand = new Card[allAvailableCards.size()];
            hand = allAvailableCards.toArray(hand);
            p.setFullHand(hand);
        }

        //Determine best hand
        //Royal/Straight Flush?-------------------------------------------------------------------------
        for(Player p : remainingPlayers){
            suit = containsFlush(p.getFullHand());
            if(!suit.equals("")){
                int highCard = containsStraight(p.getFullHand(), suit);
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

            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Four of a kind?-------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsNOfAKind(p.getFullHand(), 4, 2);
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

            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Full House?-----------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsFullHouse(p.getFullHand());
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
                int[] highCards = determineHighCard(p.getFullHand(), suit);
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

            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Straight?-------------------------------------------------------------------------------------
        highestHighCard = 0;
        for(Player p : remainingPlayers){
            int highCard = containsStraight(p.getFullHand());
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

            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Three of a Kind?------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        highestHighCard3 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsNOfAKind(p.getFullHand(), 3, 3);
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

            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //Two Pair?-------------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        highestHighCard3 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsTwoPair(p.getFullHand());
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

            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }

        //One Pair?-------------------------------------------------------------------------------------
        highestHighCard = 0;
        highestHighCard2 = 0;
        highestHighCard3 = 0;
        highestHighCard4 = 0;
        for(Player p : remainingPlayers){
            int[] highCards = containsNOfAKind(p.getFullHand(), 2, 4);
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
            int[] highCards = determineHighCard(p.getFullHand());
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

            distributePot(playersWithBestHand, thePlayers, pot, updateDisplay);
            return buildResultsString(playersWithBestHand, bestHand);
        }
        
        return "No Winner!!"; //This shoud be impossible
    }

    private int[] determineHighCard(Card[] hand){
        return this.determineHighCard(hand, "");
    }

    //Determines highest value within passed in array of cards
    private int[] determineHighCard(Card[] hand, String suit){
        int[] returnArr = new int[7];
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
                return new int[]{-1, -1, -1, -1, -1};
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
    private int containsStraight(Card[] hand){
        return this.containsStraight(hand, "");
    }

    //Determines if passed in array of cards contains a Straight of specified suit,
    //returns high card value if it does, -1 if not
    private int containsStraight(Card[] hand, String suit){
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

        return returnVal + 1; //To offest the array starting at 0, returning a 10 means high card is 10, not jack
    }

    //Determines if passed in array of cards contains a full house, 
    //returns array containing {three of a kind, two of a kind} if it does, {-1, -1} if not
    private int[] containsFullHouse(Card[] hand){
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

        if(returnArr[0] == -1 || returnArr[1] == -1){
            returnArr[0] = -1;
            returnArr[1] = -1;
        }
        return returnArr;
    }

    //Determines if passed in array of cards contains two pairs, 
    //returns array containing {larger pair, smaller pair} if it does, {-1, -1} if not
    private int[] containsTwoPair(Card[] hand){
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
        }

        return returnArr;
    }

    //Determines if passed in array of cards contains a exactly N of any card, 
    //returns the cards value if it does, -1 if not
    private int[] containsNOfAKind(Card[] hand, int N, int retArrSize){
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
        }

        return returnArr;
    }

    //Makes the display string describing the outcome of the game
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

}
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GameUtilsTest {

    private Player[]  players        = new Player[3];
    private Card[]    tableCards     = new Card[3];
    private GameUtils utils          = new GameUtils();
    private String[]  aiPlayerNames  = utils.getAINames(2, "user");
    private String    returnedResult = null;
    private String    expectedResult = null;

    @Test
    public void testLastManStanding(){
        initPlayers();

        //Player 0 should win because he is the only one still playing the hand
        players[1].setPlayingHand(false);
        players[2].setPlayingHand(false);

        //The Cards that are in on the table
        Card ac = makeCard("AC");
        Card kc = makeCard("KC");
        Card qc = makeCard("QC");
        
        tableCards = new Card[] {ac, kc, qc};

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = players[0].getName() + " has won as the last man Standing!";

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testRoyalFlush(){
        initPlayers();

        //The Cards that make up the hand
        Card ac = makeCard("AC");
        Card kc = makeCard("KC");
        Card qc = makeCard("QC");
        Card jc = makeCard("JC");
        Card c10 = makeCard("10C");

        //The rest of the cards
        Card d7 = makeCard("7D");
        Card h7 = makeCard("7H");
        Card s7 = makeCard("7S");
        Card d5 = makeCard("5D");
        Card h5 = makeCard("5H");
        Card s5 = makeCard("5S");

        //Player 0 should win with royal flush
        tableCards = new Card[] {ac, kc, qc, d7, d5};
        players[0].setCards(new Card[] {jc,c10}, false);
        players[1].setCards(new Card[] {h7,h5}, false);
        players[2].setCards(new Card[] {s7,s5}, false);
        
        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players[0])), "a Royal Flush!!!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testRoyalFlushTie(){
        initPlayers();

        //The Cards that make up the hand
        Card ac = makeCard("AC");
        Card kc = makeCard("KC");
        Card qc = makeCard("QC");
        Card jc = makeCard("JC");
        Card c10 = makeCard("10C");

        //The rest of the cards
        Card d7 = makeCard("7D");
        Card h7 = makeCard("7H");
        Card s7 = makeCard("7S");
        Card d5 = makeCard("5D");
        Card h5 = makeCard("5H");
        Card s5 = makeCard("5S");

        //All players should tie with royal flush
        tableCards = new Card[] {ac, kc, qc, jc, c10};
        players[0].setCards(new Card[] {d7,d5}, false);
        players[1].setCards(new Card[] {h7,h5}, false);
        players[2].setCards(new Card[] {s7,s5}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players)), "a Royal Flush!!!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testStraightFlush(){
        initPlayers();

        //The Cards that make up the hand
        Card ac = makeCard("AC");
        Card c2 = makeCard("2C");
        Card c3 = makeCard("3C");
        Card c4 = makeCard("4C");
        Card c5 = makeCard("5C");

        //The rest of the cards
        Card d7 = makeCard("7D");
        Card h7 = makeCard("7H");
        Card s7 = makeCard("7S");
        Card d8 = makeCard("8D");
        Card h8 = makeCard("8H");
        Card s8 = makeCard("8S");

        //Player 0 should win with straight flush
        tableCards = new Card[] {ac, c2, c3, d7, d8};
        players[0].setCards(new Card[] {c4,c5}, false);
        players[1].setCards(new Card[] {h7,h8}, false);
        players[2].setCards(new Card[] {s7,s8}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players[0])), "a 5's High Straight Flush!!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testStraightFlushTie(){
        initPlayers();

        //The Cards that make up the hand
        Card ac = makeCard("AC");
        Card c2 = makeCard("2C");
        Card c3 = makeCard("3C");
        Card c4 = makeCard("4C");
        Card c5 = makeCard("5C");

        //The rest of the cards
        Card d7 = makeCard("7D");
        Card h7 = makeCard("7H");
        Card s7 = makeCard("7S");
        Card d8 = makeCard("8D");
        Card h8 = makeCard("8H");
        Card s8 = makeCard("8S");

        //All players should tie with a straight flush
        tableCards = new Card[] {ac, c2, c3, c4, c5};
        players[0].setCards(new Card[] {d7,d8}, false);
        players[1].setCards(new Card[] {h7,h8}, false);
        players[2].setCards(new Card[] {s7,s8}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players)), "a 5's High Straight Flush!!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testFourOfAKind(){
        initPlayers();

        //The Cards that make up the hand
        Card d8 = makeCard("8D");
        Card h8 = makeCard("8H");
        Card s8 = makeCard("8S");
        Card c8 = makeCard("8C");
        Card ac = makeCard("AC");

        //The rest of the cards
        Card d7 = makeCard("7D");
        Card h7 = makeCard("7H");
        Card s7 = makeCard("7S");
        Card c2 = makeCard("2C");
        Card c3 = makeCard("3C");
        Card c4 = makeCard("4C");
        
        //Player 0 should win with four 8's due to his kicker (Ace of Clubs)
        tableCards = new Card[] {d8, h8, s8, c8, c2};
        players[0].setCards(new Card[] {d7,ac}, false);
        players[1].setCards(new Card[] {h7,c3}, false);
        players[2].setCards(new Card[] {s7,c4}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players[0])), "four 8's!!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testFourOfAKindTie(){
        initPlayers();

        //The Cards that make up the hand
        Card d8 = makeCard("8D");
        Card h8 = makeCard("8H");
        Card s8 = makeCard("8S");
        Card c8 = makeCard("8C");
        Card ac = makeCard("AC");

        //The rest of the cards
        Card d7 = makeCard("7D");
        Card h7 = makeCard("7H");
        Card s7 = makeCard("7S");
        Card c2 = makeCard("2C");
        Card c3 = makeCard("3C");
        Card c4 = makeCard("4C");

        //All players should tie with four 8's
        tableCards = new Card[] {d8, h8, s8, c8, ac};
        players[0].setCards(new Card[] {d7,c2}, false);
        players[1].setCards(new Card[] {h7,c3}, false);
        players[2].setCards(new Card[] {s7,c4}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players)), "four 8's!!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testFullHouse(){
        initPlayers();

        //The Cards that make up the hand
        Card d8 = makeCard("8D");
        Card h8 = makeCard("8H");
        Card s8 = makeCard("8S");
        Card d7 = makeCard("7D");
        Card h7 = makeCard("7H");

        //The rest of the cards
        Card ck = makeCard("KC");
        Card cj = makeCard("JC");
        Card c9 = makeCard("9C");
        Card c5 = makeCard("5C");
        Card c3 = makeCard("3C");
        Card c2 = makeCard("2C");
        
        //Player[0] should win with a full house, 8's full of 7's
        tableCards = new Card[] {d8, h8, ck, c2, h7};
        players[0].setCards(new Card[] {s8,d7}, false);
        players[1].setCards(new Card[] {cj,c3}, false);
        players[2].setCards(new Card[] {c9,c5}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players[0])), "a full house, 8's full of 7's!!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testFullHouseTie(){
        initPlayers();

        //The Cards that make up the hand
        Card d8 = makeCard("8D");
        Card h8 = makeCard("8H");
        Card s8 = makeCard("8S");
        Card d7 = makeCard("7D");
        Card h7 = makeCard("7H");

        //The rest of the cards
        Card ck = makeCard("KC");
        Card cj = makeCard("JC");
        Card c9 = makeCard("9C");
        Card c5 = makeCard("5C");
        Card c3 = makeCard("3C");
        Card c2 = makeCard("2C");
        
        //All players should tie with a full house, 8's full of 7's
        tableCards = new Card[] {d8, h8, s8, d7, h7};
        players[0].setCards(new Card[] {ck,c2}, false);
        players[1].setCards(new Card[] {cj,c3}, false);
        players[2].setCards(new Card[] {c9,c5}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players)), "a full house, 8's full of 7's!!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testFlush(){
        initPlayers();

        //The Cards that make up the hand
        Card ck = makeCard("KC");
        Card cj = makeCard("JC");
        Card c9 = makeCard("9C");
        Card c5 = makeCard("5C");
        Card c4 = makeCard("4C");

        //The rest of the cards
        Card d8 = makeCard("8D");
        Card h8 = makeCard("8H");
        Card s8 = makeCard("8S");
        Card d7 = makeCard("7D");
        Card c3 = makeCard("3C");
        Card c2 = makeCard("2C");
        
        //Player 2 should win with a king's high flush (because his fifth highest card is a 4, while everyone else has a 3)
        tableCards = new Card[] {ck, cj, c9, c5, c3};
        players[0].setCards(new Card[] {d8,h8}, false);
        players[1].setCards(new Card[] {s8,d7}, false);
        players[2].setCards(new Card[] {c4,c2}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players[2])), "a King's High Flush!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testFlushTie(){
        initPlayers();

        //The Cards that make up the hand
        Card ck = makeCard("KC");
        Card cj = makeCard("JC");
        Card c9 = makeCard("9C");
        Card c5 = makeCard("5C");
        Card c3 = makeCard("3C");

        //The rest of the cards
        Card d8 = makeCard("8D");
        Card h8 = makeCard("8H");
        Card s8 = makeCard("8S");
        Card d7 = makeCard("7D");
        Card h7 = makeCard("7H");
        Card c2 = makeCard("2C");
        
        //All players should tie with a king's high flush
        tableCards = new Card[] {ck, cj, c9, c5, c3};
        players[0].setCards(new Card[] {d8,h8}, false);
        players[1].setCards(new Card[] {s8,d7}, false);
        players[2].setCards(new Card[] {h7,c2}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players)), "a King's High Flush!");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testHighCard(){
        initPlayers();

        //The Cards that make up the hand
        Card sa = makeCard("AS");
        Card ck = makeCard("KC");
        Card dq = makeCard("QD");
        Card hj = makeCard("JH");
        Card d8 = makeCard("8D");

        //The rest of the cards
        Card c9 = makeCard("9C");
        Card h3 = makeCard("3H");
        Card s3 = makeCard("3S");
        Card d2 = makeCard("2D");
        Card h2 = makeCard("2H");
        Card s2 = makeCard("2S");
        
        //Player 0 should with an Ace High, due to his fifth highest card value is a 9, while everyone else has an 8
        tableCards = new Card[] {sa, ck, dq, hj, d8};
        players[0].setCards(new Card[] {c9,d2}, false);
        players[1].setCards(new Card[] {s3,h2}, false);
        players[2].setCards(new Card[] {h3,s2}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players[0])), "an Ace High.");

        assertEquals(expectedResult, returnedResult);
    }

    @Test
    public void testHighCardTie(){
        initPlayers();

        //The Cards that make up the hand
        Card sa = makeCard("AS");
        Card ck = makeCard("KC");
        Card dq = makeCard("QD");
        Card hj = makeCard("JH");
        Card d8 = makeCard("8D");

        //The rest of the cards
        Card c3 = makeCard("3C");
        Card h3 = makeCard("3H");
        Card s3 = makeCard("3S");
        Card d2 = makeCard("2D");
        Card h2 = makeCard("2H");
        Card s2 = makeCard("2S");
        
        //All players should tie with an Ace High.
        tableCards = new Card[] {sa, ck, dq, hj, d8};
        players[0].setCards(new Card[] {c3,d2}, false);
        players[1].setCards(new Card[] {s3,h2}, false);
        players[2].setCards(new Card[] {h3,s2}, false);

        returnedResult = utils.determineBestHand(players, tableCards, 0, false, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players)), "an Ace High.");

        assertEquals(expectedResult, returnedResult);
    }


    ///////////////////////////////////////--HELPER FUNCTIONS--///////////////////////////////////////
    private void initPlayers(){
        for(int i=0; i<players.length; i++){
            Card[] playerHand   = new Card[2];
            int    playerCash   = 1000;
            
            if(i == 0) { // Initialize human player
                players[i]   = new Player("user", playerHand, 0, playerCash, i);
            } else { // Initialize AI player
                String aiName = aiPlayerNames[i - 1];
                players[i]   = new Player(aiName, playerHand, 0, 1000, i);
            }
        }
    }

    private Card makeCard(String cardName){
        try {
            Image temp = ImageIO.read(this.getClass().getResource("/" + cardName + ".png"));
            return new Card(cardName, temp);
        } catch (IOException ioex) {
            System.exit(1);
        }

        return null;
    }

}
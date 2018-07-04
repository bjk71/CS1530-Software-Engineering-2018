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

    //cs = {"AC", "2C", "3C", "4C", "5C", "6C", "7C", "8C", "9C", "10C", "JC", "QC", "KC"};
    //ds = {"AD", "2D", "3D", "4D", "5D", "6D", "7D", "8D", "9D", "10D", "JD", "QD", "KD"};
    //hs = {"AH", "2H", "3H", "4H", "5H", "6H", "7H", "8H", "9H", "10H", "JH", "QH", "KH"};
    //ss = {"AS", "2S", "3S", "4S", "5S", "6S", "7S", "8S", "9S", "10S", "JS", "QS", "KS"};

    @Test
    public void testLastMan(){
        initPlayers();

        players[1].setPlayingHand(false);
        players[2].setPlayingHand(false);

        //The Cards that are in on the table
        Card ac = makeCard("AC");
        Card kc = makeCard("KC");
        Card qc = makeCard("QC");
        
        tableCards = new Card[] {ac, kc, qc};

        returnedResult = utils.determineBestHand(players, tableCards, 0, false);
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

        players[0].setCards(new Card[] {d7,d5});
        players[1].setCards(new Card[] {h7,h5});
        players[2].setCards(new Card[] {s7,s5});
        
        //All players should tie with royal flush
        tableCards = new Card[] {ac, kc, qc, jc, c10};

        returnedResult = utils.determineBestHand(players, tableCards, 0, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players)), "a Royal Flush!!!");

        assertEquals(expectedResult, returnedResult);

        //Player 0 should win with royal flush
        tableCards = new Card[] {ac, kc, qc, d7, d5};
        players[0].setCards(new Card[] {jc,c10});
        
        returnedResult = utils.determineBestHand(players, tableCards, 0, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players[0])), "a Royal Flush!!!");

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

        players[0].setCards(new Card[] {d7,d8});
        players[1].setCards(new Card[] {h7,h8});
        players[2].setCards(new Card[] {s7,s8});
        
        //All players should tie with a straight flush
        tableCards = new Card[] {ac, c2, c3, c4, c5};

        returnedResult = utils.determineBestHand(players, tableCards, 0, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players)), "a 5's High Straight Flush!!");

        assertEquals(expectedResult, returnedResult);

        //Player 0 should win with straight flush
        tableCards = new Card[] {ac, c2, c3, d7, d8};
        players[0].setCards(new Card[] {c4,c5});

        returnedResult = utils.determineBestHand(players, tableCards, 0, false);
        expectedResult = utils.buildResultsString(new ArrayList<Player> (Arrays.asList(players[0])), "a 5's High Straight Flush!!");

        assertEquals(expectedResult, returnedResult);
    }



    ///////////////////////////////////////--HELPER FUNCTIONS--///////////////////////////////////////
    private void initPlayers(){
        for(int i=0; i<players.length; i++){
            Card[] playerHand   = new Card[2];
            int    playerCash   = 1000;
            JLabel _playerCash  = null;
            
            if(i == 0) { // Initialize human player
                players[i]   = new Player("user", playerHand, playerCash, _playerCash, true);
            } else { // Initialize AI player
                String aiName = aiPlayerNames[i - 1];
                players[i]   = new Player(aiName, playerHand, 1000, _playerCash, true);
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
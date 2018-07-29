import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class CommunityCardsPanel extends JPanel{
    private final Color POKER_GREEN = new Color(71, 113, 72);

    private JPanel   _centeredPanel = new JPanel();
    private JLabel[] _cardLabels    = new JLabel[5];
    private Card[]   communityCards = new Card[5];
    private Card     cardBack       = null;
    private boolean  dealtFlop      = false;
    private boolean  dealtTurn      = false;
    private boolean  dealtRiver     = false;

    public CommunityCardsPanel() {
        this(null, null);
    }

    /**
     * Reinitializes Images after loading save file
     */
    public void reinitImages() {
        for (Card card : communityCards){
            card.reinitFace();
        }
    } 

    /**
     * @param communityCards Card array of community cards.
     * @param cardBack       Card object of card back, used for displaying cards facedown. 
     */
    public CommunityCardsPanel(Card[] communityCards, Card cardBack) {
        Image resizedCardBack = cardBack.getFace().getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);

        for(int i = 0; i < 5; i++) {
            this.communityCards[i] = communityCards[i];

            this._cardLabels[i] = new JLabel(new ImageIcon(resizedCardBack));
            this._cardLabels[i].setMinimumSize(new Dimension(100, 140));
        }
        
        this.cardBack = cardBack;

        displayCards();
        showComponents();
    }

    /**
     * Return <b>communityCards[]</b> array.
     */
    public Card[] getCards() {
        return communityCards;
    }

    /**
     * Deal the next cards.
     */
    public void deal() {
        if(!dealtFlop) {
            dealFlop();
            dealtFlop = true;
        } else if(!dealtTurn) {
            dealTurn();
            dealtTurn = true;
        } else if(!dealtRiver) {
            dealRiver();
            dealtRiver = true;
        }
    }

    /**
     * Displays border around cards in the winning hand
     */
    public void showWinningCards(){
        for(int i = 0; i < communityCards.length; i++){
            if(communityCards[i].isInWinningHand()){
                Image resizedCard = null;
                Border raisedbevel = BorderFactory.createRaisedBevelBorder();
                Border loweredbevel = BorderFactory.createLoweredBevelBorder();
                Border greenline = BorderFactory.createLineBorder(POKER_GREEN, 10);
                Border whiteline = BorderFactory.createLineBorder(Color.white, 3);

                Border compound = BorderFactory.createCompoundBorder(raisedbevel, whiteline);
                compound = BorderFactory.createCompoundBorder(compound, loweredbevel);
                compound = BorderFactory.createCompoundBorder(compound, greenline);

                resizedCard = communityCards[i].getFace().getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);
                this._cardLabels[i] = new JLabel(new ImageIcon(resizedCard));
                this._cardLabels[i].setBorder(compound);
            }
        }

        displayCards();
    }

    /* Private methods */

    private void displayCards() {
        this.removeAll();

        for(int i = 0; i < 5; i++) {
            this.add(_cardLabels[i]);
            if(i < 4) {
                this.add(paddingPanel(15));
            }
        }

        this.revalidate();
        this.repaint();
    }

    private void dealFlop() {
        Image[] resizedCards = new Image[3];

        for(int i = 0; i < 3; i++) {
            resizedCards[i] = communityCards[i].getFace().getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);
            this._cardLabels[i] = new JLabel(new ImageIcon(resizedCards[i]));
        }

        displayCards();
    }

    private void dealTurn() {
        Image resizedCard = null;

        resizedCard = communityCards[3].getFace().getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);
        this._cardLabels[3] = new JLabel(new ImageIcon(resizedCard));

        displayCards();
    }

    private void dealRiver() {
        Image resizedCard = null;

        resizedCard = communityCards[4].getFace().getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);
        this._cardLabels[4] = new JLabel(new ImageIcon(resizedCard));

        displayCards();
    }

    
    private void showComponents() {
        this.setLayout(new GridBagLayout());
        this.setBackground(POKER_GREEN);
        this.revalidate();
        this.repaint();
    }

    /**
     * Return POKER_GREEN background JPanel with height and width equal to <b>size</b>.
     * @param size Height and width of padding panel.
     */
    private JPanel paddingPanel(int size) {
        JPanel _padding = new JPanel();

        _padding.setPreferredSize(new Dimension(size, size));
        _padding.setBackground(POKER_GREEN);

        return _padding;
    }
}
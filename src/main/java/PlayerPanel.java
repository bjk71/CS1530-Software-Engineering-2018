import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

// TODO: make font size adjust based on number of players, allow user font size to
// be larger

public class PlayerPanel extends JPanel {
    private final Color POKER_GREEN = new Color(71, 113, 72);
    private final Color WHITE       = Color.WHITE;
    private final Font  COURIER     = new Font("Courier", Font.PLAIN, 22);

    private JPanel  _labelPanel = new JPanel();
    private JPanel  _cardPanel  = new JPanel();

    private JLabel  _nameLabel  = new JLabel();
    private JLabel  _cashLabel  = new JLabel();

    private Card[]  playerHand  = new Card[2];
    private Card    cardBack    = null;
    private String  name        = null;
    private boolean showCards   = false;
    private int     playerRole  = 0;

    /**
     * Initialize PlayerPanel object and variables, then call methods to display
     * components.
     * @param player    Player object being displayed, use to set PlayerPanel variables.
     * @param cardBack  Card object of card back, used for displaying cards facedown.
     * @param showCards Boolean, determines whether to show cards initially.
     */
    public PlayerPanel(Player player, Card cardBack, boolean showCards) {
        setPlayerName(player.getName());
        setPlayerCash(player.getCash());

        this.playerHand[0] = player.getCards()[0];
        this.playerHand[1] = player.getCards()[1];

        this.cardBack   = cardBack;
        this.name       = player.getName();
        this.showCards  = showCards;
        this.playerRole = player.getRole();

        this._nameLabel.setForeground(WHITE);
        this._nameLabel.setFont(COURIER);

        this._cashLabel.setForeground(WHITE);
        this._cashLabel.setFont(COURIER);

        showCards(showCards);

        showComponents();
    }

    /**
     * Set player cash and cash label text.
     * @param cash Integer cash value.
     */
    public int setPlayerCash(int cash) {
        this._cashLabel.setText("$" + cash);

        return cash;
    }

    /**
     * Update stored player card values.
     * @param cards Card array, only stores first two values.
     */
    public Card[] setPlayerCards(Card[] cards) {
        playerHand[0] = cards[0];
        playerHand[1] = cards[1];

        return playerHand;
    }

    /**
     * Set boolean value <b>showCards</b> and then update displayed card images.
     * @param showCards Boolean value to set.
     */
    public void showCards(boolean showCards) {
        this.showCards = showCards;

        displayCards();
    }

    /**
     * Display player as inactive by removed their cash label, cards, and buttons. 
     */
    public void setInactive() {
        this._cardPanel.removeAll();
        this._cardPanel.add(paddingPanel(52));
        this._cardPanel.add(paddingPanel(105));
        this._cashLabel.setText("---");

        this.revalidate();
        this.repaint();
    }

    /* Private Methods */

    /**
     * Display user cards or card backs based on value of <b>showCards</b> variable.
     */
    private void displayCards() {
        JPanel   _bottomPanel = new JPanel();
        JLabel[] _cardLabels  = new JLabel[2];
        Image[]  resizedCards = new Image[2];
        
        if(showCards) {
            resizedCards[0] = playerHand[0].getFace().getScaledInstance(75, 105,  java.awt.Image.SCALE_SMOOTH);
            resizedCards[1] = playerHand[1].getFace().getScaledInstance(75, 105,  java.awt.Image.SCALE_SMOOTH);
        } else {
            resizedCards[0] = cardBack.getFace().getScaledInstance(75, 105, java.awt.Image.SCALE_SMOOTH);
            resizedCards[1] = cardBack.getFace().getScaledInstance(75, 105, java.awt.Image.SCALE_SMOOTH);
        }

        this._cardPanel.removeAll();
        this._cardPanel.setBackground(POKER_GREEN);
        this._cardPanel.setLayout(new BoxLayout(this._cardPanel, BoxLayout.PAGE_AXIS));

        _cardLabels[0] = new JLabel(new ImageIcon(resizedCards[0]));
        _cardLabels[1] = new JLabel(new ImageIcon(resizedCards[1]));

        _cardLabels[0].setMinimumSize(new Dimension(75, 105));
        _cardLabels[1].setMinimumSize(new Dimension(75, 105));

        _bottomPanel.setBackground(POKER_GREEN);
        _bottomPanel.add(_cardLabels[0]);
        _bottomPanel.add(_cardLabels[1]);

        this._cardPanel.add(imagePanel(52, getRoleImage(this.playerRole)));
        this._cardPanel.add(_bottomPanel);

        this.repaint();
        this.revalidate();
    }

    /**
     * Set player name and name label text. Only called in constructor.
     * @param name String name value.
     */
    private void setPlayerName(String name) {
        this._nameLabel.setText(name + ": ");
    }

    /**
     * Add all components to <b>this</b> and repaint. Only called in constructor.
     */
    private void showComponents() {
        this._labelPanel.setBackground(POKER_GREEN);
        this._labelPanel.add(_nameLabel);
        this._labelPanel.add(_cashLabel);

        this.setLayout(new BorderLayout());
        this.setBackground(POKER_GREEN);
        // this.setMinimumSize(new Dimension(1, 200));
        this.add(this._labelPanel, BorderLayout.NORTH);
        this.add(this._cardPanel, BorderLayout.CENTER);
        this.add(paddingPanel(15), BorderLayout.SOUTH);

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

    private Image getRoleImage(int role) {
        Image image = null;

        try {
            switch(role) {
                case 1:
                    image = ImageIO.read(this.getClass().getResource("/Dealer.png"));
                    break;
                case 2:
                    image = ImageIO.read(this.getClass().getResource("/Small-Blind.png"));
                    break;
                case 3:
                    image = ImageIO.read(this.getClass().getResource("/Big-Blind.png"));
                    break;
            }
        } catch (IOException ioex) {
            System.exit(1);
        }

        return image;
    }

    /**
     * Return <b>image</b> background JPanel with height and width equal to <b>size</b>.
     * @param size  Height and width of padding panel.
     * @param image Background image.
     */
    private JPanel imagePanel(int size, Image image) {
        JPanel _imagePanel = new JPanel();
        JLabel _imageLabel = null;

        _imagePanel.setPreferredSize(new Dimension(size, size));
        _imagePanel.setBackground(POKER_GREEN);

        try {
            _imageLabel = new JLabel(new ImageIcon(image));
            _imagePanel.add(_imageLabel);
        } catch(Exception e) {
            // No role image
        }

        return _imagePanel;
    }
}
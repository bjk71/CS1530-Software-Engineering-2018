import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 * Info Panel that holds custom setting information for the
 * user, including Timer, Hand Strength, and Pot Odds
 */
public class InfoPanel extends JPanel {
    private static final Color POKER_GREEN  = new Color(71, 113, 72);
    private static final Color POKER_DARK   = new Color(47, 89, 49);

    private JLabel _timerLabel    = new JLabel();
    private JLabel _relativeLabel = new JLabel();
    private JLabel _oddsLabel     = new JLabel();

    /**
     * Constructor
     */
    public InfoPanel() {

        this.setLayout(new GridLayout(3,1));

        _timerLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 28));
        _timerLabel.setForeground(Color.WHITE);
        _timerLabel.setHorizontalAlignment(JLabel.CENTER);
        _timerLabel.setVerticalAlignment(JLabel.CENTER);

        _relativeLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        _relativeLabel.setForeground(Color.WHITE);
        _relativeLabel.setHorizontalAlignment(JLabel.CENTER);
        _relativeLabel.setVerticalAlignment(JLabel.CENTER);

        _oddsLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        _oddsLabel.setForeground(Color.WHITE);
        _oddsLabel.setHorizontalAlignment(JLabel.CENTER);
        _oddsLabel.setVerticalAlignment(JLabel.CENTER);
        
        this.add(_timerLabel);
        this.add(_relativeLabel);
        this.add(_oddsLabel);
        this.setPreferredSize(new Dimension(250, 350));

        this.revalidate();
        this.repaint();
    }

    /**
     * Paints custom graphic JPanel geometry
     * @param g     Graphics
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        g2d.setColor(POKER_GREEN);
        g2d.fillRect(0, 0, 25, 25);
        g2d.fillRect(0, 325, 25, 25);

        g2d.setColor(POKER_DARK);
        g2d.fillArc(0, 0, 50, 50, 90, 90);
        g2d.fillRect(25, 0, 225, 25);
        g2d.fillRect(0, 25, 250, 300);
        g2d.fillRect(25, 325, 225, 25);
        g2d.fillArc(0, 300, 50, 50, 180, 90);
    }

    /**
     * Sets timer label
     * @param text  New label string
     */
    public void setTimerText(String text) {
        this._timerLabel.setText(text);
    }

    /**
     * Sets pre-flop hand strength label
     * @param text  New label string
     */
    public void setRelativeLabel(String text) {
        this._relativeLabel.setText(text);
    }

    /**
     * Sets pot odds label
     * @param text  New label string
     */
    public void setOddsLabel(String text) {
        this._oddsLabel.setText(text);
    }

    /**
     * Gets timer label
     * @return  JLabel
     */
    public JLabel getTimerLabel() {
        return this._timerLabel;
    }

    /**
     * Gets pre-flop hand strength label
     * @return  JLabel
     */
    public JLabel getRelativeLabel() {
        return this._relativeLabel;
    }

    /**
     * Gets pot odds label
     * @return  JLabel
     */
    public JLabel getOddsLabel() {
        return this._oddsLabel;
    }
}
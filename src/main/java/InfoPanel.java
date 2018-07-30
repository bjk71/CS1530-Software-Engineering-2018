import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.geom.GeneralPath;

public class InfoPanel extends JPanel {
    private static final Color POKER_GREEN  = new Color(71, 113, 72);
    private final Color POKER_DARK   = new Color(47, 89, 49);

    private JLabel _timerLabel    = new JLabel();
    private JLabel _relativeLabel = new JLabel();
    private JLabel _oddsLabel     = new JLabel();

    public InfoPanel() {
        _timerLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 28));
        _timerLabel.setForeground(Color.WHITE);
        _relativeLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        _relativeLabel.setForeground(Color.WHITE);
        _oddsLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        _oddsLabel.setForeground(Color.WHITE);

        this.add(_timerLabel);
        this.add(_relativeLabel);
        this.add(_oddsLabel);
        this.setPreferredSize(new Dimension(250, 350));

        this.revalidate();
        this.repaint();
    }

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

    public void setTimerText(String text) {
        this._timerLabel.setText(text);
    }

    public void setRelativeLabel(String text) {
        this._relativeLabel.setText(text);
    }

    public void setOddsLabel(String text) {
        this._oddsLabel.setText(text);
    }

    public JLabel getTimerLabel() {
        return this._timerLabel;
    }

    public JLabel getRelativeLabel() {
        return this._relativeLabel;
    }

    public JLabel getOddsLabel() {
        return this._oddsLabel;
    }
}
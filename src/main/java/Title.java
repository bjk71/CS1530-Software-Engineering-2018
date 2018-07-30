import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Title extends JPanel {
    private final Color POKER_GREEN  = new Color(71, 113, 72);
    private final Color WHITE        = new Color(255, 255, 255);

    /**
     * Create Title JPanel object.
     */
    public Title() {        
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        
        JLabel _titleLabel = new JLabel();
        JLabel _quoteLabel = new JLabel();
        JLabel _imgLabel   = null;
        JPanel _panel      = new JPanel();

        setLayout(new BorderLayout(20, 20));
        _panel.setLayout(new GridLayout(2, 1, 50, 50));

        try {
            Image img = ImageIO.read(this.getClass().getResource("/royalflush.png"));
            _imgLabel = new JLabel(new ImageIcon(img.getScaledInstance(360, 273, java.awt.Image.SCALE_SMOOTH)));
        } catch (IOException ioex) {
            System.exit(1);
        }

        _imgLabel.setHorizontalAlignment(JLabel.CENTER);
        _imgLabel.setVerticalAlignment(JLabel.BOTTOM);

        _quoteLabel.setForeground(WHITE);
        _quoteLabel.setFont(new Font("Courier", Font.ITALIC, 36));
        _quoteLabel.setHorizontalAlignment(JLabel.CENTER);
        _quoteLabel.setVerticalAlignment(JLabel.TOP);
        _quoteLabel.setText("\"If you can't beat them, bluff them\"");
        _panel.add(_imgLabel);
        _panel.add(_quoteLabel);

        _titleLabel.setForeground(WHITE);
        _titleLabel.setFont(new Font("Courier", Font.PLAIN, 60));
        _titleLabel.setHorizontalAlignment(JLabel.CENTER);
        _titleLabel.setVerticalAlignment(JLabel.BOTTOM);
        _titleLabel.setText("Welcome to Pocket Rockets Poker!");

        setBackground(POKER_GREEN);
        _panel.setBackground(POKER_GREEN);
        add(_titleLabel, BorderLayout.PAGE_START);
        add(_panel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

}
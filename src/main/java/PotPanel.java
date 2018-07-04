import java.awt.*;
import javax.swing.*;

    // TODO: handle side pots with additional labels and logic

public class PotPanel extends JPanel {
    private final Color POKER_GREEN = new Color(71, 113, 72);
    private final Color WHITE       = Color.WHITE;
    private final Font  COURIER     = new Font("Courier", Font.PLAIN, 22);

    private JLabel _potLabel = new JLabel();
    private int    value = 0;

    /**
     * Initialize pot <b>value</b> to zero.
     */
    public PotPanel() {
        this(0);
    }

    /**
     * Initialize pot with starting value, then update label and create components.
     * @param value Dollar value to initialize pot to.
     */
    public PotPanel(int value) {
        this.value = value;

        updateLabel();
        showComponent();
    }

    /**
     * Update pot by positive or negative value <b>change</b>.
     * @return New pot total.
     */
    public int adjustPot(int change) {
        this.value += change;

        updateLabel();

        return this.value;
    }

    /**
     * Set pot <b>value</b> to zero and return amount cleared.
     * @return Integer value of total pot from last round.
     */
    public int clearPot() {
        int total = this.value;

        this.value = 0;

        updateLabel();

        return total;
    }

    /* Private methods */

    /**
     * Update pot label text.
     */
    private void updateLabel() {
        this._potLabel.setText("Pot: $" + this.value);

        this.add(_potLabel);
    }

    /**
     * Initialize and add components.
     */
    private void showComponent() {
        this._potLabel.setBackground(POKER_GREEN);
        this._potLabel.setForeground(WHITE);
        this._potLabel.setFont(COURIER);

        this.setBackground(POKER_GREEN);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(500, Integer.MAX_VALUE));
        this.add(this._potLabel);

        this.revalidate();
        this.repaint();
    }
}
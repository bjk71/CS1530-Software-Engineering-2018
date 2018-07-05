import java.awt.*;
import javax.swing.*;

    // TODO: handle side pots with additional labels and logic

public class PotPanel extends JPanel {
    private final Color POKER_GREEN = new Color(71, 113, 72);
    private final Color WHITE       = Color.WHITE;
    private final Font  COURIER     = new Font("Courier", Font.PLAIN, 22);
    private final int   MAXIMUM_SIDEPOTS = 7;

    private JLabel _potLabel[] = new JLabel[MAXIMUM_SIDEPOTS];
    private int[]  value       = new int[MAXIMUM_SIDEPOTS];

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
        this.value[0] = value;
        _potLabel[0] = new JLabel();
        updateLabel(0);
        showComponent(0);
    }

    public void addPot(int num, int value){
        this.value[num] = value;
        _potLabel[num] = new JLabel();
        updateLabel(num);
        showComponent(num);
    }

    /**
     * Update pot by positive or negative value <b>change</b>.
     * @return New pot total.
     */
    public int adjustPot(int num, int change) {
        this.value[num] += change;

        updateLabel(num);

        return this.value[num];
    }

    /**
     * Set pot <b>value</b> to zero and return amount cleared.
     * @return Integer value of total pot from last round.
     */
    public int clearPot(int num) {
        int total = this.value[num];

        this.value[num] = 0;

        updateLabel(num);

        return total;
    }

    /* Private methods */

    /**
     * Update pot label text.
     */
    private void updateLabel(int num) {
        if (num == 0)  
            this._potLabel[num].setText("Pot: $" + this.value[num] + "   ");
        else 
            this._potLabel[num].setText("Side Pot: $" + this.value[num] + "   ");

        this.add(_potLabel[num]);
    }

    /**
     * Initialize and add components.
     */
    private void showComponent(int num) {
        this._potLabel[num].setBackground(POKER_GREEN);
        this._potLabel[num].setForeground(WHITE);
        this._potLabel[num].setFont(COURIER);

        this.setBackground(POKER_GREEN);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(500, Integer.MAX_VALUE));
        this.add(this._potLabel[num]);

        this.revalidate();
        this.repaint();
    }
}
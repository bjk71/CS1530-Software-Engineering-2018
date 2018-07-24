import java.awt.*;
import javax.swing.*;

    // TODO: handle side pots with additional labels and logic

public class PotPanel extends JPanel {
    private final Color POKER_GREEN = new Color(71, 113, 72);
    private final Color WHITE       = Color.WHITE;
    private final Font  COURIER     = new Font("Courier", Font.PLAIN, 22);
    private final int   MAXIMUM_SIDEPOTS = 8;

    private JLabel[] _potLabel = new JLabel[MAXIMUM_SIDEPOTS];
    private int[]    value     = new int[MAXIMUM_SIDEPOTS];

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
        this._potLabel[0] = new JLabel();
        this.value[0]     = value;

        updateLabel(0);
        showComponent();
    }

    public void setPots(int[] pots) {
        for(int i = 0; i < pots.length; i++) {
            if(pots[i] > 0) {
                this._potLabel[i] = new JLabel();
                this.value[i]     = pots[i];
                updateLabel(i);
            } else {
                this._potLabel[i] = null;
                this.value[i]     = 0;
            }
        }

        showComponent();
    }

    public void addPot(int num, int value){
        this._potLabel[num] = new JLabel();
        this.value[num]     = value;

        updateLabel(num);
        showComponent();
    }

    /**
     * Update pot by positive or negative value <b>change</b>.
     * @return New pot total.
     */
    public int adjustPot(int num, int change) {
        try {
            this.value[num] += change;

            updateLabel(num);

        } catch(Exception e) {}

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
     * Update pot label text for pot at index <b>num</b>.
     */
    private void updateLabel(int num) {
        if(num == 0) {
            this._potLabel[num].setText("Pot: $" + this.value[num] + "   ");
        } else {
            this._potLabel[num].setText("Side Pot " + num + ": $" + this.value[num] + "   ");
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * Initialize and add components.
     */
    private void showComponent() {
        GridBagConstraints constraints = new GridBagConstraints();

        this.setBackground(POKER_GREEN);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(500, Integer.MAX_VALUE));
        this.removeAll();

        for(int i = 0; i < _potLabel.length; i++) {
            try {
                this._potLabel[i].setBackground(POKER_GREEN);
                this._potLabel[i].setForeground(WHITE);
                this._potLabel[i].setFont(COURIER);   
                constraints.gridy = i;     
                this.add(this._potLabel[i], constraints);
            } catch (Exception e) {}
        }

        this.revalidate();
        this.repaint();
    }
}
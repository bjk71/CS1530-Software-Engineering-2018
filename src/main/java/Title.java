import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class Title extends JPanel {

    /**
     * Create generic Title JPanel object.
     */
    public Title() {
        JLabel _sampleLabel = new JLabel("Title");
        
        _sampleLabel.setBackground(Color.gray);

        setBackground(Color.gray);
        setVisible(true);

        add(_sampleLabel);
    }
}
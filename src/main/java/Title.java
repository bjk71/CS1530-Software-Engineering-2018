import java.awt.*;
import javax.swing.*;

public class Title extends JPanel {

    public Title() {
        JLabel _sampleLabel = new JLabel("Title");
        
        _sampleLabel.setBackground(Color.gray);

        setBackground(Color.gray);
        setVisible(true);

        add(_sampleLabel);
    }
}
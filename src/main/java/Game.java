import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Game extends JPanel {

    private final Color pokerGreen = new Color(71, 113, 72);

    public Game() {
        JLabel _sampleLabel = new JLabel("Game");

        _sampleLabel.setBackground(pokerGreen);

        setBackground(pokerGreen);
        setVisible(true);

        add(_sampleLabel);
    }

    public Game(File saveFile) {
        // initialize game settings from save file
    }
}
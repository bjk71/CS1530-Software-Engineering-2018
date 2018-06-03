import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Game extends JPanel {

    private final Color pokerGreen = new Color(71, 113, 72);

    public Game() {
        JTextArea _sampleTextArea = new JTextArea();
        _sampleTextArea.setText("Game");
        _sampleTextArea.setBackground(pokerGreen);

        setBackground(pokerGreen);
        setVisible(true);
        setSize(300, 300);

        add(_sampleTextArea);
    }

    public Game(File saveFile) {
        // initialize game settings from save file
    }
}
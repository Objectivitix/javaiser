import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    // "global" variable so WelcomeScreen can stop this
    // music when "BEGIN" button there is clicked
    public static Clip music;

    public Main() {
        // set JFrame's title
        super("The Javaiser");

        // begin music and save it to "global" variable
        music = Utils.playSound("epic.wav");

        // prevent resizing of window
        setResizable(false);

        // center top-level JPanels (*Screen.java)
        setLayout(new GridLayout());

        // close program if user closes window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // start with a WelcomeScreen
        add(new WelcomeScreen());

        // automatically resize JFrame to contents
        pack();

        // center window with respect to viewport
        setLocationRelativeTo(null);

        // make everything visible
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}

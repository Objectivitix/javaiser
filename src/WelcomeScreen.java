import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends Screen implements ActionListener {
    // buttons accessed by multiple instance methods
    private JButton begin;
    private JButton about;

    public WelcomeScreen() {
        // initialize with border layout and padding around
        // the actual content (that's what EmptyBorder is for)
        super();
        setLayout(new BorderLayout(0, 50));
        setBorder(new EmptyBorder(20, 50, 60, 50));

        // top part of the layout, containing welcome and some hero text;
        // setOpaque(false) so the inherited BG color from Screen shines through
        JPanel top = new JPanel(new BorderLayout(0, -15));
        top.setOpaque(false);
        add(top, BorderLayout.NORTH);

        // big welcome title with our resplendent logo
        JLabel welcome = new JLabel("You enter the");
        welcome.setForeground(Utils.TEXT_CLR_HIGH_CONTRAST);
        welcome.setFont(new Font("Rasa", Font.BOLD, 72));
        welcome.setIcon(Utils.icon("javaiser.png", 700, 306));

        // logo has too much whitespace at top part,
        // so bring it closer to "You enter the" using negative gap
        welcome.setIconTextGap(-180);

        // set text position relative to icon, as well as alignment
        welcome.setHorizontalTextPosition(SwingConstants.CENTER);
        welcome.setVerticalTextPosition(SwingConstants.TOP);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcome.setVerticalAlignment(SwingConstants.TOP);

        // finally, add welcome to the top part of our top panel
        top.add(welcome, BorderLayout.NORTH);

        // now for the description. It's a lot less work, just font & center :P
        JLabel desc = new JLabel(Utils.center(
            "Think you know Java?\nHere's 10 programming puzzles.\n"
            + "How far can you go?"
        ));
        desc.setForeground(Utils.TEXT_CLR);
        desc.setFont(new Font("", Font.PLAIN, 36));
        desc.setHorizontalAlignment(SwingConstants.CENTER);
        desc.setVerticalAlignment(SwingConstants.TOP);
        top.add(desc, BorderLayout.CENTER);

        // middle part of the layout, containing two big buttons
        JPanel middle = new JPanel(new GridLayout(2, 1, 0, 25));
        middle.setOpaque(false);
        add(middle, BorderLayout.CENTER);

        // create about button, leading to extended instructions
        about = new JButton("HOW THIS WORKS");
        about.setFont(new Font("", Font.ITALIC, 28));
        about.setIcon(Utils.icon("about.png", 60, 60));
        about.setIconTextGap(17);
        about.addActionListener(this);
        middle.add(about);

        // create begin button, leading to level 0
        begin = new JButton("BEGIN");
        begin.setFont(new Font("", Font.ITALIC, 28));
        begin.setIcon(Utils.icon("begin.png", 60, 60));
        begin.setIconTextGap(15);
        begin.addActionListener(this);
        middle.add(begin);

        // create footer label (grayer and smaller text than the rest)
        JLabel footer = new JLabel(
            "a Calo Zheng experience | ICS4U 24-25 #1"
        );
        footer.setForeground(Utils.TEXT_CLR_LOW_CONTRAST);
        footer.setFont(new Font("", Font.PLAIN, 18));
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == begin) {
            // if epic welcome music is playing, stop it
            if (Main.music != null) {
                Main.music.stop();
                Main.music = null;
            }

            replaceWith(new LevelScreen(0));
        }

        if (e.getSource() == about) {
            replaceWith(new AboutScreen());
        }
    }
}

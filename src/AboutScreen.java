import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutScreen extends Screen implements ActionListener {
    // button accessed by multiple instance methods
    private final JButton back;

    public AboutScreen() {
        // initialize with border layout and padding around
        // the actual content (that's what EmptyBorder is for)
        super();
        setLayout(new BorderLayout(0, 40));
        setBorder(new EmptyBorder(20, 50, 60, 50));

        // top part of the layout, containing title and app instructions;
        // setOpaque(false) so the inherited BG color from Screen shines through
        JPanel top = new JPanel(new BorderLayout(0, -10));
        top.setOpaque(false);
        add(top, BorderLayout.NORTH);

        // create title with logo, exactly like the one in
        // WelcomeScreen.java; see detailed comments there
        JLabel title = new JLabel("How to play");
        title.setForeground(Utils.TEXT_CLR_HIGH_CONTRAST);
        title.setFont(new Font("Rasa", Font.BOLD, 72));
        title.setIcon(Utils.icon("javaiser.png", 700, 306));
        title.setIconTextGap(-180);
        title.setHorizontalTextPosition(SwingConstants.CENTER);
        title.setVerticalTextPosition(SwingConstants.TOP);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.TOP);
        top.add(title, BorderLayout.NORTH);

        // game instructions -- they also belong in the top part of layout
        JLabel body = new JLabel(Utils.html(
        "In Javaiser, you write code to solve increasingly difficult programming puzzles. "
            + "Your code is tested against pre-determined input and expected output, and must "
            + "pass all cases to beat a level.\n\n"
            + "When you code, consider yourself already inside a class. So you need "
            + "only write methods. Your primary method (the one that actually "
            + "returns the answer) must be public static, be named \"solution\", and "
            + "match each level's required signature EXACTLY. "
            + "Also, consider java.util.* already imported.\n\n"
            + "Test your skills and have fun -- good luck!"
        ));
        body.setForeground(Utils.TEXT_CLR);
        body.setFont(new Font("", Font.PLAIN, 26));
        top.add(body, BorderLayout.CENTER);

        // create back button with set height to constrain the rest
        back = new JButton("GO BACK");
        back.setFont(new Font("", Font.ITALIC, 28));
        back.setPreferredSize(new Dimension(80, 80));
        back.addActionListener(this);
        add(back, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            replaceWith(new WelcomeScreen());
        }
    }
}

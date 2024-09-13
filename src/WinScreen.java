import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WinScreen extends Screen implements ActionListener {
    // button accessed by multiple instance methods
    private final JButton backToWelcome;

    public WinScreen() {
        // initialize with border layout and padding around
        // the actual content (that's what EmptyBorder is for)
        super();
        setLayout(new BorderLayout(0, 30));
        setBorder(new EmptyBorder(90, 50, 60, 50));

        // create congratulatory header
        JLabel win = new JLabel("Hey, not bad.");
        win.setForeground(Utils.TEXT_CLR_HIGH_CONTRAST);
        win.setFont(new Font("Rasa", Font.BOLD, 72));
        win.setHorizontalAlignment(SwingConstants.CENTER);
        add(win, BorderLayout.NORTH);

        // middle part of layout, containing some remarks on success
        // and a thanks-for-playing note; setOpaque(false) so the
        // inherited BG color from Screen shines through
        JPanel middle = new JPanel(new BorderLayout(0, 20));
        middle.setOpaque(false);
        add(middle, BorderLayout.CENTER);

        // create success remark with approval gif
        JLabel remark = new JLabel("You solved all 10 puzzles!");
        remark.setForeground(Utils.TEXT_CLR);
        remark.setFont(new Font("", Font.PLAIN, 36));
        remark.setIcon(Utils.gif("nod.gif", 600, 335));
        remark.setIconTextGap(25);
        remark.setHorizontalTextPosition(SwingConstants.CENTER);
        remark.setVerticalTextPosition(SwingConstants.BOTTOM);
        remark.setHorizontalAlignment(SwingConstants.CENTER);
        middle.add(remark, BorderLayout.NORTH);

        // create final remark with smaller font
        JLabel thank = new JLabel(Utils.center(
        "Now you can probably ace Mr Lauder's Unit 1 test " +
            "(maybe Unit 2 also). Thanks for playing the Javaiser."
        ));
        thank.setForeground(Utils.TEXT_CLR);
        thank.setFont(new Font("", Font.PLAIN, 24));
        thank.setHorizontalAlignment(SwingConstants.CENTER);
        middle.add(thank, BorderLayout.CENTER);

        // create back button with set height to constrain the rest
        backToWelcome = new JButton("BACK TO MAIN MENU");
        backToWelcome.setPreferredSize(new Dimension(80, 80));
        backToWelcome.setFont(new Font("", Font.ITALIC, 28));
        backToWelcome.addActionListener(this);
        add(backToWelcome, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToWelcome) {
            replaceWith(new WelcomeScreen());
        }
    }
}

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.util.Objects;

public class Utils {
    // the app's color scheme
    public static final Color BG_CLR = new Color(0, 51, 58);
    public static final Color CODE_BG_CLR = new Color(0, 24, 30);
    public static final Color TEXT_CLR_HIGH_CONTRAST = new Color(222, 222, 222);
    public static final Color TEXT_CLR = new Color(212, 212, 212);
    public static final Color TEXT_CLR_LOW_CONTRAST = new Color(152, 152, 152);

    // creates an ImageIcon of specified dimensions, using getResource so our
    // app works in all forms (e.g. in a JAR file). Does not work for GIFs.
    public static ImageIcon icon(String fileName, int width, int height) {
        return new ImageIcon(
            new ImageIcon(Objects.requireNonNull(Utils.class.getResource("/resources/" + fileName)))
            .getImage()
            .getScaledInstance(width, height, Image.SCALE_SMOOTH)
        );
    }

    // same thing as `icon`, but for GIFs. Image.SCALE_SMOOTH doesn't support
    // them so we forgo a bit of quality and opt for Image.SCALE_DEFAULT.
    public static ImageIcon gif(String fileName, int width, int height) {
        return new ImageIcon(
            new ImageIcon(Objects.requireNonNull(Utils.class.getResource("/resources/" + fileName)))
                .getImage()
                .getScaledInstance(width, height, Image.SCALE_DEFAULT)
        );
    }

    // creates and "sizes" an empty "square" JPanel
    // for convenient placement in border layouts
    public static JPanel spacer(int space) {
        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(space, space));
        return spacer;
    }

    // make any given text center-aligned and multiline,
    // for use in a JLabel, using styled html tags
    public static String center(String text) {
        return "<html><div style='text-align: center'>"
            + text.replaceAll("\n", "<br>") + "</div></html>";
    }

    // make any given text multiline-wrappable, for use
    // in a JLabel, using html tags
    public static String html(String text) {
        return "<html>"
            + text.replaceAll("\n", "<br>") + "</html>";
    }

    // play any .wav file non-blockingly, and return its
    // resource object (Clip) so the caller can stop it
    // before it naturally finished playing, if needed
    public static Clip playSound(String fileName) {
        try {
            // read the .wav file
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(
                Objects.requireNonNull(Utils.class.getResourceAsStream("/resources/" + fileName))));
            clip.open(inputStream);

            // play the clip and return it
            clip.start();
            return clip;
        } catch (Exception e) {
            // catch clause is only here to satisfy the compiler
            throw new RuntimeException(e);
        }
    }
}

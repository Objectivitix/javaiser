import javax.swing.*;
import java.awt.*;

// a top-level screen to facilitate switching and positioning
// (by setting size at the panel level, null layout positions
// are much more accurate, as the window header is excluded)
public class Screen extends JLayeredPane {
    public Screen() {
        super();
        setPreferredSize(new Dimension(800, 900));

        // set BG color and make it visible
        setBackground(Utils.BG_CLR);
        setOpaque(true);
    }

    public void replaceWith(Screen other) {
        // remove this child and add the other screen
        Container root = getParent();
        root.remove(this);
        root.add(other);

        // update display
        root.revalidate();
        root.repaint();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class DVDPanel extends JPanel implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 450;
    private static final int LOGO_WIDTH = 160;
    private static final int LOGO_HEIGHT = 72;

    // current position of logo
    private int x, y;

    // x and y velocities
    private int xv = 1;
    private int yv = 1;

    // Image object representing the logo, to be repainted every 10 ms
    private BufferedImage logo;
    private Color currentColor;

    public DVDPanel() {
        super();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // load the DVD logo into a BufferedImage
        logo = toBufferedImage(Utils.icon("dvd.png", LOGO_WIDTH, LOGO_HEIGHT).getImage());

        // double buffering to reduce flicker
        setDoubleBuffered(true);

        // set initial position to be at the center of panel
        x = WIDTH / 2 - LOGO_WIDTH / 2;
        y = HEIGHT / 2 - LOGO_HEIGHT / 2;

        // start with white logo
        currentColor = Color.WHITE;

        // set up a timer to call actionPerformed every 10 ms
        Timer timer = new Timer(10, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // convert to Graphics2D for more rendering options
        Graphics2D g2D = (Graphics2D) g;

        // enable antialiasing for smoother rendering (especially text)
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // clear background by filling the panel with black
        g2D.setColor(Color.BLACK);
        g2D.fillRect(0, 0, WIDTH, HEIGHT);

        // set up font properties for the text
        g2D.setFont(new Font("", Font.PLAIN, 18));
        g2D.setColor(Color.DARK_GRAY);

        // define the multiline text (split by newlines)
        String[] lines = {"C.Z.", "ICS4U 24-25 #1A"};

        // calculate the total height of the text block
        FontMetrics fm = g2D.getFontMetrics();
        int lineHeight = fm.getHeight();
        int totalTextHeight = lines.length * lineHeight;

        // calculate the starting Y position to center the text block vertically
        int startY = (HEIGHT - totalTextHeight) / 2 + fm.getAscent();

        // draw each line centered horizontally and with vertical offset
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int textX = (WIDTH - fm.stringWidth(line)) / 2;
            int textY = startY + i * lineHeight;
            g2D.drawString(line, textX, textY);
        }

        // create a new BufferedImage with same dimensions as logo, with transparency support...
        BufferedImage coloredLogo = new BufferedImage(LOGO_WIDTH, LOGO_HEIGHT, BufferedImage.TYPE_INT_ARGB);

        // ...and get its Graphics2D context for further manipulation
        Graphics2D logoG2D = coloredLogo.createGraphics();

        // draw the original logo onto the new image
        logoG2D.drawImage(logo, 0, 0, LOGO_WIDTH, LOGO_HEIGHT, null);

        // apply color filter over the logo without affecting transparency
        logoG2D.setComposite(AlphaComposite.SrcAtop);
        logoG2D.setColor(currentColor);
        logoG2D.fillRect(0, 0, LOGO_WIDTH, LOGO_HEIGHT);

        // dispose graphics context to free up resources
        logoG2D.dispose();

        // finally, draw the colored DVD logo at its current position
        g2D.drawImage(coloredLogo, x, y, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // move the logo
        x += xv;
        y += yv;

        // bounce off the edges of the window
        if (x <= 0 || x >= WIDTH - LOGO_WIDTH) {
            // reverse X direction and change color
            xv = -xv;
            currentColor = getRandomColor();
        }
        if (y <= 0 || y >= HEIGHT - LOGO_HEIGHT) {
            // reverse Y direction and change color
            yv = -yv;
            currentColor = getRandomColor();
        }

        // redraw the panel
        repaint();
    }

    // used to change to a random color on bounce;
    // we ensure generated color is not too dark by
    // making each channel have a minimum value of 50
    private Color getRandomColor() {
        return new Color(
            (int)(Math.random() * 205 + 50),
            (int)(Math.random() * 205 + 50),
            (int)(Math.random() * 205 + 50)
        );
    }

    // we must convert to BufferedImage for images to work with
    // graphics objects (and to ensure smooth animation)
    private static BufferedImage toBufferedImage(Image image) {
        // if we can simply cast, do so
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // create new BufferedImage with same dimensions,
        // making sure to include alpha channel in imageType
        BufferedImage bi = new BufferedImage(
            image.getWidth(null),
            image.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );

        // draw original image on BufferedImage
        Graphics g = bi.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        // then return it
        return bi;
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class RotatingRectangle extends JPanel implements Runnable {
    private double angle = 0; 
    private final int WIDTH = 400;
    private final int HEIGHT = 400;

    public RotatingRectangle() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        new Thread(this).start(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int rectWidth = 300;
        int rectHeight = 150;

        AffineTransform old = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate(centerX, centerY); 
        transform.rotate(Math.toRadians(angle)); 
        g2d.setTransform(transform);

        g2d.setColor(Color.BLUE);
        g2d.fill(new Rectangle2D.Double(-rectWidth / 2, -rectHeight / 2, rectWidth, rectHeight));

        g2d.setTransform(old); 
    }

    @Override
    public void run() {
        while (true) {
            angle += 1; 
            if (angle >= 360) {
                angle = 0;
            }
            repaint(); 
            try {
                Thread.sleep(20); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rotating Rectangle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new RotatingRectangle());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

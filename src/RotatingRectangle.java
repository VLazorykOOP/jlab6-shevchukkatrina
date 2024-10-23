import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class RotatingRectangle extends JPanel implements Runnable {
    private double angle = 0; // Кут обертання
    private final int WIDTH = 400;
    private final int HEIGHT = 400;

    public RotatingRectangle() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        new Thread(this).start(); // Запуск потоку для анімації
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Центр панелі
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Розміри чотирикутника
        int rectWidth = 300;
        int rectHeight = 150;

        // Застосування обертання
        AffineTransform old = g2d.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate(centerX, centerY); // Переміщуємо до центру
        transform.rotate(Math.toRadians(angle)); // Обертаємо
        g2d.setTransform(transform);

        // Малювання чотирикутника
        g2d.setColor(Color.BLUE);
        g2d.fill(new Rectangle2D.Double(-rectWidth / 2, -rectHeight / 2, rectWidth, rectHeight));

        g2d.setTransform(old); // Повертаємо стару трансформацію
    }

    @Override
    public void run() {
        while (true) {
            angle += 1; // Збільшуємо кут обертання
            if (angle >= 360) {
                angle = 0;
            }
            repaint(); // Оновлюємо панель
            try {
                Thread.sleep(20); // Затримка для плавності анімації
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
        frame.setLocationRelativeTo(null); // Відцентровуємо фрейм
        frame.setVisible(true);
    }
}

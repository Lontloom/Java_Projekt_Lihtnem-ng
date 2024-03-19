import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlappyBird extends JFrame implements ActionListener, KeyListener {
    private Timer timer;
    private int birdY, velocity, gravity;
    private boolean isGameOver;
    private int[] pipeX, pipeY;
    private final int pipeGap = 200;
    private final int pipeWidth = 80;
    private final int pipeHeight = 400;
    private final int pipeDistance = 300;
    private final int birdWidth = 40;
    private final int birdHeight = 30;

    public FlappyBird() {
        setTitle("Flappy Bird");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addKeyListener(this);

        timer = new Timer(20, this);
        timer.start();

        birdY = 250;
        velocity = 0;
        gravity = 2;

        pipeX = new int[3];
        pipeY = new int[3];
        resetPipes();

        isGameOver = false;
    }

    public void resetPipes() {
        int startX = 400;
        for (int i = 0; i < pipeX.length; i++) {
            pipeX[i] = startX + i * pipeDistance;
            pipeY[i] = (int) (Math.random() * (getHeight() - pipeGap - pipeHeight));
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.GREEN);
        for (int i = 0; i < pipeX.length; i++) {
            g.fillRect(pipeX[i], 0, pipeWidth, pipeY[i]);
            g.fillRect(pipeX[i], pipeY[i] + pipeGap, pipeWidth, getHeight() - pipeY[i] - pipeGap);
        }

        g.setColor(Color.RED);
        g.fillRect(150, birdY, birdWidth, birdHeight);

        if (isGameOver) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over!", 250, getHeight() / 2);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            for (int i = 0; i < pipeX.length; i++) {
                pipeX[i] -= 5;
                if (pipeX[i] + pipeWidth < 0) {
                    pipeX[i] = getWidth();
                    pipeY[i] = (int) (Math.random() * (getHeight() - pipeGap - pipeHeight));
                }
            }

            velocity += gravity;
            birdY += velocity;

            if (birdY > getHeight() - birdHeight || birdY < 0) {
                gameOver();
            }

            for (int i = 0; i < pipeX.length; i++) {
                if (birdY < pipeY[i] || birdY + birdHeight > pipeY[i] + pipeGap) {
                    if (150 + birdWidth > pipeX[i] && 150 < pipeX[i] + pipeWidth) {
                        gameOver();
                    }
                }
            }

            repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE && !isGameOver) {
            velocity = -20;
        }
    }

    public void keyTyped(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    public void gameOver() {
        isGameOver = true;
        timer.stop();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FlappyBird().setVisible(true);
            }
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame {
    public Main() {
        setTitle("Veerev Mandariin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        pack();
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private boolean mängLäbi;
    private Mandariin mandariin;
    private Takistus takistus;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.CYAN);

        mandariin = new Mandariin(200, 2, 0, 30);
        takistus = new Takistus();
        takistus.setTakistusX(new int[3]);
        takistus.setTakistusY(new int[3]);

        timer = new Timer(20, this);
        timer.start();

        resetTakistused();
        mängLäbi = false;

        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        for (int i = 0; i < takistus.getTakistusX().length; i++) {
            g.fillRect(takistus.getTakistusX()[i], 0, takistus.getTakistuseLaius(), takistus.getTakistusY()[i]);
            g.fillRect(takistus.getTakistusX()[i], takistus.getTakistusY()[i] + takistus.getTakistuseVahe(), takistus.getTakistuseLaius(), getHeight() - takistus.getTakistusY()[i] - takistus.getTakistuseVahe());
        }

        g.setColor(Color.ORANGE);
        int mängijaX = 150;
        int mängijaY = mandariin.getMängijaAsukoht();
        int mängijaDiameeter = mandariin.getMängijaDiameeter();
        g.fillOval(mängijaX, mängijaY, mängijaDiameeter, mängijaDiameeter);

        g.setColor(Color.BLACK);
        int täpiDiameeter = 5;
        int täpiX = mängijaX + mängijaDiameeter / 2 - täpiDiameeter / 2;
        int täpiY = mängijaY - täpiDiameeter / 2 + 3;
        g.fillOval(täpiX, täpiY, täpiDiameeter, täpiDiameeter);

        if (mängLäbi) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            FontMetrics fontMetrics = g.getFontMetrics();
            int tekstiLaius = fontMetrics.stringWidth("Mäng Läbi!");
            int tekstiKõrgus = fontMetrics.getHeight();
            int x = (getWidth() - tekstiLaius) / 2;
            int y = (getHeight() - tekstiKõrgus) / 2 + fontMetrics.getAscent();
            g.drawString("Mäng Läbi!", x, y);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!mängLäbi) {
            for (int i = 0; i < takistus.getTakistusX().length; i++) {
                takistus.getTakistusX()[i] -= 5;
                if (takistus.getTakistusX()[i] + takistus.getTakistuseLaius() < 0) {
                    takistus.getTakistusX()[i] = getWidth();
                    takistus.getTakistusY()[i] = (int) (Math.random() * (getHeight() - takistus.getTakistuseVahe() - takistus.getTakistuseKõrgus()));
                }
            }

            mandariin.setKiirus(mandariin.getKiirus() + mandariin.getGravitatsioon());
            mandariin.setMängijaAsukoht(mandariin.getMängijaAsukoht() + mandariin.getKiirus());

            if (mandariin.getMängijaAsukoht() > getHeight() - mandariin.getMängijaDiameeter() || mandariin.getMängijaAsukoht() < 0) {
                kasMängLäbi();
            }

            for (int i = 0; i < takistus.getTakistusX().length; i++) {
                if (mandariin.getMängijaAsukoht() < takistus.getTakistusY()[i] || mandariin.getMängijaAsukoht() + mandariin.getMängijaDiameeter() > takistus.getTakistusY()[i] + takistus.getTakistuseVahe()) {
                    if (150 + mandariin.getMängijaDiameeter() > takistus.getTakistusX()[i] && 150 < takistus.getTakistusX()[i] + takistus.getTakistuseLaius()) {
                        kasMängLäbi();
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE && !mängLäbi) {
            mandariin.muudaKiirust(-20);
        }else if(key == KeyEvent.VK_SPACE && mängLäbi){
            Restartmängule();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void resetTakistused() {
        int startX = 400;
        int maxTakistuseKõrgus = getHeight() - 2 * takistus.getTakistuseVahe();
        for (int i = 0; i < takistus.getTakistusX().length; i++) {
            takistus.getTakistusX()[i] = startX + i * takistus.getTakistuseKaugus();
            takistus.getTakistusY()[i] = (int) (Math.random() * maxTakistuseKõrgus) + takistus.getTakistuseVahe();
        }
    }

    private void kasMängLäbi() {
        mängLäbi = true;
        timer.stop();
    }

    private void Restartmängule(){
        resetTakistused();
        mandariin = new Mandariin(200, 2, 0, 30);
        mängLäbi = false;
        timer.restart();
    }
}


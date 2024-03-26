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
    private int skoor = 0;
    private int parimSkoor = 0;
    private double nurk = 0;
    private int täpiX = 0;
    private int täpiY = 0;
    private int mängijaX = 150;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.CYAN);

        mandariin = new Mandariin(200, 2, 0, 30);
        takistus = new Takistus();
        // Takistused on massiivis, et korraga saaks luua mitu takistust, et korraga saaks
        // ekraanil olla mitu takistust ja nende kustutamine/genereerimine ei tekitaks tühjasid auke.
        takistus.setTakistusX(new int[4]);
        takistus.setTakistusY(new int[4]);

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

        // Takistuse värvimine
        g.setColor(Color.GREEN);
        for (int i = 0; i < takistus.getTakistusX().length; i++) {
            g.fillRect(takistus.getTakistusX()[i], 0, takistus.getTakistuseLaius(), takistus.getTakistusY()[i]);
            g.fillRect(takistus.getTakistusX()[i], takistus.getTakistusY()[i] + takistus.getTakistuseVahe(), takistus.getTakistuseLaius(), getHeight() - takistus.getTakistusY()[i] - takistus.getTakistuseVahe());
        }

        // Mandariini värvimine
        g.setColor(Color.ORANGE);
        int mängijaDiameeter = mandariin.getMängijaDiameeter();
        g.fillOval(mängijaX, mandariin.getMängijaAsukoht(), mängijaDiameeter, mängijaDiameeter);

        g.setColor(Color.BLACK);
        int täpiDiameeter = 5;
        // Lahutame täpiraadiuse maha et täpp mandariini piiridest välja ei läheks.
        g.fillOval(täpiX - täpiDiameeter / 2, täpiY - täpiDiameeter / 2, täpiDiameeter, täpiDiameeter);

        // Skoorilugeja kuvamine
        g.setColor(Color.BLACK);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g.drawString("Skoor: " + skoor, 5, 25);

        // Parima skoori kuvamine
        g.setColor(Color.BLACK);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g.drawString("Parim skoor: " + parimSkoor, 5, 50);

        // "Mäng läbi" teksti kuvamine
        if (mängLäbi) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Comic Sans MS", Font.ITALIC, 50));
            FontMetrics fontMetrics = g.getFontMetrics();
            int tekstiLaius = fontMetrics.stringWidth("Mäng Läbi!");
            int tekstiKõrgus = fontMetrics.getHeight();
            int x = (getWidth() - tekstiLaius) / 2;
            int y = (getHeight() - tekstiKõrgus) / 2 + fontMetrics.getAscent();
            g.drawString("Mäng Läbi!", x, y);
            // Parimat tulemust uuendatakse ja vajadusel kuvatakse vastav teade
            if (uuendaParimatSkoori()) {
                g.setColor(Color.RED);
                g.setFont(new Font("Comic Sans MS", Font.ITALIC, 50));
                g.drawString("Uus parim tulemus: " + parimSkoor, x - tekstiLaius / 2, y + tekstiKõrgus);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!mängLäbi) {
            for (int i = 0; i < takistus.getTakistusX().length; i++) {
                takistus.getTakistusX()[i] -= 5; // Kui kiiresti takistus mängija poole liigub
                // Kui mängija on mõnest takistusest möödas, siis need kustutatakse ja luuakse uued.
                if (takistus.getTakistusX()[i] + takistus.getTakistuseLaius() < 0) {
                    takistus.getTakistusX()[i] = getWidth();
                    takistus.getTakistusY()[i] = (int) (Math.random() * (getHeight() - takistus.getTakistuseVahe() - takistus.getTakistuseKõrgus()));
                    skoor += 1;
                }
            }

            mandariin.setKiirus(mandariin.getKiirus() + mandariin.getGravitatsioon()); // Kiirendus
            mandariin.setMängijaAsukoht((mandariin.getMängijaAsukoht() + mandariin.getKiirus()));

            // Mäng saab läbi, kui mängija väljub ekraanilt
            if (mandariin.getMängijaAsukoht() > getHeight() - mandariin.getMängijaDiameeter() || mandariin.getMängijaAsukoht() < 0) {
                kasMängLäbi();
            }

            // Mäng saab läbi, kui mängija koordinaadid ühtivad takistuse omadega (mängija puutub takistust)
            for (int i = 0; i < takistus.getTakistusX().length; i++) {
                if (mandariin.getMängijaAsukoht() < takistus.getTakistusY()[i] || mandariin.getMängijaAsukoht() + mandariin.getMängijaDiameeter() > takistus.getTakistusY()[i] + takistus.getTakistuseVahe()) {
                    if (150 + mandariin.getMängijaDiameeter() > takistus.getTakistusX()[i] && 150 < takistus.getTakistusX()[i] + takistus.getTakistuseLaius()) {
                        kasMängLäbi();
                    }
                }
            }
            // Täpi liigutamine mandariinil ehk mandariini "veeremine".
            int pöörlemisKiirus = 8; // Siit saab kiirust regullida - suurem on kiirem rotatsioon
            nurk += Math.toRadians(pöörlemisKiirus); // Mitu kraadi iga tsükkel mandariin keerleb.

            int mängijaKeskPunktX = mängijaX + mandariin.getMängijaDiameeter() / 2;
            int mängijaKeskPunktY = mandariin.getMängijaAsukoht() + mandariin.getMängijaDiameeter() / 2;

            // Siin on üllatavalt elegantne lahendus: täpi x-koordinaat liigub koosinusfunktsiooni graafiku järgi ehk
            // see kasvab esimesed veerand ringi, siis kahaneb poole ringi jagu, siis viimase veerandi jälle kasvab.
            // Tähendab, x-koordinaat "kasvab" siis, kui koosinusfunktsiooni graafik on positiivne.
            // Y-koordinaat järgib siinusfunktsiooni graafikut.
            täpiX = (int) (mängijaKeskPunktX + Math.cos(nurk) * (mandariin.getMängijaDiameeter() / 2.3));
            täpiY = (int) (mängijaKeskPunktY + Math.sin(nurk) * (mandariin.getMängijaDiameeter() / 2.3));

        }
        repaint();
    }

    @Override
    // Hüppamine
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE && !mängLäbi) {
            mandariin.muudaKiirust(-20);
        } else if (key == KeyEvent.VK_SPACE) {
            Restartmängule();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Uued takistused viiakse sobivasse kohta.
    private void resetTakistused() {
        int startX = 400;
        int maxTakistuseKõrgus = getHeight() - 2 * takistus.getTakistuseVahe();
        for (int i = 0; i < takistus.getTakistusX().length; i++) {
            takistus.getTakistusX()[i] = startX + i * takistus.getTakistuseKaugus();
            takistus.getTakistusY()[i] = (int) (Math.random() * maxTakistuseKõrgus) + takistus.getTakistuseVahe();
        }
    }

    private boolean uuendaParimatSkoori() {
        if (parimSkoor < skoor) {
            parimSkoor = skoor;
            return true;
        }

        else return false;
    }

    private void kasMängLäbi() {
        mängLäbi = true;
        timer.stop();

    }

    // Kui mäng saab läbi ja mängija vajutab tühikuklahvi, siis algab mäng uuesti.
    private void Restartmängule() {
        resetTakistused();
        mandariin = new Mandariin(200, 2, 0, 30);
        mängLäbi = false;
        skoor = 0;
        timer.restart();
    }
}


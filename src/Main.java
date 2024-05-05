import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;


public class Main extends JFrame implements KeyListener{
    private MediaPlayer mediaPlayer;
    private boolean videoLõppenud;
    public Main() {
        setTitle("Veerev Mandariin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JFXPanel fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            // Video
            File videoFile = new File("src/Videod/Taavi_Libe.mp4");
            Media media = new Media(videoFile.toURI().toString());

            // Meediapleieri
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);

            MediaView mediaView = new MediaView(mediaPlayer);

            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane(mediaView);
            Scene scene = new Scene(stackPane);
            scene.setFill(javafx.scene.paint.Color.BLACK);

            fxPanel.setScene(scene);
            mediaPlayer.setOnEndOfMedia(() -> {
                javafx.scene.text.Text text = new javafx.scene.text.Text("Vajutage 'Tühiku' klahvi, et minna mängu");
                text.setFill(javafx.scene.paint.Color.RED);
                text.setFont(javafx.scene.text.Font.font("Arial", 50));
                text.setX(50);
                text.setY(50);
                stackPane.getChildren().add(text);
                fxPanel.setScene(scene);
            });

        });

        pack();
        setSize(1000, 600);
        setLocationRelativeTo(null);


        addKeyListener(this);
        setFocusable(true);
    }

    private void avamäng() {
        try { //ootab 0.5 sekundit enne mängu tööle panekut
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Avage mänguekraan
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        gamePanel.requestFocusInWindow();
        gamePanel.resetTakistused();
        gamePanel.setFocusable(true);

        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            mediaPlayer.stop();
            avamäng();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

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
    private int mängijaX = 25;

    private Clip taustamuusika;
    private Clip heliefekt;
    private Clip surmaheliefekt;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.CYAN);
        mandariin = new Mandariin(200, 2, 0, 30);
        takistus = new Takistus();
        // Takistused on massiivis, et korraga saaks luua mitu takistust, et korraga saaks
        // ekraanil olla mitu takistust ja nende kustutamine/genereerimine ei tekitaks tühjasid auke.
        takistus.setTakistusX(new int[4]);
        takistus.setTakistusY(new int[4]);

        timer = new Timer(25, this);
        timer.start();

        mängLäbi = false;

        setFocusable(true);
        addKeyListener(this);
        resetTakistused();
        laetaustamuusika("src/heli/SpringInMyStep.wav");
        laeheliefekt("src/heli/ding.wav");
        laesurmaefekt("src/heli/aaa.wav");
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
                    mängiheli();
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
                    if (25 + mandariin.getMängijaDiameeter() > takistus.getTakistusX()[i] && 25 < takistus.getTakistusX()[i] + takistus.getTakistuseLaius()) {
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
    void resetTakistused() {
        int startX = 400;
        int maxTakistuseKõrgus = getHeight() - 2 * takistus.getTakistuseVahe();
        for (int i = 0; i < takistus.getTakistusX().length; i++) {
            double randomValue = 0.1 + Math.random() * 0.7;
            takistus.getTakistusX()[i] = startX + i * takistus.getTakistuseKaugus();
            takistus.getTakistusY()[i] = (int) (randomValue * maxTakistuseKõrgus) + takistus.getTakistuseVahe();
        }
    }

    private boolean uuendaParimatSkoori() {
        if (parimSkoor < skoor) {
            parimSkoor = skoor;
            return true;
        } else return false;
    }

    private void kasMängLäbi() {
        mängLäbi = true;
        mängisurmaheli();
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

    private void laetaustamuusika(String failinimi) {
        try {
            float volume = -15.0f;
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(failinimi));
            taustamuusika = AudioSystem.getClip();
            taustamuusika.open(audioInputStream);
            FloatControl volumeControl = (FloatControl) taustamuusika.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);
            taustamuusika.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void laeheliefekt(String failinimi) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(failinimi));
            heliefekt = AudioSystem.getClip();
            heliefekt.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void mängiheli() {
        heliefekt.setFramePosition(0);
        heliefekt.start();
    }

    private void laesurmaefekt(String failinimi) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(failinimi));
            surmaheliefekt = AudioSystem.getClip();
            surmaheliefekt.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private void mängisurmaheli() {
        surmaheliefekt.setFramePosition(0);
        surmaheliefekt.start();
    }
}



import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;

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
    private boolean libeAeg;
    private Clip taustamuusika;
    private Clip heliefekt;
    private Clip surmaheliefekt;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.CYAN);
        mandariin = new Mandariin(200, 2, 0, 30);
        takistus = new Takistus();

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
        if (libeAeg) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }
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
        g.drawString("Parim skoor: " + loeParim(), 5, 50);

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

        if (libeAeg) { // Kuvame ilusa pildikese ekraanile :))))

            try {
                File pilt = new File("libenägu.png");
                BufferedImage image = ImageIO.read(pilt);
                int pildiX = mängijaX;
                int pildiY = mandariin.getMängijaAsukoht();
                g.drawImage(image, pildiX, pildiY, null);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                libeAeg = false; // Võtame selle L-i ja proovime järgmine kord uuesti
                // We'll get them next time boys
            }

            libeAeg = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        requestFocusInWindow();
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

            if (skoor % 5 == 0 && skoor > 0) libeAeg = true;
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
        // Takistused on massiivis, et korraga saaks luua mitu takistust, et korraga saaks
        // ekraanil olla mitu takistust ja nende kustutamine/genereerimine ei tekitaks tühjasid auke.
        // Muudame genereeritavate takistuste arvu vastavalt ekraani suurusele, sest
        // takistuste genereerimine/kadumine sõltub ekraani suurusest (millal on takistustest möödutud).
        // Nii väldime väikesel ekraanil kokku jooksvaid takistusi ja suurel ekraanil tühjasid auke.
        int genereeriKorraga = (int) (Math.round(4 + (getWidth() - 600) * 0.001));
        takistus.setTakistusX(new int[genereeriKorraga]);
        takistus.setTakistusY(new int[genereeriKorraga]);
        int startX = 400;
        int maxTakistuseKõrgus = getHeight() - 2 * takistus.getTakistuseVahe();
        for (int i = 0; i < takistus.getTakistusX().length; i++) {
            double randomValue = 0.1 + Math.random() * 0.7;
            takistus.getTakistusX()[i] = startX + i * takistus.getTakistuseKaugus();
            takistus.getTakistusY()[i] = (int) (randomValue * maxTakistuseKõrgus) + takistus.getTakistuseVahe();
        }
    }

    /**
     * Loeme failist parimad tulemused ja kui praegu saadud tulemus on neist suurem,
     * siis salvestame selle faili ja kuvame ekraanil uut parimat tulemust.
     *
     * @return tõeväärtus, kas parimat skoori on vaja uuendada
     */
    private boolean uuendaParimatSkoori() {
        int üldseParim = loeParim();
        if (üldseParim < skoor) {
            parimSkoor = skoor;
            salvestaParim(skoor);
            return true;
        }
        return false;
    }

    private void kasMängLäbi() {
        mängLäbi = true;
        mängisurmaheli();
        timer.stop();
    }

    /**
     * Võtame skoori ja salvestame faili "tulemused.txt".
     * Reaalsuses ajatemplil muud kasutust ei ole kui see, et näeb, millal mingi tulemus saadi
     *
     * @param skoor selles mängus saadud skoor
     */
    private void salvestaParim(int skoor) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                        new DataOutputStream(
                                new FileOutputStream("tulemused.txt", true)), "utf-8"))) {

            if (skoor == 0) return;
            bw.write(skoor + "@" + LocalDateTime.now());
            bw.write(System.lineSeparator());
            bw.flush();
        } catch (IOException e) {
            System.out.println("Kahjuks andmeid faili kirjutada ei saanud, aga sinu imeline skoor on: " + parimSkoor);
            System.out.println("Sinu andmed kaovad (erinevalt Facebookist), aga soovi korral vajuta tühikut ja proovi uuesti");
        }
    }

    /**
     * Loeme failist "tulemused.txt" kõik parimad skoorid ja tagastame neist suurima
     *
     * @return parim tulemus, mis selles arvutis saadud on
     */
    private int loeParim() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream("tulemused.txt")), "utf-8"))) {
            int suurim = 0;
            br.readLine();
            while (br.ready()) {
                String[] rida = br.readLine().split("@");
                if (suurim < Integer.parseInt(rida[0])) suurim = Integer.parseInt(rida[0]);
            }
            return suurim;
        } catch (Exception e) {
            return 0;
        }
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame implements ActionListener, KeyListener {
    private Timer timer;
    private boolean mängLäbi;
    private Mandariin mandariin;
    private Takistus takistus;



    public Main() {
        setTitle("Veerev Mandariin");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addKeyListener(this);


        mandariin = new Mandariin(250, 2, 0, 30, 30);
        takistus = new Takistus();
        takistus.setTakistusX(new int[3]);
        takistus.setTakistusY(new int[3]);

        timer = new Timer(20, this);
        timer.start();

        resetTakistused();
        mängLäbi = false;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE && !mängLäbi) {
            mandariin.muudaKiirust(-20);
        }
    }

    public void resetTakistused(){
        int startX = 400;
        for (int i = 0; i < takistus.getTakistusX().length; i++) {
            takistus.getTakistusX()[i] = startX + i * takistus.getTakistuseKaugus();
            takistus.getTakistusY()[i] = (int) (Math.random() * (getHeight() - takistus.getTakistuseVahe() - takistus.getTakistuseKõrgus()));
        }
    }

    public void paint(Graphics g){
        super.paint(g);

        g.setColor(Color.CYAN);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.GREEN);
        for (int i = 0; i < takistus.getTakistusX().length; i++) {
            g.fillRect(takistus.getTakistusX()[i], 0, takistus.getTakistuseLaius(), takistus.getTakistusY()[i]);
            g.fillRect(takistus.getTakistusX()[i], takistus.getTakistusY()[i] + takistus.getTakistuseVahe(), takistus.getTakistuseLaius(), getHeight() - takistus.getTakistusY()[i] - takistus.getTakistuseVahe());
        }

        g.setColor(Color.RED);
        g.fillRect(150, mandariin.getMängijaAsukoht(), mandariin.getMängijaLaius(), mandariin.getMängijaKõrgus());

        if (mängLäbi) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Mäng Läbi!", 250, getHeight() / 2);
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

            if (mandariin.getMängijaAsukoht() > getHeight() - mandariin.getMängijaKõrgus() || mandariin.getMängijaAsukoht() < 0) {
                kasMängLäbi();
            }

            for (int i = 0; i < takistus.getTakistusX().length; i++) {
                if (mandariin.getMängijaAsukoht() < takistus.getTakistusY()[i] || mandariin.getMängijaAsukoht() + mandariin.getMängijaKõrgus() > takistus.getTakistusY()[i] + takistus.getTakistuseVahe()) {
                    if (150 + mandariin.getMängijaLaius() > takistus.getTakistusX()[i] && 150 < takistus.getTakistusX()[i] + takistus.getTakistuseLaius()) {
                        kasMängLäbi();
                    }
                }
            }

            repaint();
        }

    }

    public void kasMängLäbi() {
        mängLäbi = true;
        timer.stop();
    }


    public void keyReleased(KeyEvent e) {}; // PLACEHOLDER
    //sisse ehitatud interface(java.util) tahab neid, aga pole praegu midagi teha nendega

    @Override
    public void keyTyped(KeyEvent e) {     //PLACEHOLDER
        //sisse ehitatud interface(java.util) tahab neid, aga pole praegu midagi teha nendega

    }

    public static void main(String[] args) {

        /*SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });

         */

        Main main = new Main();
        main.setVisible(true);


        /*Mandariin mandariin = new Mandariin(250, 2, 0, 30, 30);
        mandariin.setMängijaAsukoht(250);
        mandariin.setGravitatsioon(0);
        mandariin.setKiirus(0);
        Takistus takistus = new Takistus();
        takistus.setTakistusX(new int[3]);
        takistus.setTakistusY(new int[3]);

         */
    }
}


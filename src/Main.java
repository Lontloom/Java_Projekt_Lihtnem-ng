import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame implements ActionListener, KeyListener {
    private Timer timer;
    private boolean mängLäbi;


    // actionPerformed puudu, see on prolly kõige suurem osa praegu, millega tegeleda
    public Main() {
        setTitle("Veerev Mandariin");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addKeyListener(this);


        timer = new Timer(20, this);
        timer.start();


        mängLäbi = false;
    }


    public void nupuVajutus(KeyEvent e) {
        int nupp = e.getKeyCode();
        if (nupp == KeyEvent.VK_SPACE && !mängLäbi) {
            // Siia lisada see mandariin.setKiirus(-20);
        }
    }

    public void keyTyped(KeyEvent e) {};
    public void keyReleased(KeyEvent e) {};

    public void kasMängLäbi() {
        mängLäbi = true;
        timer.stop();
    }

    public static void main(String[] args) {
        Mandariin mandariin = new Mandariin(250, 2, 0, 30, 30);
        mandariin.setMängijaAsukoht(250);
        mandariin.setGravitatsioon(0);
        mandariin.setKiirus(0);
        Takistus takistus = new Takistus();
        takistus.setTakistusX(new int[3]);
        takistus.setTakistusY(new int[3]);
    }

}


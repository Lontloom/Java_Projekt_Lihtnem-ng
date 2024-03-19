import java.awt.*;

public class Mandariin {
    private int mängijaAsukoht;
    private int gravitatsioon;
    private int mängijaKõrgus;
    private int mängijaLaius;

    public int getMängijaAsukoht() {
        return mängijaAsukoht;
    }

    public int getGravitatsioon() {
        return gravitatsioon;
    }

    public int getMängijaKõrgus() {
        return mängijaKõrgus;
    }

    public int getMängijaLaius() {
        return mängijaLaius;
    }

    public void setGravitatsioon(int gravitatsioon) {
        this.gravitatsioon = gravitatsioon;
    }

    public void setMängijaAsukoht(int mängijaAsukoht) {
        this.mängijaAsukoht = mängijaAsukoht;
    }

    public Mandariin(int mängijaAsukoht, int gravitatsioon, int mängijaKõrgus, int mängijaLaius) {
        this.mängijaAsukoht = mängijaAsukoht;
        this.gravitatsioon = gravitatsioon;
        this.mängijaKõrgus = mängijaKõrgus;
        this.mängijaLaius = mängijaLaius;
    }

    public void paint(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(150, mängijaAsukoht, mängijaKõrgus, mängijaLaius);
    }

}

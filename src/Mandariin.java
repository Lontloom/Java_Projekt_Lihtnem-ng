import java.awt.*;

public class Mandariin {
    private int mängijaAsukoht;
    private int gravitatsioon;
    private int kiirus;
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

    public int getKiirus() {
        return kiirus;
    }

    public void setKiirus(int kiirus) {
        this.kiirus = kiirus;
    }

    public void setGravitatsioon(int gravitatsioon) {
        this.gravitatsioon = gravitatsioon;
    }

    public void setMängijaAsukoht(int mängijaAsukoht) {
        this.mängijaAsukoht = mängijaAsukoht;
    }

    public Mandariin(int mängijaAsukoht, int gravitatsioon, int kiirus, int mängijaKõrgus, int mängijaLaius) {
        this.mängijaAsukoht = mängijaAsukoht;
        this.gravitatsioon = gravitatsioon;
        this.mängijaKõrgus = mängijaKõrgus;
        this.mängijaLaius = mängijaLaius;
        this.kiirus = kiirus;
    }


 // Ma ei tea kas see panna eraldi igasse klassi või maini üks kokku
    public void paint(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(150, mängijaAsukoht, mängijaKõrgus, mängijaLaius);
    }

}

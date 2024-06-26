import java.awt.*;

public class Mandariin {
    private int mängijaAsukoht; // Mängija kõrguse koordinaadid
    private int gravitatsioon; // Kukkumise kiirendus
    private int kiirus; // Hetkeline kiirus, mida muudetakse hüppamise ja kukkumisega
    private int mängijaDiameeter;

    public int getMängijaAsukoht() {
        return mängijaAsukoht;
    }

    public int getGravitatsioon() {
        return gravitatsioon;
    }

    public int getMängijaDiameeter() {
        return mängijaDiameeter;
    }

    public int getKiirus() {
        return kiirus;
    }

    public void setKiirus(int kiirus) {
        this.kiirus = kiirus;
    }

    public void setMängijaAsukoht(int mängijaAsukoht) {
        this.mängijaAsukoht = mängijaAsukoht;
    }

    public Mandariin(int mängijaAsukoht, int gravitatsioon, int kiirus, int mängijaDiameeter) {
        this.mängijaAsukoht = mängijaAsukoht;
        this.gravitatsioon = gravitatsioon;
        this.mängijaDiameeter = mängijaDiameeter;
        this.kiirus = kiirus;
    }

    public void muudaKiirust(int uusKiirus){
        this.kiirus = uusKiirus;
    }
}

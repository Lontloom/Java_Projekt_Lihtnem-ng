import java.awt.*;

public class Mandariin {
    private int mängijaAsukoht; // Mängija keskpunkti koordinaadid
    private double gravitatsioon; // Kukkumise kiirendus
    private double kiirus; // Hetkeline kiirus, mida muudetakse hüppamise ja kukkumisega
    private int mängijaDiameeter;

    public int getMängijaAsukoht() {
        return mängijaAsukoht;
    }

    public double getGravitatsioon() {
        return gravitatsioon;
    }

    public int getMängijaDiameeter() {
        return mängijaDiameeter;
    }

    public double getKiirus() {
        return kiirus;
    }

    public void setKiirus(double kiirus) {
        this.kiirus = kiirus;
    }

    public void setGravitatsioon(double gravitatsioon) {
        this.gravitatsioon = gravitatsioon;
    }

    public void setMängijaAsukoht(int mängijaAsukoht) {
        this.mängijaAsukoht = mängijaAsukoht;
    }

    public Mandariin(int mängijaAsukoht, double gravitatsioon, double kiirus, int mängijaDiameeter) {
        this.mängijaAsukoht = mängijaAsukoht;
        this.gravitatsioon = gravitatsioon;
        this.mängijaDiameeter = mängijaDiameeter;
        this.kiirus = kiirus;
    }

    public void muudaKiirust(double uusKiirus){
        this.kiirus = uusKiirus;
    }
}

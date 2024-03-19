import static java.lang.Thread.sleep;

public class Mängija implements Entity{
    private String[][] kujutis;
    private int elud;

    public Mängija(String[][] kujutis, int elud) {
        this.kujutis = kujutis;
        this.elud = elud;
    }

    public String[][] getKujutis() {
        return kujutis;
    }

    public void setKujutis(String[][] kujutis) {
        this.kujutis = kujutis;
    }

    public int getElud() {
        return elud;
    }

    public void setElud(int elud) {
        this.elud = elud;
    }

    public void samm() throws InterruptedException {
        String[][] algnekuju = this.getKujutis();
        String[][] vanakuju = {{" ","\uD83D\uDE0E"," "},
                                    {"/","|","\\"},
                                    {" ","|"," "},
                                    {"/"," ","\\"}};
        String[][] uuskuju = {{" ","\uD83D\uDE0E"," "},
                                    {"/","|","\\"},
                                    {" ","|"," "},
                                    {" ","|"," "}};
        setKujutis(uuskuju);
        System.out.println(this.toString());
        sleep(300);
        setKujutis(vanakuju);
        System.out.println(this.toString());
        sleep(300);
        setKujutis(algnekuju);

    }

    public void hüppa() throws InterruptedException {
        String[][] algnekuju = this.getKujutis();
        String[][] vanakuju = {{" ","\uD83D\uDE0E"," "},
                                    {" ","|"," "},
                                    {" ","|"," "},
                                    {" ","|"," "}};
        String[][] uuskuju = {{" ","\uD83D\uDE0E"," "},
                                    {"\\","|","/"},
                                    {" ","|"," "},
                                    {"/"," ","\\"}};
        setKujutis(uuskuju);
        System.out.println(this.toString());
        sleep(300);
        setKujutis(vanakuju);
        System.out.println(this.toString());
        sleep(300);
        setKujutis(algnekuju);
    }
    public String toString() {
        for (String[] kuju : getKujutis()) {
            System.out.println(kuju[0] + "" + kuju[1] + "" + kuju[2]);
        }
        return "";
    }
}

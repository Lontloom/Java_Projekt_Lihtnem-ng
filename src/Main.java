
public class Main {
    public static void main(String[] args) throws InterruptedException {
        String[][] kujutis = {{" ","\uD83D\uDE0E"," "},
                            {"/","|","\\"},
                            {" ","|"," "},
                            {"/"," ","\\"}};
        Mängija madis = new Mängija(kujutis, 10);

        System.out.println(madis);
        for (int i = 0; i < 10; i++) {
            madis.hüppa();

        }
        for (int i = 0; i < 10; i++) {
            madis.samm();

        }
    }
}

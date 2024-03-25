public class Takistus {
    private int[] takistusX, takistusY;
    private int takistuseVahe = 200; // vertikaalne vahemaa takistuse ülemise ja alumise osa vahel
    private int takistuseLaius = 80;
    private int takistuseKõrgus = 30;
    private int takistuseKaugus = 270; // Takistuste horisontaalne kaugus üksteisest

    public int[] getTakistusX() {
        return takistusX;
    }

    public void setTakistusX(int[] takistusX) {
        this.takistusX = takistusX;
    }

    public int[] getTakistusY() {
        return takistusY;
    }

    public void setTakistusY(int[] takistusY) {
        this.takistusY = takistusY;
    }

    public int getTakistuseVahe() {
        return takistuseVahe;
    }

    public void setTakistuseVahe(int takistuseVahe) {
        this.takistuseVahe = takistuseVahe;
    }

    public int getTakistuseLaius() {
        return takistuseLaius;
    }

    public void setTakistuseLaius(int takistuseLaius) {
        this.takistuseLaius = takistuseLaius;
    }

    public int getTakistuseKõrgus() {
        return takistuseKõrgus;
    }

    public void setTakistuseKõrgus(int takistuseKõrgus) {
        this.takistuseKõrgus = takistuseKõrgus;
    }

    public int getTakistuseKaugus() {
        return takistuseKaugus;
    }

    public void setTakistuseKaugus(int takistuseKaugus) {
        this.takistuseKaugus = takistuseKaugus;
    }
}

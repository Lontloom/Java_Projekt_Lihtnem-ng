import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


public class Main extends JFrame implements MouseListener {
    private MediaPlayer mediaPlayer;

    public Main() {
        setTitle("Veerev Mandariin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        JFXPanel fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            // Video
            File videoFile = new File("src/Videod/Taavi_Libe.mp4");
            Media media = new Media(videoFile.toURI().toString());

            // Meediapleieri
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);

            MediaView mediaView = new MediaView(mediaPlayer);

            javafx.scene.layout.StackPane stackPane = new javafx.scene.layout.StackPane(mediaView);
            Scene scene = new Scene(stackPane);
            scene.setFill(javafx.scene.paint.Color.BLACK);

            fxPanel.setScene(scene);
            mediaPlayer.setOnEndOfMedia(() -> {
                javafx.scene.text.Text text1 = new javafx.scene.text.Text("Valmis veerema?");
                javafx.scene.text.Text text2 = new javafx.scene.text.Text("Vajuta siia, et alustada oma uut seiklust");
                javafx.scene.text.Text text3 = new javafx.scene.text.Text("(mängus vajuta tühikut, et hüpata)");
                text1.setFill(Color.RED);
                text1.setFont(javafx.scene.text.Font.font("Arial", 100));
                text2.setFill(javafx.scene.paint.Color.WHITE);
                text3.setFill(javafx.scene.paint.Color.WHITE);
                text2.setFont(javafx.scene.text.Font.font("Arial", 50));
                text3.setFont(javafx.scene.text.Font.font("Arial", 40));
                stackPane.setAlignment(text1, Pos.TOP_CENTER);
                stackPane.setAlignment(text2, Pos.CENTER);
                stackPane.setAlignment(text3, Pos.BOTTOM_CENTER);
                stackPane.getChildren().add(text1);
                stackPane.getChildren().add(text2);
                stackPane.getChildren().add(text3);
                fxPanel.setScene(scene);
            });

        });

        pack();
        setSize(1000, 600);
        setLocationRelativeTo(null);
        fxPanel.addMouseListener(this);
        setFocusable(true);
    }

    private void avamäng() {
        try { //ootab 0.5 sekundit enne mängu tööle panekut
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Avage mänguekraan
        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        gamePanel.requestFocusInWindow();
        gamePanel.resetTakistused();
        gamePanel.setFocusable(true);
        setVisible(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mediaPlayer.stop();
        avamäng();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.setVisible(true);
        });
    }
}





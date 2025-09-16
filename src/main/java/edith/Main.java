package edith;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for E.D.I.T.H. using FXML.
 */
public class Main extends Application {

    private Edith edith = new Edith("edith.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("E.D.I.T.H.");
            stage.setResizable(true);
            stage.setMinWidth(350);
            stage.setMinHeight(400);
            fxmlLoader.<MainWindow>getController().setEdith(edith);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

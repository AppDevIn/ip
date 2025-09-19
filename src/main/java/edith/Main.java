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

    private static final int MIN_WINDOW_WIDTH = 350;
    private static final int MIN_WINDOW_HEIGHT = 400;

    private Edith edith = new Edith("edith.txt");

    /**
     * Starts the JavaFX application and sets up the main window.
     * Loads the FXML layout, configures the stage, and displays the GUI.
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("E.D.I.T.H.");
            stage.setResizable(true);
            stage.setMinWidth(MIN_WINDOW_WIDTH);
            stage.setMinHeight(MIN_WINDOW_HEIGHT);
            fxmlLoader.<MainWindow>getController().setEdith(edith);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package edith;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Edith edith;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image edithImage = new Image(this.getClass().getResourceAsStream("/images/DaEdith.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));
    }

    /**
     * Injects the E.D.I.T.H. instance and displays welcome message
     *
     * @param e The E.D.I.T.H. instance to use for generating responses
     */
    public void setEdith(Edith e) {
        edith = e;
        showWelcomeMessage();
    }

    /**
     * Shows the welcome message when E.D.I.T.H. starts up
     */
    private void showWelcomeMessage() {
        String welcomeMessage = "Hello! I'm E.D.I.T.H.\nWhat can I do for you?";
        dialogContainer.getChildren().add(
                BotDialogBox.getBotDialog(welcomeMessage, edithImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing E.D.I.T.H.'s reply and then appends them to
     * the dialog container. Clears the user input after processing.
     * If the user enters a bye command, closes the application after showing the response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = edith.getResponse(input);
        dialogContainer.getChildren().addAll(
                UserDialogBox.getUserDialog(input, userImage),
                BotDialogBox.getBotDialog(response, edithImage)
        );
        userInput.clear();

        if (edith.shouldExit()) {
            javafx.application.Platform.runLater(() -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                javafx.application.Platform.exit();
            });
        }
    }
}

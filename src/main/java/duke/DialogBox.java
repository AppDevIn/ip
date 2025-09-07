package duke;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Custom control representing a dialog box in the chat interface.
 * Contains a text label and an image view for the user avatar.
 * Extends HBox to arrange components horizontally.
 */
public class DialogBox extends HBox {

    private Label text;
    private ImageView displayPicture;

    /**
     * Creates a new dialog box with the specified text and image.
     * Sets up the layout with text wrapping and proper image sizing.
     *
     * @param text The text content to display in the dialog
     * @param image The image to display as the user avatar
     */
    public DialogBox(String text, Image image) {
        this.text = new Label(text);
        displayPicture = new ImageView(image);

        this.text.setWrapText(true);
        displayPicture.setFitWidth(100.0);
        displayPicture.setFitHeight(100.0);
        this.setAlignment(Pos.TOP_RIGHT);

        this.getChildren().addAll(this.text, displayPicture);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        this.setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
    }

    /**
     * Creates a dialog box for user messages.
     *
     * @param text The text content to display
     * @param image The user avatar image
     * @return A DialogBox configured for user messages
     */
    public static DialogBox getUserDialog(String text, Image image) {
        return new DialogBox(text, image);
    }

    /**
     * Creates a dialog box for Duke messages with flipped layout.
     *
     * @param text The text content to display
     * @param image The Duke avatar image
     * @return A DialogBox configured for Duke messages
     */
    public static DialogBox getDukeDialog(String text, Image image) {
        var db = new DialogBox(text, image);
        db.flip();
        return db;
    }
}

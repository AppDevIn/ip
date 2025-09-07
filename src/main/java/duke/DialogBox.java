package duke;

import javafx.geometry.Pos;
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
}

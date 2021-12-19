package app;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class ChatsListCard extends JFXListCell<ChatCardModel> {

    @FXML
    public Label text;

    @FXML
    public AnchorPane cardContainer;

    @FXML
    public BorderPane chat_card_borderpane;

    //public ImageView chat_space_imageview;

    public FXMLLoader fxmlLoader;
    @FXML
    public JFXListCell cells;

    @Override
    public void  updateItem(ChatCardModel model, boolean empty) {
        super.updateItem(model, empty);
        if(model != null || !empty) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/chatsListCard.fxml"));
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                System.out.println("failed to load fxml: " + e);
            }
            String imgUrl = "http://localhost:80/messaging_app/uploads/images/1.png";
            Image img = new Image(imgUrl);
            Circle clip = new Circle();
            ImageView chat_space_imageview = new ImageView();
            BorderPane borderPane = new BorderPane();
            clip.setRadius(25);
            clip.setCenterX(0);
            clip.setCenterY(0);

            chat_space_imageview.setImage(img);
            chat_space_imageview.setClip(clip);
            borderPane.maxHeight(60);
            borderPane.maxWidth(200);
            borderPane.setStyle("fx-background-color: red");
            borderPane.setLeft(chat_space_imageview);

            //chat_card_borderpane.setLeft();
            //text.setText(model.getText());
                setText(null);
            setGraphic(borderPane);
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}

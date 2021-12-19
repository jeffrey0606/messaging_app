package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatContainer implements Initializable {

    public ImageView profile_image;
    public BorderPane side_bar_chat_list;
    public StackPane dialog_parent_stackpane;
    //public JFXDialog jfxDialog;
    public JFXButton create_group_id;
    public static JFXButton createGroupId;

    public static JFXDialog jfxDialog;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createGroupId = create_group_id;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmls/chatListView.fxml"));
            side_bar_chat_list.setCenter(root);
        } catch (IOException e) {
            System.out.println("failed to load list view: " + e);
        }
    }

    public void create_group(ActionEvent actionEvent) {
        try {
            Parent createChatGroup = FXMLLoader.load(getClass().getResource("/fxmls/createChatGroup.fxml"));
            jfxDialog = new JFXDialog(dialog_parent_stackpane, (AnchorPane) createChatGroup, JFXDialog.DialogTransition.CENTER, false);
            jfxDialog.show();
            createGroupId.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeDialog() {
        jfxDialog.close();
        createGroupId.setDisable(false);
    }
}

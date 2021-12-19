package app;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatListViewController implements Initializable {

    public JFXListView<ChatCardModel> list_view_of_chats;
    public static ObservableList<ChatCardModel> chatCardObservableList = FXCollections.observableArrayList();
    public static int i = 0;

    public static void addNewChatCard() {
        chatCardObservableList.add(0, new ChatCardModel("Jeffrey: " + i++));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatCardObservableList.add(new ChatCardModel("jeffrey"));
        list_view_of_chats.setItems(chatCardObservableList);
        list_view_of_chats.setCellFactory( card -> new ChatsListCard());
    }
}

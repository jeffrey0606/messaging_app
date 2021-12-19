package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class CreateChatGroup implements Initializable {

    public JFXListView add_users_list;
    public JFXButton add_new_user_id;
    public TextField new_user_textfield;
    public Text error_text;
    public Text no_user_yet;
    public  static ObservableList<HashMap<String, Object>> added_users = FXCollections.observableArrayList();
    public JFXButton create_chat_space_id;
    public FontAwesomeIcon chat_space_icon;
    public ImageView chat_space_image;
    public AnchorPane change_chat_space_image;
    public static File fileToUpload;
    public TextArea chat_space_purpose_textArea;
    public TextField chat_space_name;


    EventHandler<MouseEvent> addChatSpaceImageEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            FileChooser fileChooser = new FileChooser();

            fileToUpload = fileChooser.showOpenDialog(new Stage());

            if(fileToUpload != null) {
                chat_space_icon.setVisible(false);
                chat_space_image.setImage(new Image(fileToUpload.toURI().toString()));
            }
        }
    };

    @FXML
    public void close_dialog(ActionEvent actionEvent) {
        ChatContainer chatContainer = new ChatContainer();
        added_users.clear();
        chatContainer.closeDialog();
    }

    public static boolean deleteAddedUser(HashMap<String, Object> user) {
        return added_users.remove(user);
    }

    public void on_add_user_textfield_change(KeyEvent keyEvent) {
        if (new_user_textfield.getText().isEmpty()) {
            add_new_user_id.setDisable(true);
        }
        add_new_user_id.setDisable(false);
    }

    public void add_new_user(ActionEvent actionEvent) throws SQLException {
        Dbmanager dbmanager = new Dbmanager();
        if(!new_user_textfield.getText().isEmpty()) {

            try {
                int userId = Integer.parseInt(new_user_textfield.getText());
                HashMap<String, Object> user = dbmanager.getUserInfos(userId);
                if(!user.isEmpty()) {
                    System.out.println("re: " + user);
                    if(!added_users.contains(user))added_users.add(user);
                    if(!added_users.isEmpty()){
                        no_user_yet.setText("");
                    }
                } else {
                    error_text.setText("There is no user with an ID of " + userId +"!");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                error_text.setText("");
                            });
                        }
                    }, 5000);
                }
            } catch (NumberFormatException e) {
                error_text.setText("The User Id you tried to enter is not an number");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            error_text.setText("");
                        });
                    }
                }, 5000);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        add_users_list.setItems(added_users);
        add_users_list.setCellFactory(addUsersCell -> new AddUsersCell());

        change_chat_space_image.addEventFilter(MouseEvent.MOUSE_CLICKED, addChatSpaceImageEventHandler);
    }

    public void create_chat_space_button(ActionEvent actionEvent) throws IOException, ParseException, SQLException {
        if( !chat_space_purpose_textArea.getText().isEmpty() && !chat_space_name.getText().isEmpty()) {
            UploadProfile uploadProfile = new UploadProfile();
            ArrayList<Integer> addedUsersId = new ArrayList<Integer>();
            UserData userData = new UserData();
            added_users.forEach(user -> {
               addedUsersId.add((Integer) user.get("id"));
            });

            if(fileToUpload != null) {
                HashMap<String, Object> uploadResult = uploadProfile.startUploading(fileToUpload);
                if((boolean) uploadResult.get("status") == true) {
                    Dbmanager dbmanager = new Dbmanager();
                    boolean result = dbmanager.createChatSpace(chat_space_purpose_textArea.getText(), chat_space_name.getText(), (String) uploadResult.get("msg"), userData.getId(), addedUsersId);
                    if(result) {
                        fileToUpload = null;
                        ChatContainer chatContainer = new ChatContainer();
                        added_users.clear();
                        ChatListViewController.addNewChatCard();
                        chatContainer.closeDialog();
                    } else {
                        error_text.setText((String) uploadResult.get("msg"));
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    error_text.setText("");
                                });
                            }
                        }, 5000);
                    }
                } else {
                    error_text.setText("The Chat Space Image Failed to Upload! Please try again later");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                error_text.setText("");
                            });
                        }
                    }, 5000);
                }
            } else {
                Dbmanager dbmanager = new Dbmanager();
                boolean result = dbmanager.createChatSpace(chat_space_purpose_textArea.getText(), chat_space_name.getText(), null, userData.getId(), addedUsersId);
                if(result) {
                    fileToUpload = null;
                    ChatContainer chatContainer = new ChatContainer();
                    added_users.clear();
                    ChatListViewController.addNewChatCard();
                    chatContainer.closeDialog();
                } else {
                    error_text.setText("Could not create a new Chat Space! Please again later");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                error_text.setText("");
                            });
                        }
                    }, 5000);
                }
            }
        } else {
            error_text.setText("Both the Chat Space Name and Chat Space Purpose Fields are required");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        error_text.setText("");
                    });
                }
            }, 5000);
        }
    }
}

class AddUsersCell extends JFXListCell<HashMap<String, Object>> {

    public FXMLLoader fxmlLoader;
    @FXML
    public BorderPane addUserContainer;
    @FXML
    public ImageView profile_image;

    @FXML
    public FontAwesomeIcon delete_added_user;
    @FXML
    public Label user_name;
    @FXML
    public Label user_id;

    @Override
    public void updateItem(HashMap<String, Object> user, boolean empty) {
        super.updateItem(user, empty);

        if(user != null || !empty) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxmls/addUsersCard.fxml"));
            fxmlLoader.setController(this);
            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Creating the mouse event handler
            EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    CreateChatGroup.deleteAddedUser(user);
                    System.out.println("Hello World");
                }
            };
            //Registering the event filter
            delete_added_user.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
            //Circle clip = new Circle();
            //clip.setRadius(40 / 2);
            //clip.setCenterX(50 / 2);
            //clip.setCenterY(50 / 2);
            //profile_image.setClip(clip);
            user_name.setText((String) user.get("user_name"));
            user_id.setText(String.valueOf(user.get("id")));
            if(user.get("profile_image") != null) profile_image.setImage(new Image("http://localhost:80/messaging_app/uploads/images/"+user.get("profile_image")));
            setText(null);
            setGraphic(addUserContainer);
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}

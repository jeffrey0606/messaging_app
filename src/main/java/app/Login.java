package app;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Login {

    public JFXButton login_button_id;
    public TextField user_name;
    public TextField password;
    public Text error_msg;
    public ProgressIndicator progress_indicator;

    @FXML
    public void register(MouseEvent mouseEvent) {

        Home home = new Home();

        home.changeTo("register");
    }

    public void login_buttton(ActionEvent actionEvent) {
        if(user_name.getText().isEmpty()) {
            user_name.setStyle("-fx-border-color: red");
            error_msg.setText("A user name is required");
        } else if (password.getText().isEmpty()) {
            password.setStyle("-fx-border-color: red");
            error_msg.setText("A password is required");
        } else {
            error_msg.setText("");
            user_name.setStyle("-fx-border-color: gray");
            password.setStyle("-fx-border-color: gray");

            progress_indicator.setVisible(true);
            login_button_id.setDisable(true);

            Dbmanager dbmanager = new Dbmanager();
            HashMap<String, Object> res = dbmanager.login(user_name.getText(), password.getText());

            if((boolean) res.get("status")) {
                App app = new App();
                UserData userData = new UserData();
                userData.setName(user_name.getText());
                userData.setId((int) res.get("user_id"));
                app.goChatContainer();
                System.out.println("User logged in successfully");
            } else {
                error_msg.setText("User was not Found or Try try again later");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            error_msg.setText("");
                        });
                    }
                }, 5000);
                System.out.println("User not found");
            }

            progress_indicator.setVisible(false);
            login_button_id.setDisable(false);
        }
    }
}

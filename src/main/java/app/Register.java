package app;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Register{

    public TextField user_name;

    public TextField password;
    public TextField confirm_password;
    public Text error_msg;

    public Statement stmt;
    public ResultSet result;
    public ProgressIndicator progress_indicator;
    public JFXButton register_button_id;

    public void login(MouseEvent mouseEvent) {
        Home home = new Home();

        home.changeTo("login");
    }


    public void register_button(ActionEvent actionEvent) {
        if(user_name.getText().isEmpty()) {
            user_name.setStyle("-fx-border-color: red");
            error_msg.setText("A user name is required");
        } else if (password.getText().isEmpty()) {
            password.setStyle("-fx-border-color: red");
            error_msg.setText("A password is required");
        } else if (confirm_password.getText().isEmpty()) {
            confirm_password.setStyle("-fx-border-color: red");
            error_msg.setText("A password confirmation is required");
        } else {
            error_msg.setText("");
            user_name.setStyle("-fx-border-color: gray");
            password.setStyle("-fx-border-color: gray");
            confirm_password.setStyle("-fx-border-color: gray");

            if(password.getText().equals(confirm_password.getText())) {
                progress_indicator.setVisible(true);
                register_button_id.setDisable(true);

                Dbmanager dbmanager = new Dbmanager();
                boolean exits = dbmanager.userAlreadyExits(user_name.getText());

                if(exits) {
                    error_msg.setText("Oops! There is a user registered with this name.");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {
                                error_msg.setText("");
                            });
                        }
                    }, 5000);
                    System.out.println("User Already Exits!");
                } else {
                    HashMap<String, Object> res = dbmanager.register(user_name.getText(), confirm_password.getText());

                    if((boolean) res.get("status")) {
                        Home home = new Home();
                        UserData userData = new UserData();
                        userData.setId((int) res.get("user_id"));
                        userData.setName(user_name.getText());
                        home.changeTo("uploadProfile");
                        System.out.println("User added successfully");
                    } else {
                        error_msg.setText("Oops something went wrong! Please try again later.");
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(() -> {
                                    error_msg.setText("");
                                });
                            }
                        }, 5000);
                        System.out.println("Failed to add user");
                    }
                }
                progress_indicator.setVisible(false);
                register_button_id.setDisable(false);
            } else {
                confirm_password.setStyle("-fx-border-color: red");
                error_msg.setText("password doesn't match");
            }
        }
    }
}

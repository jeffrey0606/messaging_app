package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {
    @FXML
    private BorderPane login_register_container;

    private static BorderPane state;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        state = login_register_container;
        loadURI("login", state);
    }

    public  void changeTo(String ui) {
        System.out.println("borderPane: " + state + " ui: " + ui);

        if(ui.equals("uploadProfile")){
            UserData userData = new UserData();
            System.out.println("User id: " + userData.getId() + " User Name: " + userData.getName());
        }

        state.setCenter(null);

        loadURI(ui, state);
    }

    public void loadURI(String ui, BorderPane borderPane) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxmls/" + ui +".fxml"));
        } catch (IOException e) {
            System.out.println("failed to load fxml ui: "+ e);
        }
        borderPane.setCenter(root);
    }
}

package app;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class App implements Initializable {

    public static Connection conn;
    public static HashMap<String, Object> state = new HashMap<String, Object>();

    @FXML
    public BorderPane app_container;
    public static BorderPane appConatiner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appConatiner = app_container;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxmls/splashScreen.fxml"));
            state.putIfAbsent("currentScreen", root);
        } catch (IOException e) {
            e.printStackTrace();
        }

        appConatiner.setCenter((Parent) state.get("currentScreen"));

        loadDrivers();
        dbConnect();
    }

    public void goChatContainer() {
        UserData userData = new UserData();
        System.out.println("User id: " + userData.getId() + " User Name: " + userData.getName());
        try {
            state.replace("currentScreen", FXMLLoader.load(getClass().getResource("/fxmls/chatContainer.fxml")));

            appConatiner.setCenter((Parent) state.get("currentScreen"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDrivers() {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            System.out.println("failed to load Drivers: " + ex);
        }
    }

    public void dbConnect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/messaging_app?serverTimezone=UTC", "root", "");

            //if(conn != null) {
                System.out.println("connected successfully to db");
                state.replace("currentScreen", FXMLLoader.load(getClass().getResource("/fxmls/home.fxml")));
                Timer timer = new Timer();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            appConatiner.setCenter((Parent) state.get("currentScreen"));
                        });
                    }
                }, 3000);
            //} else {
                System.out.println("could not connect to the db.");
            //}
            // Do something with the Connection
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.out.println("could not connect to the db.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
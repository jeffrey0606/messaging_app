package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        stage.initStyle(StageStyle.DECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("/fxmls/app.fxml"));
        stage.setTitle("Hello World");
        Scene scene = new Scene(root, 1300, 700);

        //Scene scene = new Scene(root, 1400, 900);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

}
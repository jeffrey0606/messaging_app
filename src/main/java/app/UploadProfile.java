package app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXClippedPane;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class UploadProfile implements Initializable {

    public ImageView profile_image;

    public File fileToUpload;
    public Text user_name;
    public StackPane profile_image_container;
    public ProgressIndicator loading_profile_indicator;
    public Text error_msg;
    public JFXButton skip_button;
    public ImageView skip_icon;
    public JFXClippedPane clip_pane;

    @FXML
    public void skip_button(ActionEvent actionEvent) {
        App app = new App();

        app.goChatContainer();
    }

    public void uplaod_profile_button(MouseEvent mouseEvent) throws IOException, ParseException, SQLException {
        FileChooser fileChooser = new FileChooser();

        fileToUpload = fileChooser.showOpenDialog(new Stage());

        if(fileToUpload != null) {
            loading_profile_indicator.setVisible(true);
            profile_image.setImage(null);

            HashMap<String, Object> result = startUploading(fileToUpload);

            //..\assets\images\skip.png
            if((boolean) result.get("status") == true){
                Image img = new Image("/assets/images/success.png");

                UserData userData = new UserData();

                Dbmanager dbmanager = new Dbmanager();

                dbmanager.addProfileImage(userData.getId(), result.get("msg").toString());

                skip_icon.setImage(img);
                skip_button.setText("Ok");
                skip_button.setTextFill(Paint.valueOf("green"));

                Image profileImage = new Image("http://localhost:80/messaging_app/uploads/images/" + result.get("msg").toString());
                profile_image.setImage(profileImage);

                loading_profile_indicator.setVisible(false);
            } else {
                Image img2 = new Image("/assets/images/profile.png");
                profile_image.setImage(img2);

                loading_profile_indicator.setVisible(false);
                error_msg.setText(result.get("msg").toString());
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            error_msg.setText("");
                        });
                    }
                }, 5000);
            }
        }

    }

    public HashMap<String, Object> startUploading(File file) throws IOException, ParseException {
        HashMap<String, Object> result = new HashMap<String, Object>();
        try(CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpEntity data = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("uploadProfile", file, ContentType.DEFAULT_BINARY, file.getName()).build();

            HttpUriRequest request = RequestBuilder.post("http://localhost:80/messaging_app/upload.php").setEntity(data).build();

            System.out.println("Executing request " + request.getRequestLine());

            ResponseHandler< String > responseHandler = (response) -> {
            int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            String responseBody = httpclient.execute(request, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);

            JSONParser parser = new JSONParser();

            return (HashMap<String, Object>) parser.parse(responseBody);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserData userData = new UserData();

        Circle clip = new Circle();
        clip.setRadius(160 / 2);
        clip.setCenterX(160 / 2);
        clip.setCenterY(152 / 2);
        clip_pane.setClip(clip);

        user_name.setText(userData.getName());
    }
}

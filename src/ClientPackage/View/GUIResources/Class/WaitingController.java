package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import Server.Model.Map;
import Utilities.Class.CircularArrayList;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Giulio on 16/05/2016.
 */
public class WaitingController {

    private ClientController clientController;
    private JFXSpinner jfxSpinner = new JFXSpinner();
    private ArrayList<String> typewriterArrayList = new ArrayList<>();
    private String stringToPrint;
    private Timeline timeline = new Timeline();
    @FXML private Label typewriterLabel;
    @FXML private StackPane background;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        background.getChildren().add(jfxSpinner);
        StackPane.setAlignment(jfxSpinner, Pos.CENTER);
        jfxSpinner.radiusProperty().bind(background.widthProperty().divide(7));
        typewriter();
    }

    public void typewriter(){
        int count = 0;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                typewriterArrayList.add("Aspetto l'araldo");
                typewriterArrayList.add("Attendo il commissario");
                typewriterArrayList.add("Cercando il bigodino");
                Collections.shuffle(typewriterArrayList);
                startingTimeOut(typewriterArrayList.get(count), count);
            }
        });
    }

    private void startingTimeOut(String string, int count) {
        Random random = new Random();
        int duration = random.nextInt(1500) + 1500;
        IntegerProperty letters = new SimpleIntegerProperty(0);
        letters.addListener((a, b, c) -> {
            typewriterLabel.setText(string.substring(0, c.intValue()));
        });
        timeline = new Timeline();
        KeyValue keyValue = new KeyValue(letters, string.length());
        KeyFrame keyFrame = new KeyFrame(Duration.millis(duration), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (count == typewriterArrayList.size()-1){
                    Collections.shuffle(typewriterArrayList);
                }
                startingTimeOut(typewriterArrayList.get((count + 1)%typewriterArrayList.size()), count + 1);
            }
        });
    }

}

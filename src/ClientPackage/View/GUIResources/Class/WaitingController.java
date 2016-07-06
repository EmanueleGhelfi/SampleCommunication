package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Giulio on 16/05/2016.
 */
public class WaitingController {

    private ClientController clientController;
    private JFXSpinner jfxSpinner = new JFXSpinner();
    private ArrayList<String> typewriterArrayList = new ArrayList<>();
    private Timeline timeline = new Timeline();

    @FXML
    private Label typewriterLabel;
    @FXML
    private StackPane background;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        background.getChildren().add(jfxSpinner);
        jfxSpinner.setRadius(200);
        StackPane.setAlignment(jfxSpinner, Pos.CENTER);
        StackPane.setAlignment(typewriterLabel, Pos.CENTER);
        typewriter();
    }

    public void typewriter() {
        int count = 0;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                typewriterArrayList.add("Playing the trumpet");
                typewriterArrayList.add("Contacting the herald");
                typewriterArrayList.add("Looking for Sisi");
                typewriterArrayList.add("Saddling the unicorn");
                typewriterArrayList.add("Travelling through space and time");
                typewriterArrayList.add("Learning the Waltz");
                typewriterArrayList.add("Visiting Horst and Grete");
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
                if (count == typewriterArrayList.size() - 1) {
                    Collections.shuffle(typewriterArrayList);
                }
                startingTimeOut(typewriterArrayList.get((count + 1) % typewriterArrayList.size()), count + 1);
            }
        });
    }

}

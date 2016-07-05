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
    private final JFXSpinner jfxSpinner = new JFXSpinner();
    private final ArrayList<String> typewriterArrayList = new ArrayList<>();
    private Timeline timeline = new Timeline();

    @FXML
    private Label typewriterLabel;
    @FXML
    private StackPane background;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        this.background.getChildren().add(this.jfxSpinner);
        this.jfxSpinner.setRadius(200);
        StackPane.setAlignment(this.jfxSpinner, Pos.CENTER);
        StackPane.setAlignment(this.typewriterLabel, Pos.CENTER);
        this.typewriter();
    }

    public void typewriter() {
        int count = 0;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WaitingController.this.typewriterArrayList.add("Playing the trumpet");
                WaitingController.this.typewriterArrayList.add("Contacting the herald");
                WaitingController.this.typewriterArrayList.add("Looking for Sisi");
                WaitingController.this.typewriterArrayList.add("Saddling the unicorn");
                WaitingController.this.typewriterArrayList.add("Travelling through space and time");
                WaitingController.this.typewriterArrayList.add("Learning the Waltz");
                WaitingController.this.typewriterArrayList.add("Visiting Horst and Grete");
                Collections.shuffle(WaitingController.this.typewriterArrayList);
                WaitingController.this.startingTimeOut(WaitingController.this.typewriterArrayList.get(count), count);
            }
        });
    }

    private void startingTimeOut(String string, int count) {
        Random random = new Random();
        int duration = random.nextInt(1500) + 1500;
        IntegerProperty letters = new SimpleIntegerProperty(0);
        letters.addListener((a, b, c) -> {
            this.typewriterLabel.setText(string.substring(0, c.intValue()));
        });
        this.timeline = new Timeline();
        KeyValue keyValue = new KeyValue(letters, string.length());
        KeyFrame keyFrame = new KeyFrame(Duration.millis(duration), keyValue);
        this.timeline.getKeyFrames().add(keyFrame);
        this.timeline.play();
        this.timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (count == WaitingController.this.typewriterArrayList.size() - 1) {
                    Collections.shuffle(WaitingController.this.typewriterArrayList);
                }
                WaitingController.this.startingTimeOut(WaitingController.this.typewriterArrayList.get((count + 1) % WaitingController.this.typewriterArrayList.size()), count + 1);
            }
        });
    }

}
